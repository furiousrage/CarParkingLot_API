package com.bridgelabz.carparkinglot.user.repository;


import com.bridgelabz.carparkinglot.user.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Owner findByUserId(Long user_id);

    Owner findByEmailId(String email_id);

}
