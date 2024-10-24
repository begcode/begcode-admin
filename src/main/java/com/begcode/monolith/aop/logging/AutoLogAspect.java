package com.begcode.monolith.aop.logging;

import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.log.service.SysLogService;
import com.begcode.monolith.log.service.dto.SysLogDTO;
import com.begcode.monolith.security.SecurityUtils;
import com.begcode.monolith.util.IPUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统日志，切面处理类
 *
 */
@Aspect
@Component
public class AutoLogAspect {

    static ObjectMapper mapper;

    @Resource
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.begcode.monolith.aop.logging.AutoLog)")
    public void logPointCut() {}

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        // 保存日志
        saveSysLog(point, time, result);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, Object obj) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLogDTO dto = new SysLogDTO();
        AutoLog syslog = method.getAnnotation(AutoLog.class);
        if (syslog != null) {
            String content = syslog.value();
            /*if(syslog.module() == ModuleType.ONLINE){
                content = getOnlineLogContent(obj, content);
            }*/
            // 注解上的描述,操作日志内容
            dto.setLogType(syslog.logType());
            dto.setLogContent(content);
        }

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        dto.setMethod(className + "." + methodName + "()");

        // 设置操作类型
        if (dto.getLogType() == LogType.OPERATE) {
            dto.setOperateType(syslog.operateType());
        }

        // 获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 请求的参数
        dto.setRequestParam(getReqestParams(request, joinPoint));
        // 设置IP地址
        dto.setIp(IPUtils.getIpAddr(request));
        // 获取登录用户信息
        SecurityUtils.getCurrentUserLogin().ifPresent(dto::setUserid);
        // 耗时
        dto.setCostTime(time);
        dto.setRequestUrl(request.getRequestURI());
        // 保存系统日志
        sysLogService.save(dto);
    }

    /**
     * @Description: 获取请求参数
     * @param request:  request
     * @param joinPoint:  joinPoint
     * @Return: java.lang.String
     */
    private String getReqestParams(HttpServletRequest request, JoinPoint joinPoint) {
        String httpMethod = request.getMethod();
        StringBuilder params = new StringBuilder();
        if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod)) {
            Object[] paramsArray = joinPoint.getArgs();
            // java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
            //  https://my.oschina.net/mengzhang6/blog/2395893
            Object[] arguments = new Object[paramsArray.length];
            for (int i = 0; i < paramsArray.length; i++) {
                if (
                    paramsArray[i] instanceof BindingResult ||
                    paramsArray[i] instanceof ServletRequest ||
                    paramsArray[i] instanceof ServletResponse ||
                    paramsArray[i] instanceof MultipartFile
                ) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = paramsArray[i];
            }
            try {
                params = new StringBuilder(mapper.writeValueAsString(arguments));
            } catch (JsonProcessingException e) {
                params = new StringBuilder("参数序列化失败");
            }
        } else {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            StandardReflectionParameterNameDiscoverer u = new StandardReflectionParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            if (args != null && paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
                }
            }
        }
        return params.toString();
    }

    static {
        mapper = new ObjectMapper();
        PropertyFilter propertyFilter = new PropertyFilter() {
            @Override
            public void serializeAsField(
                Object pojo,
                com.fasterxml.jackson.core.JsonGenerator gen,
                com.fasterxml.jackson.databind.SerializerProvider prov,
                PropertyWriter writer
            ) throws Exception {
                writer.serializeAsField(pojo, gen, prov);
            }

            @Override
            public void serializeAsElement(
                Object elementValue,
                com.fasterxml.jackson.core.JsonGenerator gen,
                com.fasterxml.jackson.databind.SerializerProvider prov,
                PropertyWriter writer
            ) throws Exception {
                if (elementValue != null && elementValue.toString().length() <= 500) {
                    writer.serializeAsElement(elementValue, gen, prov);
                }
            }

            @Override
            public void depositSchemaProperty(
                PropertyWriter writer,
                ObjectNode propertiesNode,
                com.fasterxml.jackson.databind.SerializerProvider provider
            ) throws JsonMappingException {
                writer.depositSchemaProperty(propertiesNode, provider);
            }

            @Override
            public void depositSchemaProperty(
                PropertyWriter writer,
                JsonObjectFormatVisitor objectVisitor,
                com.fasterxml.jackson.databind.SerializerProvider provider
            ) throws JsonMappingException {
                writer.depositSchemaProperty(objectVisitor, provider);
            }
        };
        mapper.setFilterProvider(new SimpleFilterProvider().addFilter("profilter", propertyFilter));
    }
}
