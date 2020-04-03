package group.xuxiake.web.upload;

import org.apache.commons.fileupload.ProgressListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * 自定义上传进度监听器
 */
@Component
public class FileUploadProgressListener implements ProgressListener {

    @Autowired
    private HttpSession session;

    /**
     * @param pBytesRead 到目前为止读取文件的比特数
     * @param pContentLength 文件总大小
     * @param pItems 目前正在读取第几个文件
     */
    @Override
    public void update(long pBytesRead, long pContentLength, int pItems) {
        Progress status = new Progress();
        status.setpBytesRead(pBytesRead);
        status.setpContentLength(pContentLength);
        status.setpItems(pItems);
        ProgressSingleton.put(session.getId(),status);
    }
}
