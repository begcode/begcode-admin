package com.begcode.monolith.system.service;

import com.begcode.monolith.config.Constants;
import java.util.LinkedHashMap;
import java.util.List;
import javax.cache.CacheManager;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final CacheManager cacheManager;

    private final Logger log = LoggerFactory.getLogger(SmsService.class);

    public SmsService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 发送固定消息模板短信
     *
     * @param phone
     * @param message
     * @return
     */
    public Boolean sendMessage(String phone, String message) {
        SmsResponse response = SmsFactory.getSmsBlend().sendMessage(phone, message);
        if (!response.isSuccess()) {
            log.error("短信发送失败，错误描述：{}", response.getData().toString());
        }
        return response.isSuccess();
    }

    /**
     * 使用自定义模板发送短信
     *
     * @param phone
     * @param templateId
     * @param messages
     * @return
     */
    public Boolean sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        SmsResponse response = SmsFactory.getSmsBlend().sendMessage(phone, templateId, messages);
        if (!response.isSuccess()) {
            log.error("短信发送失败，错误描述：{}", response.getData().toString());
        }
        return response.isSuccess();
    }

    /**
     * 群发短信，固定模板短信
     *
     * @param phones
     * @param message
     * @return
     */
    public Boolean massTexting(List<String> phones, String message) {
        SmsResponse response = SmsFactory.getSmsBlend().massTexting(phones, message);
        if (!response.isSuccess()) {
            log.error("短信发送失败，错误描述：{}", response.getData().toString());
        }
        return response.isSuccess();
    }

    /**
     * 使用自定义模板群发短信
     *
     * @param phones
     * @param templateId
     * @param messages
     * @return
     */
    public Boolean massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        SmsResponse response = SmsFactory.getSmsBlend().massTexting(phones, templateId, messages);
        if (!response.isSuccess()) {
            log.error("短信发送失败，错误描述：{}", response.getData().toString());
        }
        return response.isSuccess();
    }

    /**
     * 异步发送固定模板短信
     *
     * @param phone
     * @param message
     * @param callBack
     */
    public void sendMessageAsync(String phone, String message, CallBack callBack) {
        SmsFactory.getSmsBlend().sendMessageAsync(phone, message, callBack);
    }

    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack) {
        SmsFactory.getSmsBlend().sendMessageAsync(phone, templateId, messages, callBack);
    }

    /**
     * 使用固定模板发送延时短信
     *
     * @param phone
     * @param message
     * @param delayedTime
     */
    public void delayedMessage(String phone, String message, Long delayedTime) {
        SmsFactory.getSmsBlend().delayedMessage(phone, message, delayedTime);
    }

    /**
     * 群发固定模板延迟短信
     *
     * @param phones
     * @param message
     * @param delayedTime
     */
    public void delayMassTexting(List<String> phones, String message, Long delayedTime) {
        SmsFactory.getSmsBlend().delayMassTexting(phones, message, delayedTime);
    }

    /**
     * 群发自定义模板延迟短信
     *
     * @param phones
     * @param templateId
     * @param messages
     * @param delayedTime
     */
    public void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        SmsFactory.getSmsBlend().delayMassTexting(phones, templateId, messages, delayedTime);
    }

    public Boolean sendValidate(String phone) {
        String templateId = ""; // todo 短信模板Id
        String templateName = ""; // todo 验证码变量名
        LinkedHashMap<String, String> message = new LinkedHashMap<>();
        String code = String.valueOf(RandomUtils.nextInt(100000, 999999));
        message.put(templateName, code);
        if (sendMessage(phone, templateId, message)) {
            cacheManager.getCache(Constants.CAPTCHA_KEY).put(Constants.CAPTCHA_KEY + phone, code);
            return true;
        }
        return false;
    }

    public boolean validateMessage(String mobile, String code) {
        String cache = (String) cacheManager.getCache(Constants.CAPTCHA_KEY).get(Constants.CAPTCHA_KEY + mobile);
        if (StringUtils.isNotBlank(code) && StringUtils.equalsIgnoreCase(cache, code)) {
            cacheManager.getCache(Constants.CAPTCHA_KEY).remove(Constants.CAPTCHA_KEY + mobile);
            return true;
        }
        return false;
    }
}
