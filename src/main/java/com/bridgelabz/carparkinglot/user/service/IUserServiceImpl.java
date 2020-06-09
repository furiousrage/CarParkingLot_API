package com.bridgelabz.carparkinglot.user.service;


import com.bridgelabz.carparkinglot.exception.LoginException;
import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.LoginDTO;
import com.bridgelabz.carparkinglot.user.dto.OwnerDTO;
import com.bridgelabz.carparkinglot.user.dto.ParkingLotsDTO;
import com.bridgelabz.carparkinglot.user.model.Owner;
import com.bridgelabz.carparkinglot.user.model.ParkingLotSystem;
import com.bridgelabz.carparkinglot.user.model.ParkingLots;
import com.bridgelabz.carparkinglot.user.repository.OwnerRepository;
import com.bridgelabz.carparkinglot.user.repository.ParkingLotSystemRepository;
import com.bridgelabz.carparkinglot.user.repository.ParkingLotsRepository;
import com.bridgelabz.carparkinglot.utility.JwtUtill;
import com.bridgelabz.carparkinglot.utility.RegistrationMailService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl implements IUserService {

    private Logger logger = LoggerFactory.getLogger(IUserServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtill jwtUtill;

    @Autowired
    private OwnerRepository userRepository;

    @Autowired
    private ParkingLotSystemRepository parkingLotSystemRepository;

    @Autowired
    private ParkingLotsRepository parkingLotsRepository;

    @Autowired
    private RegistrationMailService registrationService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String registration(OwnerDTO ownerDTO) throws LoginException {

        Owner owner = modelMapper.map(ownerDTO, Owner.class);
        Owner alreadyPresent = userRepository.findByEmailId(owner.getEmailId());
        if (alreadyPresent != null) {
            throw new LoginException("User Already Present Try with Other Email-Id");
        }
        String password = owner.getPassword();
        String encryptPwd = bCryptPasswordEncoder.encode(password);
        owner.setPassword(encryptPwd);
        userRepository.save(owner);
        try {
            registrationService.sendNotification(owner);
        } catch (MailException e) {
            logger.info("Error Sending Email" + e.getMessage());
        }
        return "Registration Successfully";
    }

    @Override
    public Response emailValidate(String token) {
        Long owner_Id = jwtUtill.decodeToken(token);
        Owner owner = userRepository.findByUserId(owner_Id);
        owner.setVerify(true);
        userRepository.save(owner);
        return new Response("Email Validated Successfully", 200);
    }

    @Override
    public Response loginValidation(LoginDTO loginDTO) throws LoginException {

        Owner presentUser = checkEmailId(loginDTO.getEmailId());
        if (!presentUser.isVerify()) {
            throw new LoginException("Please First Verify YourSelf");
        }
        boolean status = bCryptPasswordEncoder.matches(loginDTO.getPassword(), presentUser.getPassword());
        if (status) {
            String token = jwtUtill.createToken(presentUser.getUserId());
            return new Response("login Successfully", 200);
        } else
            return new Response("Password Incorrect : Unauthorized Access", 401);

    }

    @Override
    public Response createParkingLotSystem(String token, long noOfParkingSystem) {
        Long ownerId = jwtUtill.decodeToken(token);
        Owner owner = userRepository.findByUserId(ownerId);
        for (long i = 1; i <= noOfParkingSystem; i++) {
            ParkingLotSystem parkingLotSystemObject = new ParkingLotSystem(i);
            owner.createParkingLotSystem(parkingLotSystemObject);
            parkingLotSystemObject.setOwner(owner);
            parkingLotSystemRepository.save(parkingLotSystemObject);
        }

        return new Response("ParkingLotSystem Created Successfully", 200);
    }

    @Override
    public Response createParkingLot(String token, ParkingLotsDTO parkingLotsDTO) {
        Long ownerId = jwtUtill.decodeToken(token);
        Owner owner = userRepository.findByUserId(ownerId);
        ParkingLotSystem parkingLotSystem = owner.getParkingLotSystemList().get(0);
        long j = 1;
        for (int i = 0; i < parkingLotsDTO.getNoOfParkingLot(); i++) {
            ParkingLots parkingLotsObject = new ParkingLots(j);
            parkingLotsObject.setSlotCapacity(parkingLotsDTO.getSlotCapacity()[i]);
            parkingLotsObject.setAvailableCapacity(parkingLotsDTO.getSlotCapacity()[i]);
            parkingLotSystem.addParkingLotsList(parkingLotsObject);
            parkingLotsObject.setParkingLotSystem(parkingLotSystem);
            parkingLotsRepository.save(parkingLotsObject);
            j++;
        }
        return new Response("ParkingLots Created Successfully", 200);
    }

    private Owner checkEmailId(String emailId) throws LoginException {
        Owner userRepositoryByEmailId = userRepository.findByEmailId(emailId);
        if (userRepositoryByEmailId == null)
            throw new LoginException("Invalid Email_ID");
        return userRepositoryByEmailId;
    }
}