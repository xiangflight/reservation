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

        FORMAT_PHONE_ERROR(2003, "手机号码格式错误");

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
