package group.xuxiake.util;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FastDFSUtils {

    private TrackerClient trackerClient;
    private TrackerServer trackerServer;
    private StorageClient1 storageClient1;

    public FastDFSUtils(String conf) throws IOException, MyException {
        if (conf.startsWith("classpath:")){
            conf = conf.replace("classpath:",FastDFSUtils.class.getResource("/").getPath());
        }
        ClientGlobal.init(conf);
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getConnection();
        storageClient1 = new StorageClient1(trackerServer, null);
    }

    public String uploadFile(byte[] bytes, String extName) throws IOException, MyException {

        String path = storageClient1.upload_file1(bytes, extName,null);
        return path;
    }
    public String uploadFile(String fileName, String extName) throws IOException, MyException {

        String path = storageClient1.upload_file1(fileName, extName,null);
        return path;
    }
    public byte[] downloadFile(String group_name, String remote_filename) throws IOException, MyException {
    	byte[] bytes = storageClient1.download_file(group_name, remote_filename);
    	return bytes;
    }

    /**
     * 从FastDFS路径中获取输入流
     * @param filePath
     * @return
     * @throws IOException
     * @throws MyException
     */
    public static InputStream getInputStream(String filePath) throws IOException, MyException {
        String group_name = filePath.substring(0, filePath.indexOf("/"));
        String remote_filename = filePath.substring(filePath.indexOf("/") + 1, filePath.length());
        FastDFSUtils fastDFSUtils = new FastDFSUtils("classpath:FastDFS.properties");
        byte[] bytes = fastDFSUtils.downloadFile(group_name, remote_filename);
        InputStream is = new ByteArrayInputStream(bytes);
        return is;
    }
}
