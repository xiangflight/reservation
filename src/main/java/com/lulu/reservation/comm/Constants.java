package com.lulu.reservation.comm;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午5:04
 * <p>
 * 类说明：
 *     常量相关类
 */

public class Constants {

    public static final String SMS_URL = "https://api.sms.jpush.cn/v1/codes";

    private static final String JG_APP_KEY = "0c5dcc959c39a0e1c3059580";

    private static final String JG_MASTER_KEY = "b4d762d444e0712864b3b975";

    private static final String DELIMITER_COLON= ":";

    public static final String JG_SMS_AUTHORIZATION = JG_APP_KEY + DELIMITER_COLON + JG_MASTER_KEY;

    public enum SmsType {
        /**
         * 短信验证码
         */
        VERIFICATION_CODE
    }

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
