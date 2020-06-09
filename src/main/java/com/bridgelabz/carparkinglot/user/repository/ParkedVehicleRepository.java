package com.bridgelabz.carparkinglot.user.repository;


import com.bridgelabz.carparkinglot.user.model.ParkedVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkedVehicleRepository extends JpaRepository<ParkedVehicle, Long> {

    boolean existsByVehicleNumber(String vehicleNumber);

    ParkedVehicle findByVehicleNumber(String vehicleNumber);
}
