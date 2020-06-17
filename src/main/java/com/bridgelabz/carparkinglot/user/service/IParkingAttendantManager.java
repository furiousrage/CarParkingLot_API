package com.bridgelabz.carparkinglot.user.service;


import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.ParkVehicleDTO;

import java.io.IOException;

public interface IParkingAttendantManager {

    Response parkVehicle(ParkVehicleDTO parkVehicleDTO) throws IOException;

    Response unParkVehicle(String vehicleNumber);
}
