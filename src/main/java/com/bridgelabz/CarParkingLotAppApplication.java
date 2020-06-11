package com.bridgelabz;

import com.bridgelabz.carparkinglot.user.repository.UserRegistrationRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRegistrationRepository.class)
public class CarParkingLotAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarParkingLotAppApplication.class, args);
    }

}
