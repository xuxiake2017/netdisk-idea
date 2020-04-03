package group.xuxiake.service.impl;

import group.xuxiake.entity.Message;
import group.xuxiake.mapper.MessageMapper;
import group.xuxiake.service.MessageService;
import group.xuxiake.util.NetdiskConstant;
import group.xuxiake.util.NetdiskErrMsgConstant;
import group.xuxiake.util.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public Result addMessage(Message message) {
        Result result = new Result();
        messageMapper.insertSelective(message);
        return result;
    }

    /**
     * 消息已读
     * @param id
     * @return
     */
    @Override
    public Result haveRead(Integer id) {
        Result result = new Result();
        if (id == null) {
            result.setCode(NetdiskErrMsgConstant.PARAM_IS_NULL);
            result.setMsg(NetdiskErrMsgConstant.getErrMessage(NetdiskErrMsgConstant.PARAM_IS_NULL));
            return result;
        }
        Message message = new Message();
        message.setId(id);
        message.setStatus(NetdiskConstant.DATA_DELETE_STATUS);
        messageMapper.updateByPrimaryKeySelective(message);
        return result;
    }
}
