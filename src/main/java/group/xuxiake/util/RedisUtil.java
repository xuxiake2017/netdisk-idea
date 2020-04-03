package group.xuxiake.util;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.crazycake.shiro.serializer.StringSerializer;
import org.springframework.stereotype.Component;

@Component("redisUtil")
public class RedisUtil {

	@Resource
	private RedisManager redisManager;

	/**
	 * 将对象持久化存进redis（自定义标示key）
	 * @param prefix
	 * @param tag
	 * @param obj
	 * @param expire
	 */
	public void setObj(String prefix, String tag, Object obj, int expire) {
		byte[] key = null;
		byte[] value = null;
		try {
			key = new StringSerializer().serialize(prefix+tag);
			value = new ObjectSerializer().serialize(obj);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		redisManager.set(key, value, expire);
	}
	/**
	 * 将对象持久化存进redis（以session id作为标示key）
	 * @param prefix
	 * @param session
	 * @param obj
	 * @param expire
	 */
	public void setObj(String prefix, HttpSession session, Object obj, int expire) {
		String id = session.getId();
		byte[] key = null;
		byte[] value = null;
		try {
			key = new StringSerializer().serialize(prefix+id);
			value = new ObjectSerializer().serialize(obj);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		redisManager.set(key, value, expire);
	}
	
	/**
	 * 把持久化的对象从redis中取出来（以session id作为标示key）
	 * @param prefix
	 * @param session
	 * @param c
	 * @return
	 */
	public <T> T getObj(String prefix, HttpSession session, Class<T> c) {
		String id = session.getId();
		byte[] key = null;
		Object obj = null;
		try {
			key = new StringSerializer().serialize(prefix+id);
			byte[] bs = redisManager.get(key);
			obj = new ObjectSerializer().deserialize(bs);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		return (T)obj;
	}

	/**
	 * 把持久化的对象从redis中取出来（自定义标示key）
	 * @param prefix
	 * @param tag
	 * @param c
	 * @param <T>
	 * @return
	 */
	public <T> T getObj(String prefix, String tag, Class<T> c) {
		byte[] key = null;
		Object obj = null;
		try {
			key = new StringSerializer().serialize(prefix+tag);
			byte[] bs = redisManager.get(key);
			obj = new ObjectSerializer().deserialize(bs);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		return (T)obj;
	}
	
	/**
	 * 删除存在redis中的对象（以session id作为标示key）
	 * @param prefix
	 * @param session
	 */
	public void delObj(String prefix, HttpSession session) {
		String id = session.getId();
		byte[] key = null;
		try {
			key = new StringSerializer().serialize(prefix+id);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		redisManager.del(key);
	}

	/**
	 * 删除存在redis中的对象（自定义标示key）
	 * @param prefix
	 * @param tag
	 */
	public void delObj(String prefix, String tag) {
		byte[] key = null;
		try {
			key = new StringSerializer().serialize(prefix+tag);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		redisManager.del(key);
	}
}
