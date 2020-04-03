package group.xuxiake.service;

import group.xuxiake.entity.Page;
import group.xuxiake.util.Result;

public interface UserFriendMessageService {

    /**
     *
     * @return
     */
    Result getFriendMessages();

    /**
     * 获取好友消息列表
     * @param friendId
     * @param page
     * @return
     */
    Result getFriendMessages(Page page, Integer friendId);
}
