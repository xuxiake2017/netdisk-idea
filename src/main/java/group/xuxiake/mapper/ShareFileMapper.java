package group.xuxiake.mapper;

import group.xuxiake.entity.ShareFile;
import group.xuxiake.entity.show.ShareFileShowInfo;
import group.xuxiake.entity.show.ShareFileShowList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShareFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShareFile record);

    int insertSelective(ShareFile record);

    ShareFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShareFile record);

    int updateByPrimaryKey(ShareFile record);

    /**
     * 获取分享文件信息
     * @param shareId
     * @return
     */
    ShareFileShowInfo getShareFileInfo(String shareId);

    /**
     * 根据shareId查找文件
     * @param shareId
     * @return
     */
    ShareFile findByShareId(String shareId);

    /**
     * 查找用户分享文件列表
     * @param userId
     * @return
     */
    List<ShareFileShowList> findAllByUserId(@Param("userId") Integer userId, @Param("fileName") String fileName);
}