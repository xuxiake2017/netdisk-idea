package group.xuxiake.mapper;

import group.xuxiake.entity.UserFriendMessage;
import group.xuxiake.entity.show.UserFriendMessageShow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFriendMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFriendMessage record);

    int insertSelective(UserFriendMessage record);

    UserFriendMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFriendMessage record);

    int updateByPrimaryKey(UserFriendMessage record);

    /**
     * 获取好友消息列表
     * @param userId 用户id
     * @param friendId 好友id
     * @return
     */
    List<UserFriendMessageShow> getFriendMessages(@Param("userId") Integer userId, @Param("friendId") Integer friendId, @Param("limit") Integer limit);
}