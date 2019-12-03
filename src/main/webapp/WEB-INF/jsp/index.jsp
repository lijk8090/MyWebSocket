<%@ page language="java" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>MyWebSocket</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" type="text/css"
	href="${ctx}/css/bootstrap.min.css" />

<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/js/sockjs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/stomp.min.js"></script>

</head>

<body>
	<h1>MyWebSocket</h1>

	<div>
		<span>模拟WEB登陆</span>
		<div>
			<input type="button" id="webConnect" name=webConnect value="WEB建连" />
		</div>
		<div>
			<input type="button" id="webSendMsg" name=webSendMsg value="WEB发送信息" />
		</div>
		<div>
			<input type="button" id="webDisConnect" name=webDisConnect
				value="WEB断连" />
		</div>
	</div>

	<div>
		<span>模拟APP登陆</span>
		<div>
			<span>UUID:</span> <input type="text" id="uuid" name=uuid
				value="123456" style="display: block" />
		</div>
		<div>
			<span>账号:</span> <input type="text" id="username" name="username"
				value="root" />
		</div>
		<div>
			<span>密码:</span> <input type="text" id="password" name="password"
				value="11111111" />
		</div>
		<div>
			<input type="button" id="appLogin" name=appLogin value="APP登陆" />
		</div>
	</div>

	<script type="text/javascript">
		var stompClient = null;

		function onSucceed(frame) {
			console.log(frame);

			var uuid = $('#uuid').val();
			stompClient.subscribe('/queue/ws-client/' + uuid,
					function(message) {
						alert(message);
						console.log(JSON.parse(message.body));
						window.location.href = '/';
					});

			// 单播
			stompClient.subscribe('/user/queue/ws-client', function(message) {
				alert(message);
				console.log(JSON.parse(message.body));
				window.location.href = 'main.do';
			});

			// 广播
			stompClient.subscribe('/topic/ws-public', function(message) {
				console.log(message);
				console.log(message.body);
			});

			// 错误
			stompClient.subscribe('/queue/ws-errors', function(message) {
				alert(message);
				window.location.href = '/';
			});

			console.log("onSucceed");
		}

		function onFailed(errors) {

			alert(errors);
			console.log("onFailed");
		}

		function onConnect() {

			var uuid = $('#uuid').val();
			var endpoint = "http://localhost:4040/ws-endpoint?uuid=" + uuid;
			var socket = new SockJS(endpoint);

			var headers = {};
			headers['uuid'] = $('#uuid').val();
			headers['login'] = $('#username').val();
			headers['passcode'] = $('#password').val();

			stompClient = Stomp.over(socket);

			stompClient.heartbeat.outgoing = 10000;
			stompClient.heartbeat.incoming = 10000;

			stompClient.connect(headers, onSucceed, onFailed);

			console.log("onConnect");
		}

		function onDisConnect() {

			if (stompClient !== null) {
				stompClient.disconnect();
			}

			console.log("onDisConnect");
		}

		function sendMessage() {

			var data = {};
			data['uuid'] = $('#uuid').val();
			data['username'] = $('#username').val();
			data['password'] = $('#password').val();

			stompClient.send('/app/ws-server', {}, JSON.stringify(data));
			console.log("sendMessage");
		}

		$('#webConnect').click(function() {

			return onConnect();
		});

		$('#webSendMsg').click(function() {

			return sendMessage();
		});

		$('#webDisConnect').click(function() {

			return onDisConnect();
		});

		function getUuid() {
			$.ajaxSettings.async = false;
			$.getJSON("uuid.do", function(data, status, xhr) {
				console.log(data);
				$('#uuid').val(data.uuid)
			});
		}

		$(document).ready(function() {

			return getUuid();
		});

		$('#appLogin').click(function() {
			var data = {};
			data['uuid'] = $('#uuid').val();
			data['username'] = $('#username').val();
			data['password'] = $('#password').val();

			$.ajaxSettings.processData = true;
			$.post("appLogin.do", data, function(data, status, xhr) {
				console.log(data.isLogin);
				if (data.isLogin == 'true') {
					alert("APP登陆成功!");
					return true;
				} else {
					alert("APP登陆失败!");
					return false;
				}
			});
		});
	</script>
</body>
</html>
