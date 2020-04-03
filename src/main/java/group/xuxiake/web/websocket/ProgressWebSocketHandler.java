package group.xuxiake.web.websocket;

import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class ProgressWebSocketHandler extends TextWebSocketHandler {

    public static final Map<String, WebSocketSession> userWebSocketSessionMap;

    static {
        userWebSocketSessionMap = new HashMap<>();
    }

    // 握手实现连接后
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        log.debug("webSocket已连接");
        String sessionId = webSocketSession.getAttributes().get("sessionId").toString();
        if (userWebSocketSessionMap.get(sessionId) == null) {
            userWebSocketSessionMap.put(sessionId, webSocketSession);
        }
    }

    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    /**
     * 在此刷新页面就相当于断开WebSocket连接,原本在静态变量userSocketSessionMap中的
     * WebSocketSession会变成关闭状态(close)，但是刷新后的第二次连接服务器创建的
     * 新WebSocketSession(open状态)又不会加入到userSocketSessionMap中,所以这样就无法发送消息
     * 因此应当在关闭连接这个切面增加去除userSocketSessionMap中当前处于close状态的WebSocketSession，
     * 让新创建的WebSocketSession(open状态)可以加入到userSocketSessionMap中
     *
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.debug("webSocket连接已关闭");
        String sessionId = webSocketSession.getAttributes().get("sessionId").toString();
        log.debug("WebSocket & 用户session[ID:" + sessionId + "] close connection");
        Iterator<Map.Entry<String, WebSocketSession>> iterator = userWebSocketSessionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            if (webSocketSession.getAttributes().get("sessionId").equals(entry.getValue().getAttributes().get("sessionId"))) {
                userWebSocketSessionMap.remove(sessionId);
                log.debug("WebSocket & in static map:[sessionId:" + webSocketSession.getAttributes().get("sessionId") + "] removed");
            }
        }
    }

    public boolean supportsPartialMessages() {
        return false;
    }

    //发送信息的实现
    public static void sendMessage(String sessionId, Object message) throws IOException {
        if (sessionId != null && ! "".equals(sessionId)) {
            WebSocketSession session = userWebSocketSessionMap.get(sessionId);
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(message)));
            }
        }
    }

    /**
     * 关闭连接
     * @param sessionId
     * @throws IOException
     */
    public static void close(String sessionId) throws IOException {
        if (sessionId != null && ! "".equals(sessionId)) {
            WebSocketSession session = userWebSocketSessionMap.get(sessionId);
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * 连接是否正常
     * @param sessionId
     * @return
     */
    public static boolean isAlive(String sessionId){
        if (sessionId != null && ! "".equals(sessionId)) {
            WebSocketSession session = userWebSocketSessionMap.get(sessionId);
            if (session != null && session.isOpen()) {
                return true;
            }
        }
        return false;
    }

}
