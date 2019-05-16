package com.cimforce.authservice.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cimforce.authservice.user.model.entity.SysCtl001Entity;

@Repository
public interface SysCtl001Repository extends JpaRepository<SysCtl001Entity, Integer>, JpaSpecificationExecutor<SysCtl001Entity>{

	public SysCtl001Entity findByUsername(String username);
}

