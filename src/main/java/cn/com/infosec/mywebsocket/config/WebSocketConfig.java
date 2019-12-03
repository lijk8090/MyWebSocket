package cn.com.infosec.mywebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.enableSimpleBroker("/topic", "/queue"); // 服务端 -> 客户端
		registry.setUserDestinationPrefix("/user"); // 指定用户: 服务端 -> 客户端
		registry.setApplicationDestinationPrefixes("/app"); // 客户端 -> 服务端
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		StompWebSocketEndpointRegistration registration = registry.addEndpoint("/ws-endpoint");

		registration.addInterceptors(new MyHandshakeInterceptor());
		registration.setHandshakeHandler(new MyHandshakeHandler());
		registration.setAllowedOrigins("*");
		registration.withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {

		registration.interceptors(new MyChannelInterceptor());
	}

}
