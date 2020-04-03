package group.xuxiake.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import group.xuxiake.entity.Page;
import group.xuxiake.entity.UserNetdisk;
import group.xuxiake.entity.show.UserFriendListShow;
import group.xuxiake.entity.show.UserFriendMessageShow;
import group.xuxiake.mapper.UserFriendListMapper;
import group.xuxiake.mapper.UserFriendMessageMapper;
import group.xuxiake.service.UserFriendMessageService;
import group.xuxiake.util.Result;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author: xuxiake
 * @create: 2019-05-01 14:44
 * @description:
 **/
@Service("userFriendMessageService")
public class UserFriendMessageServiceImpl implements UserFriendMessageService {

    @Resource
    private UserFriendMessageMapper userFriendMessageMapper;
    @Resource
    private UserFriendListMapper userFriendListMapper;
    /**
     * 获取好友消息列表
     * @return
     */
    @Override
    public Result getFriendMessages() {

        Result result = new Result();
        UserNetdisk user = (UserNetdisk) SecurityUtils.getSubject().getPrincipal();

        List<UserFriendListShow> friendList = userFriendListMapper.getFriendList(user.getId());
        List<UserFriendMessageShow> friendMessages = new ArrayList<>();
        for (UserFriendListShow item : friendList) {

            List<UserFriendMessageShow> list = userFriendMessageMapper.getFriendMessages(user.getId(), item.getFriendId(), 100);
            friendMessages.addAll(list);
        }
        friendMessages.sort(new Comparator<UserFriendMessageShow>() {
            @Override
            public int compare(UserFriendMessageShow o1, UserFriendMessageShow o2) {
                return o1.getId() - o2.getId();
            }
        });

        result.setData(friendMessages);
        return result;
    }

    /**
     * 获取好友消息列表
     * @param friendId
     * @param page
     * @return
     */
    @Override
    public Result getFriendMessages(Page page, Integer friendId) {

        Result result = new Result();
        UserNetdisk user = (UserNetdisk) SecurityUtils.getSubject().getPrincipal();
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<UserFriendMessageShow> list = userFriendMessageMapper.getFriendMessages(user.getId(), friendId, null);
        list.sort(new Comparator<UserFriendMessageShow>() {
            @Override
            public int compare(UserFriendMessageShow o1, UserFriendMessageShow o2) {
                return o1.getId() - o2.getId();
            }
        });
        PageInfo<UserFriendMessageShow> pageInfo = new PageInfo<>(list);
        result.setData(pageInfo);
        return result;
    }
}
