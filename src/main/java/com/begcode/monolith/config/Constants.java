package com.begcode.monolith.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "zh-cn";
    public static final String DATA_PATH = "data";
    public static final String BASE_PACKAGE = "com.hmtech.cms";
    public static final String DOMAIN_PACKAGE = BASE_PACKAGE + ".domain";
    public static final String REPOSITORY_PACKAGE = BASE_PACKAGE + ".repository";
    public static final String DTO_PACKAGE = BASE_PACKAGE + ".service.dto";
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".service.mapper";
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";
    public static final String WEB_REST_PACKAGE = BASE_PACKAGE + ".web.rest";
    public static final String CAPTCHA_KEY = "jhipster.sms.captcha";
    /**
     * 正则表达式:验证汉字(1-9个汉字)  {1,9} 自定义区间
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5]{1,9}$";
    /**
     * 正则表达式:验证身份证
     */
    public static final String REGEX_ID_CARD = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
    /**
     * 正则表达式:验证IP地址
     */
    public static final String REGEX_IP_ADDR =
        "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
    /**
     *说明：移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     * 验证号码 手机号 固话均可
     * 正则表达式:验证手机号
     */
    public static final String REGEX_MOBILE =
        "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";

    /** 登录二维码 */
    public static final String LOGIN_QRCODE_PRE = "QRCODELOGIN:";
    public static final String LOGIN_QRCODE = "LQ:";
    /** 登录二维码token */
    public static final String LOGIN_QRCODE_TOKEN = "LQT:";

    private Constants() {}
}
