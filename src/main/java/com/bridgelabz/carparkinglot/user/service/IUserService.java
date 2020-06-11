package com.bridgelabz.carparkinglot.user.service;


import com.bridgelabz.carparkinglot.exception.LoginException;
import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.LoginDTO;
import com.bridgelabz.carparkinglot.user.dto.UserRegistrationDTO;
import com.bridgelabz.carparkinglot.user.dto.ParkingLotSystemDTO;

public interface IUserService {

    String registration(UserRegistrationDTO userRegistrationDTO) throws LoginException;

    Response loginValidation(LoginDTO loginDTO) throws LoginException;

    Response emailValidate(String token);

    Response createParkingLotSystem(ParkingLotSystemDTO parkingLotSystemDTO);


}
