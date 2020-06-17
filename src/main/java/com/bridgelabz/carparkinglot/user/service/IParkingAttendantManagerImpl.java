package com.bridgelabz.carparkinglot.user.service;

import com.bridgelabz.carparkinglot.MailHandler.MessageSender;
import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.ParkVehicleDTO;
import com.bridgelabz.carparkinglot.user.model.ParkedVehicle;
import com.bridgelabz.carparkinglot.user.model.ParkingLots;
import com.bridgelabz.carparkinglot.user.model.UnParkedVehicle;
import com.bridgelabz.carparkinglot.user.model.UserRegistration;
import com.bridgelabz.carparkinglot.user.repository.ParkedVehicleRepository;
import com.bridgelabz.carparkinglot.user.repository.ParkingLotsRepository;
import com.bridgelabz.carparkinglot.user.repository.UnParkedVehicleRepository;
import com.bridgelabz.carparkinglot.user.repository.UserRegistrationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IParkingAttendantManagerImpl implements IParkingAttendantManager {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Autowired
    private UnParkedVehicleRepository unParkedVehicleRepository;

    @Autowired
    private ParkingLotsRepository parkingLotsRepository;

    @Autowired
    private ParkedVehicleRepository parkedVehicleRepository;

    @Autowired
    private MessageSender messageSender;

    @Override
    public Response parkVehicle(ParkVehicleDTO parkVehicleDTO) throws IOException {
        ParkedVehicle parkedVehicle = modelMapper.map(parkVehicleDTO, ParkedVehicle.class);
        long parkingLotsId = findParkingLot();
        ParkingLots parkingLot = parkingLotsRepository.findByParkingLotId(parkingLotsId);
        long slot = findParkingSlot(parkingLot);
        if (checkVehicleAlreadyAvailable(parkedVehicle.getVehicleNumber())) {
            parkedVehicle.setSlotNumber(slot);
            parkedVehicle.setParkingLots(parkingLot);
            parkingLot.setAvailableCapacity(parkingLot.getAvailableCapacity() - 1);
            if(parkingLot.getAvailableCapacity() == 0){
                UserRegistration owner = parkingLot.getParkingLotSystem().getOwner();
                messageSender.sendEmail(owner.getEmailId(),"Alert!!"+"\nParking lot Id: "+parkingLot.getParkingLotId());
            }
            parkedVehicleRepository.save(parkedVehicle);
            parkingLot.addParkedVehicle(parkedVehicle);
            parkingLotsRepository.save(parkingLot);
            return new Response("Vehicle Parked", 202);
        } else
            return new Response("Vehicle already parked", 202);
    }

    @Override
    public Response unParkVehicle(String vehicleNumber) {
        if (!checkVehicleAlreadyAvailable(vehicleNumber)) {
            ParkedVehicle parkedVehicle = parkedVehicleRepository.findByVehicleNumber(vehicleNumber);
            parkedVehicle.getParkingLots().setAvailableCapacity(parkedVehicle.getParkingLots().getAvailableCapacity() + 1);
            UnParkedVehicle vehicle = modelMapper.map(parkedVehicle,UnParkedVehicle.class);
            vehicle.setEndDate(LocalDate.now());
            vehicle.setEndTime(LocalTime.now());
            vehicle.setOwner(parkedVehicle.getParkingLots().getParkingLotSystem().getOwner());
            UserRegistration owner =parkedVehicle.getParkingLots().getParkingLotSystem().getOwner();
            owner.addUnParkedVehicleList(vehicle);
            unParkedVehicleRepository.save(vehicle);
            userRegistrationRepository.save(owner);
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
            if(parkingLots.getAvailableCapacity()==0)
                continue;
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
