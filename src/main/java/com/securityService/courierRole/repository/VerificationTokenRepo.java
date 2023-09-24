package com.securityService.courierRole.repository;

import com.securityService.courierRole.security.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {


    VerificationToken findByToken(String token);

}
