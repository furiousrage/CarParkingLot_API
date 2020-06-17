package com.bridgelabz.carparkinglot.MailHandler;


import com.bridgelabz.carparkinglot.user.dto.MailDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;



@Component
public class RabbitMqInitializer {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMessageToQueue(MailDTO mailDTO)  {
        final String exchange = "QueueExchangeConn";
        final String routingKey = "RoutingKey";
        rabbitTemplate.convertAndSend(exchange, routingKey, mailDTO);

    }

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void send(MailDTO mailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getMailTo());
        message.setFrom(mailDTO.getMailFrom());
        message.setSubject(mailDTO.getMailSubject());
        message.setText(mailDTO.getMailBody());
        javaMailSender.send(message);
        System.out.println("Mail Sent Successfully");
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void receiveMessage(MailDTO mailDTO) {

        send(mailDTO);
    }

}
