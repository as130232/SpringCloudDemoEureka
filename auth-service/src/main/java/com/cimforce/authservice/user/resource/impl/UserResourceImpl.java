package com.cimforce.authservice.user.resource.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cimforce.authservice.user.model.dto.UserDTO;
import com.cimforce.authservice.user.model.entity.SysCtl001Entity;
import com.cimforce.authservice.user.repository.SysCtl001Repository;
import com.cimforce.authservice.user.resource.UserResource;

@Service
public class UserResourceImpl implements UserResource{
	@Autowired
	private SysCtl001Repository sysCtl001Repository;
	
	@Override
	public UserDTO getUser(String username) {
		SysCtl001Entity sysCtl001Entity = sysCtl001Repository.findByUsername(username);
		if(sysCtl001Entity != null) {
			UserDTO userDto = UserDTO.from(sysCtl001Entity);
			return userDto;
		}
		return null;
	}

}
