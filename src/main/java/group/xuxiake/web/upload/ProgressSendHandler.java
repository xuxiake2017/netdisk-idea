package group.xuxiake.web.upload;

import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import group.xuxiake.web.websocket.ProgressWebSocketHandler;

import java.io.IOException;

/**
 * 用于向前端发送上传进度的线程
 */
@Slf4j
public class ProgressSendHandler implements Runnable{

    private HttpSession session;
    
    public ProgressSendHandler(HttpSession session) {
        this.session = session;
    }

    @Override
    public void run() {
        String sessionId = session.getId();
        try {
            //线程启动时，上传进度监听器还未启动
            Thread.sleep(100);
            Progress status = (Progress) ProgressSingleton.get(session.getId());
            double progress = status.getpBytesRead() * 1.0 / status.getpContentLength() * 100;
            while (progress!=100.0){
                status = (Progress) ProgressSingleton.get(session.getId());
                progress = status.getpBytesRead() * 1.0 / status.getpContentLength() * 100;
                Thread.sleep(1000);
                log.debug("上传进度："+progress);
                if (ProgressWebSocketHandler.isAlive(sessionId)){
                    ProgressWebSocketHandler.sendMessage(sessionId, progress);
                }else {
                    break;
                }
            }

        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
            	Thread.sleep(1000);
                ProgressWebSocketHandler.close(sessionId);
                ProgressSingleton.remove(session.getId());
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            } catch (InterruptedException e) {
            	log.error(e.getMessage(),e);
			}
        }
    }
}
