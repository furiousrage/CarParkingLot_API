package com.bridgelabz.carparkinglot.user.service;


import com.bridgelabz.carparkinglot.exception.LoginException;
import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.LoginDTO;
import com.bridgelabz.carparkinglot.user.dto.OwnerDTO;
import com.bridgelabz.carparkinglot.user.dto.ParkingLotsDTO;

public interface IUserService {

    String registration(OwnerDTO ownerDTO) throws LoginException;

    Response loginValidation(LoginDTO loginDTO) throws LoginException;

    Response emailValidate(String token);

    Response createParkingLotSystem(String token, long noOfParkingSystem);

    Response createParkingLot(String token, ParkingLotsDTO parkinglotsDto);

}
