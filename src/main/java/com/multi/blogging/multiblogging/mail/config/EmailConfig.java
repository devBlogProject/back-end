//package com.multi.blogging.multiblogging.mail.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class EmailConfig {
//
//    @Value("${spring.mail.host}")
//    private String host;
//
//    @Value("${spring.mail.port}")
//    private int port;
//
//    @Value("${spring.mail.username}")
//    private String username;
//
//    @Value("${spring.mail.password}")
//    private String password;
//
//    @Value("${spring.mail.properties.mail.smtp.auth}")
//    private boolean auth;
//
//    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
//    private boolean starttls;
//
//    @Value("${spring.mail.properties.mail.smtp.timeout}")
//    private int timeout;
//
//    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
//    private boolean sslEnable;
//
//    @Value("${spring.mail.properties.mail.debug}")
//    private boolean debugEnable;
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(host);
//        mailSender.setPort(port);
//        mailSender.setUsername(username);
//        mailSender.setPassword(password);
//        mailSender.setDefaultEncoding("UTF-8");
//        mailSender.setJavaMailProperties(getMailProperties());
//
//        return mailSender;
//    }
//
//    private Properties getMailProperties() {
//        Properties properties = new Properties();
//        properties.put("mail.smtp.auth", auth);
//        properties.put("mail.smtp.timeout", timeout);
//        properties.put("mail.starttls.enable", starttls);
//        properties.put("mail.smtp.ssl.enable", sslEnable);
//        properties.put("mail.debug", debugEnable);
//        properties.put("mail.smtp.port", port);
////        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
////        properties.put("mail.smtp.socketFactory.class", javax.net.ssl.SSLSocketFactory.class);
////        properties.put("mail.smtp.protocol", "smtp");
////        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
//
//        return properties;
//    }
//}