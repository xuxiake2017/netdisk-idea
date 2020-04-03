
package group.xuxiake.controller;

import javax.annotation.Resource;

import group.xuxiake.service.ImageService;
import group.xuxiake.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import group.xuxiake.entity.Page;

/**
 * 用于处理图片的Controller
 * @author xuxiake
 *
 */
@Controller
@RequestMapping("/img")
public class ImageController {

	@Resource
	private ImageService imageService;
	/*
	 * 得到图片列表
	 */
	@RequestMapping("/toImgList")
	@ResponseBody
	public Result toImgList(Page page) {
		return imageService.toImgList(page);
	}
}
