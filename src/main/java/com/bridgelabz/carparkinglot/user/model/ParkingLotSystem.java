package com.bridgelabz.carparkinglot.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ParkingLotSystem {

    @Id
    private long parkingLotSystemId;

    public ParkingLotSystem(long parkingLotSystemId) {
        this.parkingLotSystemId = parkingLotSystemId;
    }

    @ManyToOne
    private UserRegistration owner;


    @OneToMany(mappedBy = "parkingLotSystem")
    private List<ParkingLots> parkingLotsList = new ArrayList<>();

    public void addParkingLotsList(ParkingLots parkingLotsList) {
        this.parkingLotsList.add(parkingLotsList);
    }
}
