package com.example.demo.domain.notification.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.notification.entity.Notifications;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

	List<Notifications> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
