//package com.multi.blogging.multiblogging.mail;
//
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
//    MailService mailService;
//
//    private String testUserEmail="rmagksfla000@naver.com";
//
//    @Test
//    void 메일발송테스트(){
//        mailService.sendEmail(testUserEmail,"테스트이메일 제목","테스트이메일 내용");
//    }
//
//}