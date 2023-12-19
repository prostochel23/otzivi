package com.example.otzivi.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import static java.lang.Math.abs;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {


    public String sendConfirmMessage(String to, Long id) {
        String code = generateCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vfvfigfgfiz@gmail.com");
        message.setTo(to);
        message.setSubject("Регистрация на Otzivi");
        message.setText("Ваша ссылка для подтверждения регистрации: http://localhost:8080/confirm/"+id+"/" + code);
        getJavaMailSender().send(message);
        return code;
    }
    public String sendRecoveryMessage(String to, Long id) {
        String code = generateCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vfvfigfgfiz@gmail.com");
        message.setTo(to);
        message.setSubject("Восстановление на Otzivi");
        message.setText("Ваша ссылка восстановления пароля: http://localhost:8080/recovery/"+id+"/" + code);
        getJavaMailSender().send(message);
        return code;
    }
    public String generateCode()
    {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase(Locale.ROOT);String digits = "0123456789";
        char[] alphabet = (upper + lower + digits).toCharArray();
        int alphabetLen = alphabet.length;
        Random random = new Random(System.currentTimeMillis());
        int lenght = 8;
        String code = "";
        for (int i = 0; i < lenght;i++)
            code += alphabet[random.nextInt(0, alphabetLen)];
        return code;
    }
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("vfvfigfgfiz@gmail.com");
        mailSender.setPassword("tcgbyatrxnlzcxqn");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}