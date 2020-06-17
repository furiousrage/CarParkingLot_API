package com.bridgelabz.carparkinglot.user.controller;


import com.bridgelabz.carparkinglot.exception.LoginException;
import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.LoginDTO;
import com.bridgelabz.carparkinglot.user.dto.UserRegistrationDTO;
import com.bridgelabz.carparkinglot.user.dto.ParkVehicleDTO;
import com.bridgelabz.carparkinglot.user.dto.ParkingLotSystemDTO;
import com.bridgelabz.carparkinglot.user.service.IParkingAttendantManager;
import com.bridgelabz.carparkinglot.user.service.IUserService;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/carparkinng")
public class UserController {

    @Autowired
    private IParkingAttendantManager iParkingAttendantManager;

    @Autowired
    private IUserService iUserService;

    @PostMapping("/register")
    public String addUser(@RequestBody UserRegistrationDTO userRegistrationDTO) throws LoginException {
        return iUserService.registration(userRegistrationDTO);
    }

    @GetMapping(value = "/{token}")
    public ResponseEntity<Response> emailValidation(@PathVariable String token) throws UserException {
        Response response = iUserService.emailValidate(token);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @PostMapping("/userLogin")
    public ResponseEntity<Response> login(@RequestBody LoginDTO loginDTO) throws LoginException {
        Response response = iUserService.loginValidation(loginDTO);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @PostMapping("/ParkingLotSystem}")
    public Response addingParkingLotSystem(ParkingLotSystemDTO parkingLotSystemDTO) {
        return iUserService.createParkingLotSystem(parkingLotSystemDTO);
    }

    @RequestMapping(value = "/parkVehicle", method = RequestMethod.POST)
    public Response parkVehicle(@RequestBody ParkVehicleDTO parkVehicleDTO) throws IOException {
        return iParkingAttendantManager.parkVehicle(parkVehicleDTO);
    }

    @RequestMapping(value = "/unParkVehicle", method = RequestMethod.GET)
    public Response unParkVehicle(@RequestParam String vehicleNumber) {
        return iParkingAttendantManager.unParkVehicle(vehicleNumber);
    }

}
