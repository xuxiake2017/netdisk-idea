package group.xuxiake.controller;

import group.xuxiake.entity.Page;
import group.xuxiake.service.UserFriendMessageService;
import group.xuxiake.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: xuxiake
 * @create: 2019-05-01 14:44
 * @description:
 **/
@RestController
@RequestMapping("/friendMessage")
public class UserFriendMessageController {

    @Resource
    private UserFriendMessageService userFriendMessageService;

    /**
     * 获取好友消息列表
     * @return
     */
    @RequestMapping("/getFriendMessages")
    public Result getFriendMessages(Page page, Integer friendId) {

        if (friendId == null) {
            return userFriendMessageService.getFriendMessages();
        } else {
            return userFriendMessageService.getFriendMessages(page, friendId);
        }

    }
}
