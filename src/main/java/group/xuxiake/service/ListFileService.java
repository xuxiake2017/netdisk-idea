package group.xuxiake.service;

import group.xuxiake.entity.Page;
import group.xuxiake.util.Result;

public interface ListFileService {

    /**
     * 查找所有文件
     * @param page
     * @return
     */
    Result listFile(Page page);

    //查找所有文档
    Result getDocumentList(Page page);

    //查找所有视频
    Result getVideoList(Page page);

    //查找所有图片
    Result getPicList(Page page);

    //查找所有音乐
    Result getAudioList(Page page);
}
