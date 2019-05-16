package com.cimforce.authservice.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * TokenProvider類提供了一種創建JWT（createToken）的方法
 * 和一種解析和驗證JWT並返回Authentication對象（getAuthentication）的方法。
 * @author charles.chen
 * @date 2018年1月25日 下午4:51:17
 */
@Component
public class TokenProvider {
	private static Logger log = LoggerFactory.getLogger(TokenProvider.class);
	
	private final static String AUTHORIZATION_HEADER = "Authorization";
	
	private final String secretKey;
	private final long tokenValidityInMilliseconds;
	private final long refreshExpInMilliseconds;
	private final UserDetailsService userService;

	//初始化時，JWTAppConfig會將application.properties jwt參數讀近來
	public TokenProvider(JwtAppConfig config, UserDetailsService userService) {
		this.secretKey = Base64.getEncoder()
				.encodeToString(config.getSecret().getBytes());
		this.tokenValidityInMilliseconds = 1000 * config.getTokenValidityInSeconds();
		this.refreshExpInMilliseconds = 1000 * config.getRefreshExpInSeconds();
		this.userService = userService;
	}

	/**
	 * JWT是由jjwt庫中的Jwts構建器創建的。對於簽名(jwt中的signature)，構建器需要知道算法（HMAC與SHA-512）
	 * 且需要一個密鑰secret，從application.yml文件中讀取此密鑰（密鑰必須是base64編碼的字符串），
	 * 注意：secret是保存在伺服器端的，jwt的簽發生成也是在伺服器端的，secret就是用來進行jwt的簽發和jwt的驗證，所以，它就是你服務端的私鑰，在任何場景都不應該流露出去。一旦客戶端得知這個secret, 那就意味著客戶端是可以自我簽發jwt了
	 * @author Charles
	 * @date 2017年9月16日 下午6:54:01
	 * @param username
	 * @return String(JWT)
	 */
	public String createToken(String username) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);
		return Jwts.builder()
				.setId(UUID.randomUUID().toString())
				//用戶名稱將被放置到具有setSubject的主題中
				.setSubject(username)
				//設置當前日期和時間
				.setIssuedAt(now)
				.signWith(SignatureAlgorithm.HS512, this.secretKey)
				//設置該密鑰secret有效時間。
				.setExpiration(validity)
				//最終調用compact（）構建並返回String型態的JWT。
				.compact();
	}

	/**
	 * 刷新token
	 * @author charles.chen
	 * @date 2018年3月26日 下午1:51:05
	 */
	public String refreshToken(String username) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + this.refreshExpInMilliseconds);
		return Jwts.builder()
				.setId(UUID.randomUUID().toString())
				//用戶名稱將被放置到具有setSubject的主題中
				.setSubject(username)
				//設置當前日期和時間
				.setIssuedAt(now)
				.signWith(SignatureAlgorithm.HS512, this.secretKey)
				//設置該密鑰secret有效時間。
				.setExpiration(validity)
				//最終調用compact（）構建並返回String型態的JWT。
				.compact();
	}
	
	/**
	 * 接收JWT並進行身分驗證解析，
	 * 當第一次登入驗證通過時會產生JWT回傳給客戶端，客戶端後續動作將會一併帶著此JWT進行驗證
	 * 每次都需要檢查與第一次創建的JWT來驗證signature的有效性
	 * 驗證成功後，從數據庫加載用戶(UserDetails)
	 * @author charles.chen
	 * @date 2017年9月16日 下午6:54:01
	 * @param token
	 * @return Authentication
	 */
	public Authentication getAuthentication(String token) {
		Claims claims = null;
		try {
			claims = Jwts.parser()
					.setSigningKey(this.secretKey)
					.parseClaimsJws(token)
					.getBody();
		} catch(ExpiredJwtException e) {
			//若令牌有效日期過期會無法解析
			log.info("用戶令牌已過期。");
			throw new ExpiredJwtException(null, e.getClaims(), "token expired for username: " + e.getClaims().getSubject() + ", message: " + e.getMessage());
		}
		String username = claims.getSubject();
		
//		String email = (String) claims.get("email");
//      List roleNames = (List) claims.get("roles");
//      Date expirationDate = claims.getExpiration();
        
		UserDetails userDetails = this.userService.loadUserByUsername(username);

		return new UsernamePasswordAuthenticationToken(userDetails, "",
				userDetails.getAuthorities());
	}
	
	public String resolveToken(HttpServletRequest request) {
		//取得請求中標頭的jwt(key:Authorization)
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
	
}