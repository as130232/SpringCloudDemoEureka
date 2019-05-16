package com.cimforce.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableFeignClients
@EnableZuulProxy
@SpringCloudApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
	
	/**
	 * 處理微服務跨域請求設置，不需再所有微服務中加入，直接在zuul網關解決跨域問題
	 * 備註:簡單跨域就是GET，HEAD和POST請求，但是POST請求的"Content-Type"只能是application / x-www-form-urlencoded，multipart / form-data或text / plain
     * 反之，就是非簡單跨域，此跨域有一個預檢機制，說直白點，就是會發兩次請求，一次OPTIONS請求，一次真正的請求
	 * @author charles.chen
	 * @date 2018年4月16日 上午10:49:34
	 */
	@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);	//允許cookies跨域  
        config.addAllowedOrigin("*");		//允許向該服務器提交請求的URI，*表示全部允許，在用SpringMVC中，如果設成*，會自動轉成當前請求頭中的原產地  
        config.addAllowedHeader("*");		//允許訪問的頭信息，*表示全部
        config.setMaxAge(18000L);			//預檢請求的緩存時間（秒），即在這個時間段裡，對於相同的跨域請求不會再預檢了
        config.addAllowedMethod("OPTIONS");	//允許提交請求的方法，*表示全部允許
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
