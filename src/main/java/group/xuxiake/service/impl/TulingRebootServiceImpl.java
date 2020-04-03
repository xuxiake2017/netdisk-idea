package group.xuxiake.service.impl;

import com.alibaba.fastjson.JSONObject;
import group.xuxiake.entity.UserNetdisk;
import group.xuxiake.service.TulingRebootService;
import group.xuxiake.util.HttpClientUtils;
import group.xuxiake.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Author by xuxiake, Date on 2019/4/30.
 * PS: Not easy to write code, please indicate.
 * Description：
 */
@Service("tulingRebootService")
public class TulingRebootServiceImpl implements TulingRebootService {

    @Value("#{conf.tuling_apiKey}")
    private String apiKey;
    @Value("#{conf.tuling_apiUrl}")
    private String apiUrl;
    /**
     * 发送消息
     * @param message
     * @return
     */
    @Override
    public Result sendMessage(String message) {

        Result result = new Result();
        if (StringUtils.isAnyEmpty(message)) {
            return Result.paramIsNull(result);
        }
        UserNetdisk user = (UserNetdisk) SecurityUtils.getSubject().getPrincipal();

        Map<String, Object> inputText = new HashMap<>();
        inputText.put("text", message);
        Map<String, Object> perception = new HashMap<>();
        perception.put("inputText", inputText);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("apiKey", apiKey);
        userInfo.put("userId", user.getId());

        Map<String, Object> msg = new HashMap<>();
        msg.put("reqType", 0);
        msg.put("perception", perception);
        msg.put("userInfo", userInfo);

        JSONObject jsonObject = new JSONObject(msg);
        JSONObject receive = HttpClientUtils.doPost(apiUrl, jsonObject);
        result.setData(receive);
        return result;
    }
}
