package com.bridgelabz.carparkinglot.user.repository;

import com.bridgelabz.carparkinglot.user.model.ParkingLots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotsRepository extends JpaRepository<ParkingLots, Long> {

    ParkingLots findByParkingLotId(long id);
}
