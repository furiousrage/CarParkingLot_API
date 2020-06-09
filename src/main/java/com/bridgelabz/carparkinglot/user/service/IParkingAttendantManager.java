package com.bridgelabz.carparkinglot.user.service;


import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.ParkVehicleDTO;

public interface IParkingAttendantManager {

    Response parkVehicle(ParkVehicleDTO parkVehicleDTO);

    Response unParkVehicle(String vehicleNumber);
}
