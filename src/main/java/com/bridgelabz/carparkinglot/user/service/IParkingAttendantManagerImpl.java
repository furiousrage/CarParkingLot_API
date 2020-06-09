package com.bridgelabz.carparkinglot.user.service;

import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.ParkVehicleDTO;
import com.bridgelabz.carparkinglot.user.model.ParkedVehicle;
import com.bridgelabz.carparkinglot.user.model.ParkingLots;
import com.bridgelabz.carparkinglot.user.repository.ParkedVehicleRepository;
import com.bridgelabz.carparkinglot.user.repository.ParkingLotsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IParkingAttendantManagerImpl implements IParkingAttendantManager {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ParkingLotsRepository parkingLotsRepository;

    @Autowired
    private ParkedVehicleRepository parkedVehicleRepository;

    @Override
    public Response parkVehicle(ParkVehicleDTO parkVehicleDTO) {
        ParkedVehicle parkedVehicle = modelMapper.map(parkVehicleDTO, ParkedVehicle.class);
        long parkingLotsId = findParkingLot();
        ParkingLots parkingLot = parkingLotsRepository.findByParkingLotId(parkingLotsId);
        long slot = findParkingSlot(parkingLot);
        if (parkingLot.getAvailableCapacity() > 0 && checkVehicleAlreadyAvailable(parkedVehicle.getVehicleNumber())) {
            parkedVehicle.setSlotNumber(slot);
            parkedVehicle.setParkingLots(parkingLot);
            parkingLot.setAvailableCapacity(parkingLot.getAvailableCapacity() - 1);
            parkedVehicleRepository.save(parkedVehicle);
            parkingLot.addParkedVehicle(parkedVehicle);
            parkingLotsRepository.save(parkingLot);
            return new Response("Vehicle Parked", 202);
        } else if (!checkVehicleAlreadyAvailable(parkedVehicle.getVehicleNumber()))
            return new Response("Vehicle already parked", 202);
        return new Response("ParkingLot is Full ", 202);

    }

    @Override
    public Response unParkVehicle(String vehicleNumber) {
        if (!checkVehicleAlreadyAvailable(vehicleNumber)) {
            ParkedVehicle parkedVehicle = parkedVehicleRepository.findByVehicleNumber(vehicleNumber);
            parkedVehicle.getParkingLots().setAvailableCapacity(parkedVehicle.getParkingLots().getAvailableCapacity() + 1);
            parkedVehicle.getParkingLots().getParkedVehicleList().remove(parkedVehicle);
            parkedVehicleRepository.delete(parkedVehicle);
            return new Response("Vehicle unParked Successfully", 202);
        }
        return new Response(vehicleNumber + ": Vehicle is not parked in these ParkingLot", 400);
    }

    private long findParkingSlot(ParkingLots parkingLots) {
        long allotedSlot = 1L;
        List<ParkedVehicle> parkedVehicleList = parkingLots.getParkedVehicleList();
        List<Long> slotList = parkedVehicleList.stream().map(ParkedVehicle::getSlotNumber).sorted().collect(Collectors.toList());
        for (Long slot : slotList) {
            if (allotedSlot < slot)
                return allotedSlot;
            allotedSlot += 1L;
        }
        return allotedSlot;
    }

    private long findParkingLot() {
        List<ParkingLots> lotsList = parkingLotsRepository.findAll();
        long min = Integer.MIN_VALUE;
        long index = 0;
        for (ParkingLots parkingLots : lotsList) {
            if (min < parkingLots.getAvailableCapacity()) {
                min = parkingLots.getAvailableCapacity();
                index = parkingLots.getParkingLotId();
            }
        }
        return index;
    }

    private boolean checkVehicleAlreadyAvailable(String vehicleNumber) {
        return !parkedVehicleRepository.existsByVehicleNumber(vehicleNumber);
    }
}
