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

	private static Logger logger = Logger.getLogger(SecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http//
				// Match only these URLs
				.requestMatchers()//
				.antMatchers("/api/**")//
				.antMatchers("/ui/**")//
				.and().authorizeRequests()//
				.antMatchers("/api/admin/**").hasAuthority("admin")//

				.antMatchers(HttpMethod.GET, "/api/brand/**").hasAnyAuthority("admin", "standard")//
				.antMatchers("/api/brand/**").hasAnyAuthority("admin")//
				.antMatchers(HttpMethod.GET, "/api/product/**").hasAnyAuthority("admin", "standard")//
				.antMatchers("/api/product/**").hasAnyAuthority("admin")//
				.antMatchers(HttpMethod.GET, "/api/inventory/**").hasAnyAuthority("admin", "standard")//
				.antMatchers("/api/inventory/**").hasAnyAuthority("admin")//
				.antMatchers(HttpMethod.GET, "/api/order/**").hasAnyAuthority("admin", "standard")//
				.antMatchers("/api/order/**").hasAnyAuthority("admin")//
				.antMatchers("/api/**").hasAnyAuthority("admin", "standard")//

				.antMatchers("/ui/admin/**").hasAuthority("admin")//
				.antMatchers("/ui/**").hasAnyAuthority("admin", "standard")//
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
