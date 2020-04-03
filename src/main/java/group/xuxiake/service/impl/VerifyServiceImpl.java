package group.xuxiake.service.impl;

import group.xuxiake.entity.Message;
import group.xuxiake.entity.UserNetdisk;
import group.xuxiake.mapper.MessageMapper;
import group.xuxiake.mapper.UserNetdiskMapper;
import group.xuxiake.service.UserNetdiskService;
import group.xuxiake.service.VerifyService;
import group.xuxiake.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Service("verifyService")
public class VerifyServiceImpl implements VerifyService {

    @Resource
    private UserNetdiskMapper userNetdiskMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserNetdiskService userNetdiskService;
    @Resource
    private MessageMapper messageMapper;
    @Autowired
    private HttpSession session;
    @Value("#{conf.verifySmsExpire}")
    private Integer verifySmsExpire;

    /**
     * 验证邮箱
     * @param key
     * @return
     */
    @Override
    public Result verifyEmail(String key) {
        Result result = new Result();
        if (key == null || "".equals(key)) {
            result.setCode(NetdiskErrMsgConstant.PARAM_IS_NULL);
            result.setMsg(NetdiskErrMsgConstant.getErrMessage(NetdiskErrMsgConstant.PARAM_IS_NULL));
            return result;
        }
        Map<String, Object> map = redisUtil.getObj("EMAIL_KEY:", key, Map.class);
        if (map == null || map.size() == 0) {
            result.setCode(NetdiskErrMsgConstant.VERIFY_TIME_OUT);
            result.setMsg(NetdiskErrMsgConstant.getErrMessage(NetdiskErrMsgConstant.VERIFY_TIME_OUT));
            return result;
        }
        Integer userId = (Integer) map.get("id");
        UserNetdisk userNetdiskCheck = userNetdiskMapper.selectByPrimaryKey(userId);
        if (userNetdiskCheck != null) {
            if (new Integer(userNetdiskCheck.getUserStatus()) != NetdiskConstant.USER_STATUS_NOT_VERIFY) {
                // 已验证
                result.setData(NetdiskErrMsgConstant.VERIFY_HAVING_BEEN_VERIFIED);
                return result;
            }
        }
        UserNetdisk userNetdisk = new UserNetdisk();
        userNetdisk.setId(userId);
        userNetdisk.setUserStatus(NetdiskConstant.USER_STATUS_NORMAL + "");
        userNetdiskMapper.updateByPrimaryKeySelective(userNetdisk);
        userNetdiskService.updatePrincipal();

        Message message = new Message();
        message.setType(NetdiskConstant.MESSAGE_TYPE_OF_SUCCESS);
        message.setTitle("账户激活成功");
        message.setDescription("您的账户激活成功，所有功能恢复正常使用");
        message.setUserId(userNetdisk.getId());
        messageMapper.insertSelective(message);

        return result;
    }

    /**
     * 给手机发送短信验证码
     * @param userNetdisk
     * @return
     */
    @Override
    public Result sendCodeToPhone(UserNetdisk userNetdisk) {

        Result result = new Result();
        String smsCode = null;
        try {
            smsCode = SmsSendUtil.regNetDisk(userNetdisk.getPhone());
//			smsCode = "1234";
            //业务限流
            if ("isv.BUSINESS_LIMIT_CONTROL".equals(smsCode)) {
                result.setCode(NetdiskErrMsgConstant.SEND_SMS_CODE_BUSINESS_LIMIT_CONTROL);
                result.setMsg(NetdiskErrMsgConstant.getErrMessage(NetdiskErrMsgConstant.SEND_SMS_CODE_BUSINESS_LIMIT_CONTROL));
                return result;
            }
            if (smsCode.length() == 4) {
                redisUtil.setObj("SMS_CODE:", session, smsCode, verifySmsExpire);
                return result;
            }
        } catch (Exception e) {
            log.error("短信验证码发送失败", e);
        }
        result.setCode(NetdiskErrMsgConstant.SEND_SMS_CODE_FAILED);
        result.setMsg(NetdiskErrMsgConstant.getErrMessage(NetdiskErrMsgConstant.SEND_SMS_CODE_FAILED));
        return result;
    }
}
