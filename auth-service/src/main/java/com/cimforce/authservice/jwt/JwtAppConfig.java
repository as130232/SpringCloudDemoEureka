package com.cimforce.authservice.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @ConfigurationProperties
 * 外部配置註釋。如果要綁定並驗證某些外部屬性（例如:application.properties文件），
 * 這裡讀取jwt屬性-密鑰(secret)與有效時間(tokenValidityInSeconds)等值
 * @author charles.chen
 * @date 2018年1月25日 下午4:51:50
 */
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtAppConfig {
	private String secret;

	private long tokenValidityInSeconds;
	private long refreshExpInSeconds;

}