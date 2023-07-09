package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	  
	  @Bean 
	  public UserDetailsService getUserDetailService() { 
		  return new UserDetailsServiceImpl(); }
	  
	  @Bean 
	  public BCryptPasswordEncoder passwordEncoder() {
		  return new BCryptPasswordEncoder(); }
	  
	  @Bean 
	  public AuthenticationProvider authenticationProvider() {
	  DaoAuthenticationProvider authenticationProvider = new
	  DaoAuthenticationProvider();
	  authenticationProvider.setUserDetailsService(this.getUserDetailService());
	  authenticationProvider.setPasswordEncoder(passwordEncoder()); 
	  return authenticationProvider; 
	  }
	  
	  
		/*
		 * @Bean public UserDetailsService userDetailsService(PasswordEncoder encoder) {
		 * UserDetails admin =
		 * User.withUsername("Rakhi").password(encoder.encode("Test")).roles("ADMIN").
		 * build();
		 * 
		 * UserDetails developer =
		 * User.withUsername("Sangam").password(encoder.encode("Test")).roles("USER").
		 * build(); return new InMemoryUserDetailsManager(admin, developer); }
		 */
	
		/*
		 * @Bean public SecurityFilterChain securityFilerChain(HttpSecurity http) { try
		 * { System.out.println("Inside the filter page"); return
		 * http.csrf().disable().authorizeHttpRequests().requestMatchers("/**").
		 * permitAll().and()
		 * .authorizeHttpRequests().requestMatchers("/user/**").authenticated().and().
		 * formLogin() .loginPage("/signin").and().build();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } return null; }
		 */
		
		
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers("/home/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin().loginPage("/home/signin")
			.defaultSuccessUrl("/user/index");
			//.loginProcessingUrl("/dologin")
			//.defaultSuccessUrl("/user/index")
			//.failureUrl("/login-fail");
			return http.build(); 
		}
		 
	  
	  
	  
}
