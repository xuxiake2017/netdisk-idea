package group.xuxiake.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import group.xuxiake.entity.FileUpload;
import group.xuxiake.entity.Page;
import group.xuxiake.entity.UserNetdisk;
import group.xuxiake.mapper.FileUploadMapper;
import group.xuxiake.service.ImageService;
import group.xuxiake.util.Result;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("imageService")
public class ImageServiceImpl implements ImageService {

    @Resource
    private FileUploadMapper fileUploadMapper;
    @Value("#{fd.nginx_server}")
    private String nginxServer;


    /*
     * 将一个集合中的图片按天为单位排序
     */
    public List<Map<Object, Object>> sortImgByDay(PageInfo<FileUpload> pageInfo) {
        List<FileUpload> list = pageInfo.getList();
        List<Map<Object, Object>> listSort = new ArrayList<>();
        if (list==null || list.isEmpty()) {
            return listSort;
        }
        Long startTimes = list.get(0).getShootTime().getTime();
        Long endTime = 0L;
        try {
            endTime = getDay(list.get(list.size()-1).getShootTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long dayTimes = 24*60*60*1000L;

        long i = startTimes;
        while(i>endTime) {
            Map<Object, Object> map = new HashMap<>();
            List<FileUpload> listOneDay = new ArrayList<>();
            for (FileUpload fileUpload : list) {
                if (fileUpload.getShootTime().getTime()<=i && fileUpload.getShootTime().getTime()>i-dayTimes) {
                    listOneDay.add(fileUpload);
                }
            }
            if (listOneDay!=null && !listOneDay.isEmpty()) {
                map.put(i, listOneDay);
                listSort.add(map);
            }
            i-=dayTimes;
        }
        return listSort;
    }

    /*
     * 得到某一天零点的毫秒值
     */
    public Long getDay(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return sdf.parse(dateStr).getTime();
    }

    @Override
    public Result toImgList(Page page) {
        UserNetdisk userNetdisk = (UserNetdisk) SecurityUtils.getSubject().getPrincipal();
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<FileUpload> list = fileUploadMapper.findAllImg(userNetdisk.getId());
        PageInfo<FileUpload> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>();
        map.put("pageInfo", pageInfo);
        map.put("list", sortImgByDay(pageInfo));
        map.put("nginxServer", nginxServer);
        Result result = new Result();
        result.setData(map);
        return result;
    }
}
