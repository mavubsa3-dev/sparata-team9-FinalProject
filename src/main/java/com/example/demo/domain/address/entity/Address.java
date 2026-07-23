package com.example.demo.domain.address.entity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "addresses")
public class Address extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(length = 30)
	private String alias;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String zipCode;

	@Column(name = "address1", nullable = false, length = 200)
	private String basicAddress;

	@Column(name = "address2", length = 200)
	private String detailAddress;

	@Builder
	public Address(User user, String alias, String name, String phoneNumber, String zipCode, String basicAddress, String detailAddress){
		this.user = user;
		this.alias = alias;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.zipCode = zipCode;
		this.basicAddress = basicAddress;
		this.detailAddress = detailAddress;
	}

	// 주문 할 때 주문자와 배송 받는 사람이 다를 경우 사용
	public void updateName(String name){
		this.name = name;
	}

	public void updateAlias(String alias){
		this.alias = alias;
	}

	public void updatePhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public void updateZipCode(String zipCode){
		this.zipCode = zipCode;
	}

	public void updateBasicAddress(String basicAddress){
		this.basicAddress = basicAddress;
	}

	public void updateDetailAddress(String detailAddress){
		this.detailAddress = detailAddress;
	}

}
