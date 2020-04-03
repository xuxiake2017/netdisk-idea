package group.xuxiake.web.websocket.chat;

import com.alibaba.fastjson.JSONObject;
import com.vdurmont.emoji.EmojiParser;
import group.xuxiake.entity.UserFriendMessage;
import group.xuxiake.entity.show.UserFriendMessageShow;
import group.xuxiake.mapper.UserFriendMessageMapper;
import group.xuxiake.util.MapEntityConvertUtils;
import group.xuxiake.web.websocket.chat.entity.ChatMessageBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author by xuxiake, Date on 2019/4/30.
 * PS: Not easy to write code, please indicate.
 * Description：
 */
@Slf4j
public class ChatWebSocketHandler extends AbstractWebSocketHandler {

    // websocket连接
    public static volatile Map<Integer, WebSocketSession> socketSessionMap = new ConcurrentHashMap();
    @Resource
    private UserFriendMessageMapper userFriendMessageMapper;

    // 握手实现连接后
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        Map<String, Object> attributes = session.getAttributes();
        Integer userId = (Integer) attributes.get("userId");
        log.debug("有新的webSocket连接，userId为：{}", userId);
        socketSessionMap.put(userId, session);
    }

    // 处理屏幕发来的文字消息
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        String payload = message.getPayload();
        // 可以在session中储存一些自定义属性
        Map<String, Object> attributes = session.getAttributes();
        Integer userId = (Integer) attributes.get("userId");
        log.debug("收到消息：{}", payload);
        ChatMessageBase chatMessageBase = JSONObject.parseObject(payload, ChatMessageBase.class);
        switch (chatMessageBase.getType()) {
            case "FRIEND":
                Map content = (Map) chatMessageBase.getContent();
                UserFriendMessageShow userFriendMessage = null;
                try {
                    userFriendMessage = (UserFriendMessageShow) MapEntityConvertUtils.convertMap(UserFriendMessageShow.class, content);
                } catch (IntrospectionException e) {
                    log.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                } catch (InstantiationException e) {
                    log.error(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    log.error(e.getMessage(), e);
                }
                Integer to = userFriendMessage.getTo();
                if (to != null) {
                    sendMessage(to, JSONObject.toJSONString(chatMessageBase));
                }
                // 将emoji转码能到数据库存储
                if (!StringUtils.isAnyEmpty(userFriendMessage.getContent())) {
                    userFriendMessage.setContent(EmojiParser.parseToAliases(userFriendMessage.getContent()));
                }
                // 保存聊天记录
                UserFriendMessage userFriendMessage_ = new UserFriendMessage();
                userFriendMessage_.setContent(userFriendMessage.getContent());
                userFriendMessage_.setFrom(userFriendMessage.getFrom());
                userFriendMessage_.setTo(userFriendMessage.getTo());
                userFriendMessage_.setFileId(userFriendMessage.getFileId());
                userFriendMessageMapper.insertSelective(userFriendMessage_);
                break;
            case "GROUP":
                break;
            case "FRIEND_APPLY_FOR":
                break;
        }
    }

    /**
     * 发送消息
     * @param userId 用户id
     * @param message 对象型消息
     * @return
     */
    public static void sendMessage(Integer userId, String message) {

        if (userId == null || StringUtils.isAnyEmpty(message)) {
            return;
        }
        WebSocketSession session = socketSessionMap.get(userId);
        if (session != null) {
            try {
                log.debug("发送消息：{}", message);
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    // 在连接关闭之后
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Object userId = session.getAttributes().get("userId");
        log.debug("websocket连接已断开，userId为：{}", userId);
        if (socketSessionMap.get(userId) != null) {
            socketSessionMap.remove(userId);
        }
    }
}
