package cn.com.infosec.mywebsocket.controller;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infosec.mywebsocket.service.WebSocketService;

@Controller
public class MenuListController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String USERNAME = "root";
	private final String PASSWORD = "11111111";

	@Autowired
	private WebSocketService websocket;

	@RequestMapping("/")
	public String indexController(ModelMap modelMap) {

		logger.info("index: /WEB-INF/jsp/index.jsp");
		return "index";
	}

	@RequestMapping("main.do")
	public String mainController(ModelMap modelMap) {

		logger.info("main: /WEB-INF/jsp/main.jsp");
		return "main";
	}

	@ResponseBody
	@RequestMapping("uuid.do")
	public Map<String, Object> qrcodeController(HttpServletResponse response, ModelMap modelMap) throws Exception {

		int min = 100000;
		int max = 999999;
		int uuid = (int) (min + Math.random() * (max - min + 1));

		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("uuid", uuid);

		System.out.println(map);
		return map;
	}

	@ResponseBody
	@RequestMapping("appLogin.do")
	public Map<String, Object> appLoginController(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password, @RequestParam(value = "uuid") String uuid,
			HttpSession session) throws Exception {

		logger.info("appLoginController: " + session.getId());
		Map<String, Object> map = new TreeMap<String, Object>();

		if (USERNAME.equals(username) == true && PASSWORD.equals(password)) {
			session.setAttribute("uuid", uuid);
			map.put("isLogin", "true");

			websocket.unicastMessage(uuid, "/queue/ws-client", map);

		} else {
			session.removeAttribute("uuid");
			map.put("isLogin", "false");

			websocket.broadcastMessage("/queue/ws-client/" + uuid, map);
		}

		System.out.println(map);
		return map;
	}

}
