package cn.com.infosec.mywebsocket.config;

import java.security.Principal;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class MyChannelInterceptor implements ChannelInterceptor {

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
		StompCommand command = sha.getCommand();
		Principal principal = sha.getUser();

		if (command != null) {
			System.out.println("preSend: " + command.toString());
		}
		if (principal != null) {
			System.out.println("preSend: " + principal.getName());
		}

		if (StompCommand.CONNECT.equals(command)) {

			System.out.println("preSend: " + sha.getLogin());
			System.out.println("preSend: " + sha.getPasscode());
			System.out.println("preSend: " + sha.getSessionId());
		}

		System.out.println("preSend");
		return message;
	}

}
