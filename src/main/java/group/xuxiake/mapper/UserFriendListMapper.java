package group.xuxiake.mapper;

import group.xuxiake.entity.UserFriendList;
import group.xuxiake.entity.show.UserFriendListShow;

import java.util.List;

public interface UserFriendListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFriendList record);

    int insertSelective(UserFriendList record);

    UserFriendList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFriendList record);

    int updateByPrimaryKey(UserFriendList record);

    /**
     * 获取好友列表
     * @param userId 用户id
     * @return
     */
    List<UserFriendListShow> getFriendList(Integer userId);
}