package com.bridgelabz.carparkinglot.user.controller;


import com.bridgelabz.carparkinglot.exception.LoginException;
import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.LoginDTO;
import com.bridgelabz.carparkinglot.user.dto.OwnerDTO;
import com.bridgelabz.carparkinglot.user.dto.ParkVehicleDTO;
import com.bridgelabz.carparkinglot.user.dto.ParkingLotsDTO;
import com.bridgelabz.carparkinglot.user.service.IParkingAttendantManager;
import com.bridgelabz.carparkinglot.user.service.IUserService;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/carparkinng")
public class UserController {

    @Autowired
    private IParkingAttendantManager iParkingAttendantManager;

    @Autowired
    private IUserService iUserService;

    @PostMapping("/register")
    public String addUser(@RequestBody OwnerDTO ownerDTO) throws LoginException {
        return iUserService.registration(ownerDTO);
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

    @PostMapping("/add/{token}/{noOfParkingLotSystem}")
    public Response addingParkingLotSystem(@PathVariable String token, @PathVariable long noOfParkingLotSystem) {
        return iUserService.createParkingLotSystem(token, noOfParkingLotSystem);
    }

    @PostMapping("/{token}")
    public Response addingParkingLots(@PathVariable String token, @RequestBody ParkingLotsDTO parkinglotsDto) {
        return iUserService.createParkingLot(token, parkinglotsDto);
    }

    @RequestMapping(value = "/parkVehicle", method = RequestMethod.POST)
    public Response parkVehicle(@RequestBody ParkVehicleDTO parkVehicleDTO) {
        return iParkingAttendantManager.parkVehicle(parkVehicleDTO);
    }

    @RequestMapping(value = "/unParkVehicle", method = RequestMethod.GET)
    public Response unParkVehicle(@RequestParam String vehicleNumber) {
        return iParkingAttendantManager.unParkVehicle(vehicleNumber);
    }

}
