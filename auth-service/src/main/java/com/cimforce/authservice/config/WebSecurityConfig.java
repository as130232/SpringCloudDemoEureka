package com.cimforce.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.cimforce.authservice.jwt.JwtAuthenticationEntryPoint;
import com.cimforce.authservice.password.Password;
import com.cimforce.authservice.service.MyAuthenticationProvider;
import com.cimforce.authservice.service.MyUserDetailsService;

/**
 * @EnableWebSecurity啟用Web安全功能(即為Enable SpringSecurityFilterChain)
 * 設定網頁存取權限，http物件就是負責這部分，它是一個builder pattern，
 * 目前先呼叫anonymous()使任何人都可以存取網頁
 * @author charles.chen
 * @date 2018年1月25日 上午10:22:30
 */
@Configuration
@EnableWebSecurity
//開啟可設定該資源需要什麼權限角色(@PreAuthorize("")
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private MyAuthenticationProvider myAuthenticationProvider;
	@Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	/**
	 * 配置如何通過攔截器保護請求
	 * @author charles.chen
	 * @date 2018年1月25日 上午10:22:30
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			//使用spring security，若有跨域這裡需配置cors
			.cors()
			.and()
			//使用的是JWT，因此不需要csrf
			.csrf().disable()
			//當jwt驗證失敗時，作例外處理
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
			.and()
			//基於token，因此將session設置為STATELESS，防止Spring Security創建HttpSession對象
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			//.httpBasic() 	// this is optional, if you want to access 
			//.and()     	// the services from a browser
			//啟用默認的登錄頁面
			//.formLogin()
			.authorizeRequests()
		 	//可以訪問而不進行身份驗證
			.antMatchers("/login").permitAll()
			.antMatchers("/authenticationToken").permitAll()
			.antMatchers("/public/**").permitAll()
			.antMatchers("/signup").permitAll()
			//第三方登入(facebook、google)
			.antMatchers("/signin/**").permitAll()
			//其他端點將被保護並且需要有效的JWT秘鑰
			.anyRequest().authenticated()
			.and()
			.logout()
            .logoutUrl("/logout")
            .clearAuthentication(true)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
            //.addLogoutHandler(customLogoutHandler());
		
		//http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}


	/**
	 * 配置user-detail、身份驗證，用於注入自定義身分驗證和密碼校驗規則
	 * @author charles.chen
	 * @date 2018年1月25日 上午10:22:30
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			//加載授權信息
			.authenticationProvider(myAuthenticationProvider)
	        //加載用戶信息
	        .userDetailsService(myUserDetailsService)
	        //使用BCrypt進行密碼的hash
	        .passwordEncoder(Password.encoder);
		
		//自行定義創建使用者，用於測試
		auth.inMemoryAuthentication()
			.withUser("user").password("0000").roles("USER")
			.and()
			.withUser("admin").password("0000").roles("USER","ADMIN");
	}
	
	/**
	 * 為了在AuthService中讓AuthenticationManager注入
	 * 在此要設定將AuthenticationManager的Bean設為公開
	 * @author charles.chen
	 * @date 2018年3月23日 下午3:58:18
	 */
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
