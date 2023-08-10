package org.hoongoin.interviewbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final IbAuthenticationEntryPoint authenticationEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/account/logout", "/account/me", "/account/reset-password").authenticated()
			.antMatchers("/account/**").permitAll()
			.antMatchers(HttpMethod.GET, "/scraps/{scrap-id}").permitAll()
			.antMatchers("/scraps/interview/{interview-id}").permitAll()
			.antMatchers("/scraps/**").authenticated()
			.antMatchers("/").permitAll()
			.antMatchers(HttpMethod.POST, "/interview").authenticated()
			.antMatchers(HttpMethod.PUT, "/interview/{interview_id}").authenticated()
			.antMatchers(HttpMethod.DELETE, "/interview/{interview_id}").authenticated()
			.antMatchers("/interview/{interview-id}/like").authenticated()
			.antMatchers("/temporary/**").authenticated()
			.and()
			.formLogin().disable().csrf().disable().cors()
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
