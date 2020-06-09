package com.bridgelabz.carparkinglot.user.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Owner {

    @Id
    @GeneratedValue
    private long userId;

    private String firstName;

    private String lastName;

    private long mobileNo;

    private String emailId;

    private String password;

    private LocalTime startTime = LocalTime.now();

    private LocalDate startDate = LocalDate.now();

    private boolean isVerify;

    private String actor;

    @OneToMany(mappedBy = "owner")
    private List<ParkingLotSystem> parkingLotSystemList;

    public void createParkingLotSystem(ParkingLotSystem parkingLotSystem) {
        this.parkingLotSystemList.add(parkingLotSystem);
    }

}
