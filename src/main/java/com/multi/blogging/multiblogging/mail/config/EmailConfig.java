package com.multi.blogging.multiblogging.mail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());

        return mailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//        properties.put("mail.smtp.socketFactory.class", javax.net.ssl.SSLSocketFactory.class);
//        properties.put("mail.smtp.protocol", "smtp");
//        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return properties;
    }
}