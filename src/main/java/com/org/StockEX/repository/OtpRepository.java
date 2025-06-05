package com.org.StockEX.repository;

import com.org.StockEX.Entity.OtpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpRequest, Long> {
    Optional<OtpRequest> findByEmail(String email);
}

