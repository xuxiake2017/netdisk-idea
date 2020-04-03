package group.xuxiake.service.impl;

import com.vdurmont.emoji.EmojiParser;
import group.xuxiake.service.EmojiService;
import group.xuxiake.util.Result;
import org.springframework.stereotype.Service;

/**
 * @author: xuxiake
 * @create: 2019-05-08 22:54
 * @description:
 **/
@Service("emojiService")
public class EmojiServiceImpl implements EmojiService {
    @Override
    public Result parseToHtmlDecimal(String aliase) {

        Result result = new Result();
        result.setData(EmojiParser.parseToUnicode(aliase));
        return result;
    }
}
