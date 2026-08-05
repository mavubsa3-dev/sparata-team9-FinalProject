package com.example.demo.domain.notification.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.notification.entity.Notifications;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

	Optional<Notifications> findByIdAndUserId(Long notificationId, Long userId);

	List<Notifications> findAllByUserIdOrderByCreatedAtDesc(Long userId);

	List<Notifications> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
}
