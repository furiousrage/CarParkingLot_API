package com.bridgelabz.carparkinglot.user.repository;


import com.bridgelabz.carparkinglot.user.model.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {

    UserRegistration findByUserId(Long user_id);

    UserRegistration findByEmailId(String email_id);

}
