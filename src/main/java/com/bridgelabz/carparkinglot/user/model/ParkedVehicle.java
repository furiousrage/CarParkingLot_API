package com.bridgelabz.carparkinglot.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Getter
@Setter
public class ParkedVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    private long slotNumber;

    private String driverName;

    private String driverType;

    private String vehicleModel;

    private String vehicleColor;

    private String vehicleType;

    private String vehicleNumber;

    private LocalTime startTime = LocalTime.now();

    private LocalDate startDate = LocalDate.now();

    @ManyToOne
    private ParkingLots parkingLots;
}
