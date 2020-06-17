package com.bridgelabz.carparkinglot.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@Component
public class MailDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    private String mailTo;
    private String mailFrom;
    private String mailSubject;
    private String mailBody;
}
