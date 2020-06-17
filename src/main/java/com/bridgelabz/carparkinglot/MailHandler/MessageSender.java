package com.bridgelabz.carparkinglot.MailHandler;

import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.MailDTO;
import com.bridgelabz.carparkinglot.user.model.UserRegistration;
import com.bridgelabz.carparkinglot.user.repository.UserRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageSender {
    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Autowired
    private MailDTO mailDTO;

    @Autowired
    private RabbitMqInitializer rabbitMqInitializer;


    public Response sendEmail(String email, String sub) throws IOException {
        UserRegistration userRecord = userRegistrationRepository.findByEmailId(email);
        String body = null;
        if (userRecord != null) {
            mailDTO.setMailTo(email);
            mailDTO.setMailFrom("pooponchipoo@gmail.com");
            mailDTO.setMailSubject(sub);
                body = "parkingLot is full..";
            mailDTO.setMailBody(body);
            rabbitMqInitializer.sendMessageToQueue(mailDTO);
            rabbitMqInitializer.send(mailDTO);
            return new Response("parkingLot is full..", 202);
        }
        return new Response("User notExist", 404);
    }
}
