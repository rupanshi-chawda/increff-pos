package com.increff.pos.model.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
//this allows the infodata instance will be unique per user session, and any changes made to it during a session will
// be available to the same user during subsequent requests within the same session.

public class InfoData implements Serializable { // This allows the object to be serialized and deserialized, which is necessary for storing the object in a user session.

	private static final long serialVersionUID = 1L;

	private String message = "";
	private String email = "No email";
	private String role = "operator";

}
