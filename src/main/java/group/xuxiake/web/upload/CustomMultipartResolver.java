package group.xuxiake.web.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 自定义的文件上传解析器
 */
public class CustomMultipartResolver extends CommonsMultipartResolver {

    @Autowired
    private FileUploadProgressListener progressListener;

    @Override
    public MultipartParsingResult parseRequest(HttpServletRequest request)
            throws MultipartException {

        //启动线程，用于向前端发送上传进度
        new Thread(new ProgressSendHandler(request.getSession())).start();

        String encoding = super.determineEncoding(request);
        FileUpload fileUpload = super.prepareFileUpload(encoding);
        fileUpload.setProgressListener(progressListener);
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return super.parseFileItems(fileItems, encoding);
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }

}


