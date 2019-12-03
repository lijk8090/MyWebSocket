package cn.com.infosec.mywebsocket.config;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class MyHandshakeHandler extends DefaultHandshakeHandler {

	private HttpServletRequest getHttpServletRequest(ServerHttpRequest request) {

		if (request instanceof ServletServerHttpRequest) {

			ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
			return serverRequest.getServletRequest();
		}

		return null;
	}

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {

		HttpServletRequest servletRequest = this.getHttpServletRequest(request);
		String uuid = servletRequest.getParameter("uuid");

		System.out.println("determineUser: " + uuid);
		return new MyPrincipal(uuid);
	}

}
