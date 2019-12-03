package cn.com.infosec.mywebsocket.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import cn.com.infosec.mywebsocket.entity.UserEntity;

@Controller
public class WebSocketController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/ws-server")
	@SendTo("/topic/ws-public")
	public Map<String, Object> handleMessage(UserEntity user) throws Exception {

		logger.info("handleMessage");
		Thread.sleep(1000);

		Map<String, Object> map = new TreeMap<String, Object>();

		map.put("uuid", user.getUuid());
		map.put("username", user.getUsername());
		map.put("password", user.getPassword());

		System.out.println(map);
		return map;
	}

	@MessageExceptionHandler
	@SendToUser("/queue/ws-errors")
	public String handleException(Throwable exception) {

		logger.warn(exception.getMessage());
		return exception.getMessage();
	}

	// 定时广播
	@Scheduled(fixedRate = 10000)
	public void scheduledMessage() {

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = dateFormat.format(date);

		logger.info(datetime);

		String dest = "/topic/ws-public";
		template.convertAndSend(dest, datetime);
	}

}
