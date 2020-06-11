package com.bridgelabz.carparkinglot.user.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLotSystemDTO {

    private int noOfParkingLotSystem;

    private LoginDTO loginDTO;

    private int noOfParkingLot;

    private int[] slotCapacity;

}
