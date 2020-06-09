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
public class ParkingLots {


    @Id
    private long parkingLotId;

    private long slotCapacity;

    private long availableCapacity;

    public ParkingLots(long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @ManyToOne
    private ParkingLotSystem parkingLotSystem;

    @OneToMany(mappedBy = "parkingLots")
    private List<ParkedVehicle> parkedVehicleList = new ArrayList<>();

    public void addParkedVehicle(ParkedVehicle parkedVehicle) {
        this.parkedVehicleList.add(parkedVehicle);
    }

}
