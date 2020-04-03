package group.xuxiake.service;

import group.xuxiake.entity.Message;
import group.xuxiake.util.Result;

public interface MessageService {

    Result addMessage(Message message);

    Result haveRead(Integer id);
}
