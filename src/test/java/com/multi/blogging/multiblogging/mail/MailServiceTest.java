package com.multi.blogging.multiblogging.mail;

import com.multi.blogging.multiblogging.auth.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MailServiceTest {
    @Autowired
    EmailService mailService;

    private String testUserEmail="rmagksfla000@naver.com";

    @Test
    void 메일발송테스트() throws Exception {
        mailService.sendAuthCodeEmail(testUserEmail,"테스트이메일 제목","테스트이메일 내용");
    }

}