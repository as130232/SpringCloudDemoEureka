package com.cimforce.apigateway.model.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

	private Integer userId;
	private String username;
	private String password;
	
	
}
