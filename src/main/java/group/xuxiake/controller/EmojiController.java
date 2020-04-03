package group.xuxiake.controller;

import group.xuxiake.service.EmojiService;
import group.xuxiake.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: xuxiake
 * @create: 2019-05-08 22:52
 * @description:
 **/
@RestController
@RequestMapping("/emoji")
public class EmojiController {

    @Resource
    private EmojiService emojiService;

    @RequestMapping("/parseToHtmlDecimal")
    public Result parseToHtmlDecimal(String aliase) {

        return emojiService.parseToHtmlDecimal(aliase);
    }
}
