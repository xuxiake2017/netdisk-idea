package group.xuxiake.web.upload;

/**
 * 储存上传进度的实体类
 * pBytesRead 到目前为止读取文件的比特数 pContentLength 文件总大小 pItems 目前正在读取第几个文件
 */
public class Progress {

    private Long pBytesRead;
    private Long pContentLength;
    private Integer pItems;

    public Long getpBytesRead() {
        return pBytesRead;
    }

    public void setpBytesRead(Long pBytesRead) {
        this.pBytesRead = pBytesRead;
    }

    public Long getpContentLength() {
        return pContentLength;
    }

    public void setpContentLength(Long pContentLength) {
        this.pContentLength = pContentLength;
    }

    public Integer getpItems() {
        return pItems;
    }

    public void setpItems(Integer pItems) {
        this.pItems = pItems;
    }
}
