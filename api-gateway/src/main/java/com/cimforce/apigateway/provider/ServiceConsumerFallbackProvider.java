package com.cimforce.apigateway.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.cimforce.apigateway.filter.AccessFilter;

/**
 * 若網關後面的微服務掛了(後端服務沒有啟動或者調用超時)，zuul允許定義fallback類別，用於熔斷處理
 * @author charles.chen
 * @date 2018年4月12日 上午11:27:04
 */
@Component
public class ServiceConsumerFallbackProvider implements FallbackProvider{

	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);
	
	@Override
	public String getRoute() {
		//指定要處理的 service，api服務id，如果需要所有調用都支持回退，則返回“*”或返回null
		return "*";
	}
	
	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		if (cause != null && cause.getCause() != null) {
            String reason = cause.getCause().getMessage();
            log.info("Excption {}",reason);
        }
        return fallbackResponse();
	}

	public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("The service is unavailable.".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
