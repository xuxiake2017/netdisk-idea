package group.xuxiake.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import group.xuxiake.entity.FileUpload;
import group.xuxiake.service.FileUploadService;
import group.xuxiake.util.Result;

/**
 * 用于文件夹操作的Controller
 * @author xuxiake
 *
 */
@Controller
@RequestMapping("/dir")
public class DirController {

	@Resource
	protected FileUploadService fileUploadService;

	/**
	 * 列出所有文件夹，用于文件移动
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="/listAllDir")
	@ResponseBody
	public Result listAllDir(Integer parentId) {
		return fileUploadService.listAllDir(parentId);
	}

	/**
	 * 移动文件
	 * @param fileUpload
	 * @return
	 */
	@RequestMapping(value="/moveFile")
	@ResponseBody
	public Result moveFile(FileUpload fileUpload) {
		return fileUploadService.moveFile(fileUpload);
	}

	/**
	 * 创建新的文件夹
	 * @param fileUpload
	 * @return
	 */
	@RequestMapping(value="/mkDir")
	@ResponseBody
	public Result mkDir(FileUpload fileUpload) {
		
		return fileUploadService.mkDir(fileUpload);
	}
}
