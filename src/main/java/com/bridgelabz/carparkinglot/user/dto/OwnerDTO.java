package com.bridgelabz.carparkinglot.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;


@Getter
@Setter
public class OwnerDTO {

    @Pattern(regexp = "^([A-Z]){1}[a-zA-Z]{3,}$", message = "length must be 3")
    private String firstName;

    @Pattern(regexp = "^([A-Z]){1}[a-zA-Z]{3,}$", message = "length must be 3")
    private String lastName;

    @Pattern(regexp = "^(0|91)[ ]([0-9]{10})$", message = "Invalid mobile number")
    private long mobileNo;

    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*[._+-]?[a-zA-Z0-9]*@[A-Za-z0-9]+([.][a-zA-Z]{2,})$", message = "Invalid Email Id")
    private String emailId;

    @Pattern(regexp = "((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,})", message = "length should be minimum 8")
    private String password;

    private String actor;


}
