package group.xuxiake.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取properties配置文件工具类
 */
@Slf4j
public class PropertiesUtil {

    private static Properties props;
    //静态代码块>>普通代码块>>构造代码块
    static {
        String fileName = NetdiskConstant.CONFIG_PROPERTIES_FILE_NAME;
        props = new Properties();
        try {
            //读取propertiesUtil类的配置
            //利用反射加载类信息，获取配置文件的文件流，并指点编码格式
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            log.error("配置文件读取异常",e);
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public Map<String, String> getAllProperty(){
        Map<String, String> map=new HashMap();
        Enumeration enu = props.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = props.getProperty(key);
            map.put(key, value);
        }
        return map;
    }
}
