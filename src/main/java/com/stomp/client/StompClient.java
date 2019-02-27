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

public class StompClient {

	private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

	private StompSession stompSession = null;

	public void addHeader(String headerName, String headerValue) {
		
		headers.add(headerName, headerValue);
		
	}

	public int connect(String url) {

		Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		List<Transport> transports = Collections.singletonList(webSocketTransport);

		SockJsClient sockJsClient = new SockJsClient(transports);
		sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

		ListenableFuture<StompSession> f = stompClient.connect(url, headers, new MyHandler(), "localhost", 8080);
		
		try {
			this.stompSession = f.get();
			return 1;
		} catch (InterruptedException e) {
			return -1;
		} catch (ExecutionException e) {
			return -1;
		}
		
	}

	public void subscribeGreetings(String topic) {

		this.stompSession.subscribe(topic, new StompFrameHandler() {

			public Type getPayloadType(StompHeaders stompHeaders) {
				return byte[].class;
			}

			public void handleFrame(StompHeaders stompHeaders, Object o) {
				System.out.println("Received greeting " + new String((byte[]) o));
			}

		});
		
	}

	public int sendMsg(String topic, String msg) {

		this.stompSession.send(topic, msg);

		return 1;

	}

	private class MyHandler extends StompSessionHandlerAdapter {

		public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {

		}

	}

	public void disconnect() {

		this.stompSession.disconnect();

	}

	public static void main(String[] args) throws Exception {

		/* example */
		StompClient helloClient = new StompClient();
		helloClient.connect("http://localhost:8080/chat");
		helloClient.subscribeGreetings("chatroom/156");
		helloClient.sendMsg("topic", "{ sender\":1,\"msg\":\" msg  \",\"msg_type\":\"m\"}");
		helloClient.disconnect();

	}

}
