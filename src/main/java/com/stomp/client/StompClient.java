package com.stomp.client;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

/**
 * Created by nick on 30/09/2015.
 */
public class StompClient {

	private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

	private StompSession stompSession = null;

	public int connect(String url) throws InterruptedException, ExecutionException {

		Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		List<Transport> transports = Collections.singletonList(webSocketTransport);

		SockJsClient sockJsClient = new SockJsClient(transports);
		sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

		ListenableFuture<StompSession> f = stompClient.connect(url, headers, new MyHandler(), "localhost", 8080);
		this.stompSession = f.get();
		return 1;

	}

	public void subscribeGreetings(String topic) throws ExecutionException, InterruptedException {
		this.stompSession.subscribe(topic, new StompFrameHandler() {

			public Type getPayloadType(StompHeaders stompHeaders) {
				return byte[].class;
			}

			public void handleFrame(StompHeaders stompHeaders, Object o) {
				// logger.info("Received greeting " + new String((byte[]) o));
			}
		});
	}

	public int sendMsg(String msg, String chatroom_idx) {
		String sendMsg = "{" + "\"sender\":1,\"msg\":\"" + msg + "\",\"msg_type\":\"m\"}";
		this.stompSession.send("/chatroom/" + chatroom_idx, sendMsg.getBytes());
		return 1;
	}

	private class MyHandler extends StompSessionHandlerAdapter {
		public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
			// logger.info("Now connected");
		}
	}

	public void disconnect() {
		this.stompSession.disconnect();
	}

	public static void main(String[] args) throws Exception {
		StompClient helloClient = new StompClient();
		helloClient.connect("http://192.168.0.6:8080/gs-guide-websocket");

		// logger.info("Subscribing to greeting topic using session " +
		// stompSession);
		// helloClient.subscribeGreetings(stompSession);

		// logger.info("Sending hello message" + stompSession);
		helloClient.sendMsg("msh", "156");
		helloClient.disconnect();
	}

}
