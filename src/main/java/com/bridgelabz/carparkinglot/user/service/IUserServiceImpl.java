package com.bridgelabz.carparkinglot.user.service;


import com.bridgelabz.carparkinglot.exception.LoginException;
import com.bridgelabz.carparkinglot.response.Response;
import com.bridgelabz.carparkinglot.user.dto.LoginDTO;
import com.bridgelabz.carparkinglot.user.dto.UserRegistrationDTO;
import com.bridgelabz.carparkinglot.user.dto.ParkingLotSystemDTO;
import com.bridgelabz.carparkinglot.user.model.UserRegistration;
import com.bridgelabz.carparkinglot.user.model.ParkingLotSystem;
import com.bridgelabz.carparkinglot.user.model.ParkingLots;
import com.bridgelabz.carparkinglot.user.repository.UserRegistrationRepository;
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
    private UserRegistrationRepository userRepository;

    @Autowired
    private ParkingLotSystemRepository parkingLotSystemRepository;

    @Autowired
    private ParkingLotsRepository parkingLotsRepository;

    @Autowired
    private RegistrationMailService registrationService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String registration(UserRegistrationDTO userRegistrationDTO) throws LoginException {

        UserRegistration userRegistration = modelMapper.map(userRegistrationDTO, UserRegistration.class);
        UserRegistration alreadyPresent = userRepository.findByEmailId(userRegistration.getEmailId());
        if (alreadyPresent != null) {
            throw new LoginException("User Already Present Try with Other Email-Id");
        }
        String password = userRegistration.getPassword();
        String encryptPwd = bCryptPasswordEncoder.encode(password);
        userRegistration.setPassword(encryptPwd);
        userRepository.save(userRegistration);
        try {
            registrationService.sendNotification(userRegistration);
        } catch (MailException e) {
            logger.info("Error Sending Email" + e.getMessage());
        }
        return "Registration Successfully";
    }

    @Override
    public Response emailValidate(String token) {
        Long owner_Id = jwtUtill.decodeToken(token);
        UserRegistration userRegistration = userRepository.findByUserId(owner_Id);
        userRegistration.setVerify(true);
        userRepository.save(userRegistration);
        return new Response("Email Validated Successfully", 200);
    }

    @Override
    public Response loginValidation(LoginDTO loginDTO) throws LoginException {

        UserRegistration presentUser = checkEmailId(loginDTO.getEmailId());
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
    public Response createParkingLotSystem(ParkingLotSystemDTO parkingLotSystemDTO) {
        String emailId = parkingLotSystemDTO.getLoginDTO().getEmailId();
        UserRegistration userRegistration = userRepository.findByEmailId(emailId);
        boolean actor = userRegistration.getActor().equalsIgnoreCase("OWNER");
        boolean status = bCryptPasswordEncoder.matches(parkingLotSystemDTO.getLoginDTO().getPassword(), userRegistration.getPassword());
        if(status && actor) {
            for (long i = 1; i <= parkingLotSystemDTO.getNoOfParkingLotSystem(); i++) {
                ParkingLotSystem parkingLotSystemObject = new ParkingLotSystem(i);
                userRegistration.createParkingLotSystem(parkingLotSystemObject);
                parkingLotSystemObject.setOwner(userRegistration);
                parkingLotSystemRepository.save(parkingLotSystemObject);
            }
            return createParkingLot(parkingLotSystemDTO, userRegistration);
        }
            return new Response("Password Incorrect : Unauthorized Access", 401);
    }

    private Response createParkingLot(ParkingLotSystemDTO parkingLotSystemDTO, UserRegistration userRegistration) {

        ParkingLotSystem parkingLotSystem = userRegistration.getParkingLotSystemList().get(0);
        long j = 1;
        for (int i = 0; i < parkingLotSystemDTO.getNoOfParkingLot(); i++) {
            ParkingLots parkingLotsObject = new ParkingLots(j);
            parkingLotsObject.setSlotCapacity(parkingLotSystemDTO.getSlotCapacity()[i]);
            parkingLotsObject.setAvailableCapacity(parkingLotSystemDTO.getSlotCapacity()[i]);
            parkingLotSystem.addParkingLotsList(parkingLotsObject);
            parkingLotsObject.setParkingLotSystem(parkingLotSystem);
            parkingLotsRepository.save(parkingLotsObject);
            j++;
        }
        return new Response("ParkingLotSystem Created Successfully", 200);
    }

    private UserRegistration checkEmailId(String emailId) throws LoginException {
        UserRegistration userRepositoryByEmailId = userRepository.findByEmailId(emailId);
        if (userRepositoryByEmailId == null)
            throw new LoginException("Invalid Email_ID");
        return userRepositoryByEmailId;
    }
}