package com.begcode.monolith.service.safety;

import com.begcode.captcha.model.common.ResponseModel;
import com.begcode.captcha.model.vo.CaptchaVO;
import com.begcode.captcha.service.CaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SafetyService {

    private final CaptchaService captchaService;

    public SafetyService(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * 获取行为验证码
     */
    public ResponseModel getSafetyCode(CaptchaVO data, HttpServletRequest request) {
        assert request.getRemoteHost() != null;
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.get(data);
    }

    /**
     * 验证行为验证码
     */
    public ResponseModel checkSafetyCode(CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.check(data);
    }

    /**
     * 行为验证码二次校验
     */
    public ResponseModel verifySafetyCode(CaptchaVO data) {
        return captchaService.verification(data);
    }

    public static String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StringUtils.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StringUtils.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StringUtils.trim(ipList[0]);
        }
        return null;
    }
}
