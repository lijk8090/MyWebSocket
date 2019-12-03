package cn.com.infosec.mywebsocket.config;

import java.security.Principal;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {

	@EventListener
	public void onSessionConnected(SessionConnectEvent event) {

		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		StompCommand command = sha.getCommand();
		Principal principal = sha.getUser();

		if (command != null) {
			System.out.println("onSessionConnected: " + command.toString());
		}
		if (principal != null) {
			System.out.println("onSessionConnected: " + principal.getName());
		}

		System.out.println("onSessionConnected: " + sha.getLogin());
		System.out.println("onSessionConnected: " + sha.getPasscode());
		System.out.println("onSessionConnected: " + sha.getSessionId());
	}

	@EventListener
	public void onSessionSubscribe(SessionSubscribeEvent event) {

		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		StompCommand command = sha.getCommand();
		Principal principal = sha.getUser();

		if (command != null) {
			System.out.println("onSessionSubscribe: " + command.toString());
		}
		if (principal != null) {
			System.out.println("onSessionSubscribe: " + principal.getName());
		}

		System.out.println("onSessionSubscribe: " + sha.getLogin());
		System.out.println("onSessionSubscribe: " + sha.getPasscode());
		System.out.println("onSessionSubscribe: " + sha.getSessionId());
	}

	@EventListener
	public void onSessionDisConnected(SessionDisconnectEvent event) {

		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		StompCommand command = sha.getCommand();
		Principal principal = sha.getUser();

		if (command != null) {
			System.out.println("onSessionDisConnected: " + command.toString());
		}
		if (principal != null) {
			System.out.println("onSessionDisConnected: " + principal.getName());
		}

		System.out.println("onSessionDisConnected: " + sha.getLogin());
		System.out.println("onSessionDisConnected: " + sha.getPasscode());
		System.out.println("onSessionDisConnected: " + sha.getSessionId());
	}

}
