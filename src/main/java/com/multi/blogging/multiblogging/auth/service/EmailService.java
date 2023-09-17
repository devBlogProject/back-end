package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.redis.RedisService;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private static final long authCodeExpirationMillis = 1000*60*30; // 30분
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String fromUser;

    public void sendAuthCodeEmail(String toEmail, String title, String text) throws Exception {
        MimeMessage message = createMessage(toEmail,title,text);
        redisService.setValues(AUTH_CODE_PREFIX+toEmail,text, Duration.ofMillis(authCodeExpirationMillis));
        try {
            mailSender.send(message);
        } catch (RuntimeException e) {
            log.debug("EmailService.sendEmail exception occur toEmail: {}, " + "title: {}, text: {}", toEmail, title, text);
            throw new RuntimeException("메일 발송 실패");
        }
    }

    private MimeMessage createMessage(String toEmail,String title, String code) throws Exception{
        log.info("보내는 대상 : {}, 인증번호 : {}, 발송 시간 : {}",toEmail,code,System.currentTimeMillis());
        MimeMessage message = mailSender.createMimeMessage();
        LocalDateTime expiredTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusMinutes(authCodeExpirationMillis/(1000*60));
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM);

        message.addRecipients(Message.RecipientType.TO, toEmail);
        message.setSubject(title);

        String msg="";
        msg+= "<div style='margin:20px;'>";
        msg+= "<h1> 안녕하세요 Multiblogging 인증 메일입니다. </h1>";
        msg+= "<br>";
        msg+= "<p>아래 코드를 입력해주세요<p>";
        msg+= "<br>";
        msg+= "<p> 유효기간 : " + dtf.format(expiredTime) +"<p>";
        msg+= "<br>";
        msg+= "<p>감사합니다.<p>";
        msg+= "<br>";
        msg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg+= "<h3 style='color:blue;'>이메일 인증 코드입니다.</h3>";
        msg+= "<div style='font-size:130%'>";
        msg+= "CODE : <strong>";
        msg+= code+"</strong><div><br/> ";
        msg+= "</div>";
        message.setText(msg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress(fromUser,fromUser.substring(0,fromUser.indexOf('@'))));//보내는 사람

        return message;
    }

    public String createCode() {
        int codeLength = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < codeLength; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new RuntimeException("인증코드 생성 에러");
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
