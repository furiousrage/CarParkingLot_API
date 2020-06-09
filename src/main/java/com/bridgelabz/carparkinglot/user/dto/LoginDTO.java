package com.bridgelabz.carparkinglot.user.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class LoginDTO {

    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*[._+-]?[a-zA-Z0-9]*@[A-Za-z0-9]+([.][a-zA-Z]{2,})$", message = "Invalid Email Id")
    private String emailId;

    @Pattern(regexp = "((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,})", message = "length should be minimum 8")
    private String password;
}
