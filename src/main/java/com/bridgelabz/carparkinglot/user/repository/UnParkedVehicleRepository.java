package com.bridgelabz.carparkinglot.user.repository;

import com.bridgelabz.carparkinglot.user.model.UnParkedVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnParkedVehicleRepository extends JpaRepository<UnParkedVehicle, Long> {
}
