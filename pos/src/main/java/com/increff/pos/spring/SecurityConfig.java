package com.increff.pos.spring;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	//todo: how does this security works behind the scenes?
	private static Logger logger = Logger.getLogger(SecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http//
				// Match only these URLs
				.requestMatchers()//
				.antMatchers("/api/**")//
				.antMatchers("/ui/**")//
				.and().authorizeRequests()//
				.antMatchers("/api/admin/**").hasAuthority("supervisor")//
				.antMatchers(HttpMethod.GET, "/api/brand").hasAnyAuthority("supervisor", "operator")//
				.antMatchers(HttpMethod.GET, "/api/brand/{id}").hasAnyAuthority("supervisor", "operator")//
				.antMatchers("/api/brand/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.GET, "/api/product/**").hasAnyAuthority("supervisor", "operator")//
				.antMatchers("/api/product/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.GET, "/api/inventory").hasAnyAuthority("supervisor", "operator")//
				.antMatchers(HttpMethod.GET, "/api/inventory/{barcode}").hasAnyAuthority("supervisor", "operator")//
				.antMatchers("/api/inventory/**").hasAnyAuthority("supervisor")//
				.antMatchers("/api/order/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/api/**").hasAnyAuthority("supervisor", "operator")//

				.antMatchers("/ui/admin/**").hasAuthority("supervisor")//
				.antMatchers("/ui/sales/**").hasAuthority("supervisor")//
				.antMatchers("/ui/sales-report/**").hasAuthority("supervisor")//
				.antMatchers("/ui/**").hasAnyAuthority("supervisor", "operator")//
				// Ignore CSRF and CORS
				.and().csrf().disable().cors().disable();
		logger.info("Configuration complete");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs",
				"/configuration/ui",
				"/swagger-resources",
				"/configuration/security",
				"/swagger-ui.html",
				"/webjars/**");
	}

}
