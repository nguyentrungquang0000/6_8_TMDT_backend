package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
