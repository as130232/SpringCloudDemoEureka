package com.cimforce.authservice.user.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SysCtl001Entity.java auto generated by Charles. 2018/5/11 下午 03:59:10
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "SYS_CTL001")
public class SysCtl001Entity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	@Column(name = "USER_ID")
	private Integer userId;
	@Column(name = "USERNAME")
	private String username;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "USER_STATUS")
	private Integer userStatus;

	@Column(name = "CREATEID")
	private String createid;
	@Column(name = "CREATEDATE")
	private Date createdate;
	@Column(name = "UPDATEID")
	private String updateid;
	@Column(name = "UPDATEDATE")
	private Date updatedate;
}
