package com.bridgelabz.carparkinglot.utility;


import com.bridgelabz.carparkinglot.user.model.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RegistrationMailService {

    @Autowired
    private JwtUtill jwtUtill;

    private JavaMailSender mailSender;

    @Autowired
    public RegistrationMailService(JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    public void sendNotification(UserRegistration userRegistration) throws MailException {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userRegistration.getEmailId());
        mailMessage.setFrom("pooponchipoo@gmail.com");
        mailMessage.setSubject("Thank you For Registering With Us : " + userRegistration.getFirstName() + userRegistration.getLastName());
        mailMessage.setText("please click on the link to verify yourself : " + "http://localhost:8080/carparkinng/" + jwtUtill.createToken(userRegistration.getUserId()));

        mailSender.send(mailMessage);
    }

}
