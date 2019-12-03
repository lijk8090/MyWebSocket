package cn.com.infosec.mywebsocket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebSocketService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private SimpUserRegistry registry;

	// 推送 -> 广播
	// 类似@SendTo
	public void broadcastMessage(String dest, Object payload) {

		logger.info("broadcastMessage");
		System.out.println(dest);

		template.convertAndSend(dest, payload);
	}

	// 推送 -> 单播
	// 类似@SendToUser
	public void unicastMessage(String user, String dest, Object payload) {

		logger.info("unicastMessage");
		System.out.println(registry.getUsers());

		SimpUser simpUser = registry.getUser(user);
		if (simpUser == null || simpUser.getName() == null) {
			logger.error("User was offline: " + user);
			return;
		}

		template.convertAndSendToUser(user, dest, payload);
	}

}
