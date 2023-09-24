package com.securityService.courierRole.repository;

import com.securityService.courierRole.entity.PasswordRestToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordRestToken, Long> {

    PasswordRestToken findByToken(String token);

}
