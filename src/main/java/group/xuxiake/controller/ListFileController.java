package group.xuxiake.controller;

import javax.annotation.Resource;

import group.xuxiake.service.ListFileService;
import org.springframework.web.bind.annotation.RequestMapping;

import group.xuxiake.entity.Page;
import group.xuxiake.util.Result;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于得到文件列表的Controller（异步加载）
 * @author xuxiake
 *
 */
@RequestMapping("/file")
@RestController
public class ListFileController {

	@Resource
	private ListFileService listFileService;

	@RequestMapping("/listFile")
	public Result listFile(Page page) {
		return listFileService.listFile(page);
	}

	/**
	 * 查找所有文档
	 * @param page
	 * @return
	 */
	@RequestMapping("getDocumentList")
	public Result getDocumentList(Page page) {
		return listFileService.getDocumentList(page);
	}

	/**
	 * 查找所有视频
	 * @param page
	 * @return
	 */
	@RequestMapping("getVideoList")
	public Result getVideoList(Page page) {
		return listFileService.getVideoList(page);
	}

	/**
	 * 查找所有图片
	 * @param page
	 * @return
	 */
	@RequestMapping("getPicList")
	public Result getPicList(Page page) {
		return listFileService.getPicList(page);
	}

	/**
	 * 查找所有音乐
	 * @param page
	 * @return
	 */
	@RequestMapping("getAudioList")
	public Result getAudioList(Page page) {
		return listFileService.getAudioList(page);
	}

}
