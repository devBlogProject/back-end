package com.multi.blogging.multiblogging.auth.controller;

import com.jayway.jsonpath.JsonPath;
import com.multi.blogging.multiblogging.auth.dto.request.MemberLoginRequestDto;
import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.dto.request.TokenReIssueRequestDto;
import com.multi.blogging.multiblogging.auth.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.multi.blogging.multiblogging.Constant.TEST_EMAIL;
import static com.multi.blogging.multiblogging.Constant.TEST_NICK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class AuthControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    ObjectMapper objectMapper = new ObjectMapper();

    static final String BEARER_PREFIX = "Bearer ";

    @BeforeEach
    void setUp() throws Exception {
        MemberSignUpRequestDto requestDto = new MemberSignUpRequestDto();
        requestDto.setEmail(TEST_EMAIL);
        requestDto.setPassword("1234");
        requestDto.setNickName(TEST_NICK);

        mockMvc.perform(post("/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        );
    }

    @Test
    @Transactional
    void 토큰발급_로그인() throws Exception {
        MemberLoginRequestDto requestDto = new MemberLoginRequestDto();
        requestDto.setEmail(TEST_EMAIL);
        requestDto.setPassword("1234");

//        given(tokenProvider.validateToken(anyString()))
//                .willReturn(true);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String accessToken = JsonPath.read(response, "$.data.accessToken");

        connectSample(accessToken);
    }

    @Test
    @Transactional
    void 토큰갱신() throws Exception {
        MemberLoginRequestDto requestDto = new MemberLoginRequestDto();
        requestDto.setEmail(TEST_EMAIL);
        requestDto.setPassword("1234");


        MvcResult result1 = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andReturn();

        String response1 = result1.getResponse().getContentAsString();
        String existedAccessToken = JsonPath.read(response1, "$.data.accessToken");
        String refreshToken = JsonPath.read(response1, "$.data.refreshToken");

        TokenReIssueRequestDto reIssueRequestDto = new TokenReIssueRequestDto();
        reIssueRequestDto.setRefreshToken(refreshToken);


        MvcResult result2 = mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reIssueRequestDto))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andReturn();

        String response2 = result2.getResponse().getContentAsString();
        String newAccessToken=JsonPath.read(response2, "$.data.accessToken");

        connectSample(newAccessToken);
    }

    private void connectSample(String accessToken) throws Exception {
        mockMvc.perform(get("/sample")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken))
                .andExpect(status().isOk());
    }
}