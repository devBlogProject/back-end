//package com.multi.blogging.multiblogging.mail;
//
//import com.multi.blogging.multiblogging.auth.service.EmailService;
//import com.multi.blogging.multiblogging.infra.redisDb.RedisService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ActiveProfiles("test")
//@SpringBootTest
//class MailServiceTest {
//    @Autowired
//    EmailService mailService;
//
//    @Autowired
//    RedisService redisService;
//
//    private String testUserEmail = "sky020419@gmail.com";
//
//    @Test
//    void 메일발송테스트() throws Exception {
//        mailService.sendAuthCodeEmail(testUserEmail, "테스트이메일 제목", "테스트이메일 내용");
//        assertNotNull(redisService.getValues("AuthCode " + testUserEmail));
//    }
//
//}