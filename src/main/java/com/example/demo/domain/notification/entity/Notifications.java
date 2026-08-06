package com.example.demo.domain.notification.entity;

import com.example.demo.common.entity.BaseTimeEntity;
import com.example.demo.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
public class Notifications extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String title;

	private String message;

	@Column(name = "is_read", nullable = false)
	private boolean isRead;

	public Notifications(User user, String title, String message){
		this.user = user;
		this.title = title;
		this.message = message;
		this.isRead = false;
	}

	public void markAsRead(){
		this.isRead = true;
	}


}
