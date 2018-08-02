package com.lulu.reservation.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.comm.config.WechatParameter;
import com.lulu.reservation.comm.exception.AesException;
import com.lulu.reservation.domain.database.User;
import com.lulu.reservation.repository.UserRepository;
import com.lulu.reservation.util.Sha1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/14 下午6:09
 * <p>
 * 类说明：
 *     微信公众号Controller
 */
@Slf4j
@Controller
@RequestMapping("/wx")
public class WxMpController {

    private final WechatParameter wechatParameter;

    private final UserRepository userRepository;

    @Autowired
    public WxMpController(WechatParameter wechatParameter, UserRepository userRepository) {
        this.wechatParameter = wechatParameter;
        this.userRepository = userRepository;
    }

    @RequestMapping("/handler")
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

    @GetMapping("/auth")
    public ModelAndView auth() {
        String url = Constants.URL_RELAY;
        try {
            log.info("url: {}", url);
            final String encodeUrl = URLEncoder.encode(url, "UTF-8");
            log.info("encodeUrl: {}", encodeUrl);
            String authUrl = String.format(Constants.WX_MP_AUTHORIZATION_URL, wechatParameter.getId(), encodeUrl);
            log.info("authUrl: {}", authUrl);
            return new ModelAndView("redirect:" + authUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/relay")
    public ModelAndView relay(HttpServletRequest request) {
        final String code = request.getParameter("code");
        log.info("code: {}", code);
        try {
            if (StringUtils.isNotEmpty(code)) {
                RestTemplate restTemplate = new RestTemplate();
                // 1. 获取openId
                String getOpenIdUrl = String.format(Constants.WX_MP_GET_OPENID_URL, wechatParameter.getId(),
                        wechatParameter.getSecret(), code);
                String response = restTemplate.getForObject(getOpenIdUrl, String.class);
                log.info("openId response: {}", response);
                JSONObject openIdJson = JSON.parseObject(response);
                final String openid = openIdJson.getString("openid");
                // 2. 获取access_token
                String getTokenUrl = String.format(Constants.WX_MP_GET_ACCESS_TOKEN_URL, wechatParameter.getId(), wechatParameter.getSecret());
                response = restTemplate.getForObject(getTokenUrl, String.class);
                log.info("token response: {}", response);
                JSONObject accessTokenJson = JSON.parseObject(response);
                String accessToken = accessTokenJson.getString("access_token");
                // 3. 打印 access_token 和 openid
                log.info("access_token: {}, openid: {}", accessToken, openid);
                // 4. 判断用户是否点击进来过
                User existUser = userRepository.findByMpOpenid(openid);
                if (existUser != null) {
                    if (StringUtils.isNotEmpty(existUser.getPhone())) {
                        return new ModelAndView("redirect:" + Constants.URL_INDEX + "?openid=" + openid);
                    } else {
                        String mpHeaderImg = existUser.getMpHeaderImg();
                        return new ModelAndView("redirect:" + Constants.URL_LOGIN + "?img=" + mpHeaderImg + "&openid="+ openid);
                    }
                } else {
                    String getUserInfoUrl = String.format(Constants.WX_MP_GET_BASE_USER_INFO_URL, accessToken, openid);
                    String userInfo = new String(restTemplate.getForObject(getUserInfoUrl,
                            String.class).getBytes("ISO-8859-1"), "UTF-8");
                    log.info("user info: {}", userInfo);
                    JSONObject baseInfoResponse = JSON.parseObject(userInfo);
                    User user = User.newInstance();
                    final String nickname = baseInfoResponse.getString("nickname");
                    final int sex = baseInfoResponse.getIntValue("sex");
                    final String language = baseInfoResponse.getString("language");
                    final String city = baseInfoResponse.getString("city");
                    final String province = baseInfoResponse.getString("province");
                    final String country = baseInfoResponse.getString("country");
                    final String headerImage = baseInfoResponse.getString("headimgurl");
                    final long subscribeTime = baseInfoResponse.getLongValue("subscribe_time");
                    final String unionId = baseInfoResponse.getString("unionid");
                    final String remark = baseInfoResponse.getString("remark");
                    final int groupId = baseInfoResponse.getIntValue("groupid");
                    final String subscribeScene = baseInfoResponse.getString("subscribe_scene");
                    user.setMpOpenid(openid);
                    user.setMpNickname(nickname);
                    user.setMpSex(sex);
                    user.setMpLanguage(language);
                    user.setMpCity(city);
                    user.setMpProvince(province);
                    user.setMpCountry(country);
                    user.setMpHeaderImg(headerImage);
                    user.setMpSubscribeTime(subscribeTime);
                    user.setMpUnionId(unionId);
                    user.setMpRemark(remark);
                    user.setMpGroupId(groupId);
                    user.setMpSubscribeScene(subscribeScene);
                    userRepository.save(user);
                    return new ModelAndView("redirect:" + Constants.URL_LOGIN + "?img=" + headerImage + "&openid=" + openid);
                }
            }
        } catch (Exception e) {
            log.error("未获取到code");
        }
        return null;
    }

}
