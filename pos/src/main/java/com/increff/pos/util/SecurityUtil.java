package com.increff.pos.util;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SecurityUtil {

	public static void createContext(HttpSession session) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}

	public static void setAuthentication(Authentication token) {
		SecurityContextHolder.getContext().setAuthentication(token);
	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static UserPrincipal getPrincipal() {
		Authentication token = getAuthentication();
		return Objects.isNull(token) ? null : (UserPrincipal) getAuthentication().getPrincipal();
	}

}
