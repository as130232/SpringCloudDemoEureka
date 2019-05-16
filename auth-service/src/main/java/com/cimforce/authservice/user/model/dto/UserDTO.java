package com.cimforce.authservice.user.model.dto;


import com.cimforce.authservice.user.model.entity.SysCtl001Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

	private Integer userId;
	private String username;
	private String password;
	
	public static UserDTO from(SysCtl001Entity s) {
		UserDTO userDto = new UserDTO();
		userDto.setUserId(s.getUserId());
		userDto.setUsername(s.getUsername());
		userDto.setPassword(s.getPassword());
		return userDto;
	}
	
}
