package cn.com.infosec.mywebsocket.config;

import java.security.Principal;

class MyPrincipal implements Principal {

	private String name;

	public MyPrincipal(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
