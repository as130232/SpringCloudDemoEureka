package com.cimforce.apigateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cimforce.apigateway.model.dto.UserDTO;

@FeignClient("auth-service")
public interface AuthServiceAPI {

   // 驗證token是否合法
   @PostMapping("/authenticationToken")
   public Boolean authenticationToken(@RequestBody String jwt);

   // 取得用戶資訊
   @PostMapping("/public/user")
   public UserDTO getUser(@RequestBody String jwt);
}
