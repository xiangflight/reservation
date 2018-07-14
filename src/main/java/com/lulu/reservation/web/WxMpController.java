package com.lulu.reservation.web;

import com.lulu.reservation.comm.config.WechatParameter;
import com.lulu.reservation.comm.exception.AesException;
import com.lulu.reservation.util.Sha1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/14 下午6:09
 * <p>
 * 类说明：
 *     微信公众号控制器
 */
@Slf4j
@Controller
public class WxMpController {

    private final WechatParameter wechatParameter;

    @Autowired
    public WxMpController(WechatParameter wechatParameter) {
        this.wechatParameter = wechatParameter;
    }

    @RequestMapping("/wx/handler")
    public void wxMpHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream output = response.getOutputStream();
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotEmpty(echostr) && StringUtils.isNotEmpty(signature)) {
            authDevAbility(signature, timestamp, nonce, echostr, output);
        }
    }

    /**
     * 检测签名，验证是微信公众号开发者
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @param output ServletOutputStream对象
     */
    private void authDevAbility(String signature, String timestamp, String nonce, String echostr, ServletOutputStream output) {
        try {
            String sha1 = Sha1.getSHA1(wechatParameter.getToken(), timestamp, nonce, "");
            log.info("signature = {}, sha1 = {}", signature, sha1);
            if (signature.equals(sha1)) {
                outputStreamWrite(output, echostr);
            }
        } catch (AesException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据流输出
     *
     * @param outputStream outputStream
     * @param text text 返回内容
     */
    private void outputStreamWrite(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
