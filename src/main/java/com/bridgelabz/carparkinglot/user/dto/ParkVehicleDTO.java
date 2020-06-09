package com.bridgelabz.carparkinglot.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParkVehicleDTO {

    private String driverName;

    private String driverType;

    private String vehicleModel;

    private String vehicleColor;

    private String vehicleType;

    private String vehicleNumber;
}
