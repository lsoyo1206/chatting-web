package com.example.chattingweb.chat;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class ChatHandler extends TextWebSocketHandler {

    // websocket 세션을 저장하기 위한 map
    private Map<String, WebSocketHandler> sessions = new HashMap<>();
    
    public void afterConnectionEstablished(WebSocketHandler session) throws Exception{
        // 클라이언트가 연결되었을 떄 실행되는 메소드 -> 새로 연결된 클라이언트 map에 저장
    }
}
