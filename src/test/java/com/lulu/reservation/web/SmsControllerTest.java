package com.lulu.reservation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lulu.reservation.domain.database.Sms;
import com.lulu.reservation.domain.request.SmsRequest;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.repository.SmsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/8 下午6:07
 * <p>
 * 类说明：
 *     Sms的单元测试
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SmsControllerTest {

    private static final String POST_PHONE = "1111";
    private static final String POST_URL = "/sms/verification/code";
    private static final String EXPECTED_STRING = "请求成功";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SmsRepository smsRepository;

    @Before
    public void setup() {
        smsRepository.deleteAll();
    }

    @Test
    public void sendVerificationCode() throws Exception{
        ResultActions resultActions = sendCreatePostRequest();
        checkResultValid(resultActions);
    }

    /**
     * 发送POST请求
     * @return 请求结果
     * @throws Exception 异常
     */
    private ResultActions sendCreatePostRequest() throws Exception {
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setPhone(POST_PHONE);
        return mockMvc.perform(MockMvcRequestBuilders.post(POST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(smsRequest)));
    }

    /**
     * 检查结果
     * @param resultActions resultActions
     * @throws Exception 异常
     */
    private void checkResultValid(ResultActions resultActions) throws Exception{
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        Resp resp = objectMapper.readValue(response, Resp.class);
        assertEquals(EXPECTED_STRING, resp.getMessage());
    }

}