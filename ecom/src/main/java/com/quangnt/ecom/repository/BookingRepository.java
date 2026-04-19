package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
