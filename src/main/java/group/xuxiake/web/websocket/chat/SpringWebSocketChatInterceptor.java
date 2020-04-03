package group.xuxiake.web.websocket.chat;

import lombok.extern.log4j.Log4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Author by xuxiake, Date on 2019/4/30.
 * PS: Not easy to write code, please indicate.
 * Description：
 */
@Log4j
public class SpringWebSocketChatInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * 在连接建立之前
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.debug("Before Handshake");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            if (session != null) {
                Integer userId = (Integer) session.getAttribute("userId");
                if (userId != null) {
//                    //确保一个用户一次只能建立一个连接
//                    if (ChatWebSocketHandler.socketSessionMap.get(userId) == null){
//                        attributes.put("userId", userId);
//                        return true;
//                    }
                    // 新连接取代旧连接(可重复连接)
                    attributes.put("userId", userId);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
