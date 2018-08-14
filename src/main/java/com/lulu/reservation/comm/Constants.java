package com.lulu.reservation.comm;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午5:04
 * <p>
 * 类说明：
 *     常量相关类
 */

public class Constants {

    public static final String HTTP_AUTHORIZATION = "Authorization";

    public static final String HTTP_BASIC = "Basic ";

    public static final String HTTP_ACCEPT = "Accept";

    public static final String HTTP_MEDIA_TYPE = "application/json;charset=UTF-8";

    public static final String SMS_TEMPLATE_MOBILE = "mobile";

    public static final String SMS_TEMPLATE_ID = "temp_id";

    public static final String WX_MP_AUTHORIZATION_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=0#wechat_redirect";

    public static final String WX_MP_GET_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    public static final String WX_MP_GET_BASE_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

    public static final String WX_MP_GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    public static final String SERVICE_ACCESS_URL_PREFIX = "http://zynei.com/reservation/";

    public static final String URL_LOGIN = "http://zynei.com/reservation/login";

    public static final String URL_RELAY = "http://zynei.com/reservation/wx/relay";

    public static final String URL_INDEX = "http://zynei.com/reservation/index";

    public static final String UPLOAD_FILE_DIR = "/home/dev/tomcat/webapps/file/";

    public static final String AVATAR_PREFIX = "http://zynei.com/file/";

    /**
     * 短信类型
     */
    public enum SmsType {
        /**
         * 短信验证码
         */
        VERIFICATION_CODE
    }

    /**
     * 状态码
     */
    public enum StatusErr {
        /**
         * 公共状态
         */
        INVALID(0, "失效"),
        VALID(1, "有效"),
        DELETED(9999, "已删除");

        private Integer code;
        private String msg;

        StatusErr(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 错误码
     */
    public enum ApiErr {
        /**
         * 系统状态值
         */
        SUCCESS(2000, "请求成功"),
        UNKNOWN_ERROR(2001, "未知错误"),

        EMPTY_PHONE_ERROR(2002, "手机号为空"),

        FORMAT_PHONE_ERROR(2003, "手机号码格式错误"),

        VERIFICATION_CODE_ERROR(2004, "验证码错误");

        private Integer code;
        private String msg;

        ApiErr(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

}
