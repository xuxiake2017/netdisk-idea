package group.xuxiake.handler;

import group.xuxiake.entity.FileUpload;
import group.xuxiake.mapper.FileUploadMapper;
import group.xuxiake.util.ConvertVideoUtil;
import group.xuxiake.util.FastDFSUtils;

import java.io.File;

/**
 * Author by xuxiake, Date on 2019/2/27.
 * PS: Not easy to write code, please indicate.
 * Description：开辟线程进行音频的转码
 */
public class LowKbpsHandler implements Runnable {

    private FileUploadMapper fileUploadMapper;
    private String[] paths;
    private FileUpload fileUpload;
    private String nginxServer;

    public LowKbpsHandler(FileUploadMapper fileUploadMapper, String[] paths, FileUpload fileUpload, String nginxServer) {
        this.fileUploadMapper = fileUploadMapper;
        this.paths = paths;
        this.fileUpload = fileUpload;
        this.nginxServer = nginxServer;
    }

    @Override
    public void run() {

        try {
            Long start = System.currentTimeMillis();
            ConvertVideoUtil.lowKmps(paths);
            Long end = System.currentTimeMillis();
            System.out.println("耗时：" + (end - start) / 1000 + "秒");
            FastDFSUtils fastDFSUtils = new FastDFSUtils("classpath:FastDFS.properties");
            String mediaCachePath = nginxServer + "/" + fastDFSUtils.uploadFile(paths[1], "mp3");
            fileUpload.setMediaCachePath(mediaCachePath);
            fileUploadMapper.updateFileSelective(fileUpload);
            for (String path : paths) {
                new File(path).delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
