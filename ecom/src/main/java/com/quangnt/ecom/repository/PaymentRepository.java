package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
