package group.xuxiake.shiro;

import javax.annotation.Resource;

import group.xuxiake.exception.UserRepeatLoginException;
import group.xuxiake.web.websocket.chat.ChatWebSocketHandler;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import group.xuxiake.entity.UserNetdisk;
import group.xuxiake.service.UserNetdiskService;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {

	@Resource
	private UserNetdiskService userNetdiskService;
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//Principal 当事人
		//Credentials 认证信息
		
		//获取从前端传过来的登录信息
		String loginInfo = (String) token.getPrincipal();
		//realm名字
		String realmName = getName();
		//这里做登陆信息验证。。。
		UserNetdisk userNetdisk = null;
		if(loginInfo.contains("@")){
			userNetdisk = userNetdiskService.findByEmail(loginInfo);
		}else {
			userNetdisk = userNetdiskService.findByName(loginInfo);
			if (userNetdisk == null) {
				userNetdisk = userNetdiskService.findByPhone(loginInfo);
			}
		}
		if (userNetdisk==null) {
			throw new UnknownAccountException();
		}
		Set<Map.Entry<Integer, WebSocketSession>> entries = ChatWebSocketHandler.socketSessionMap.entrySet();
		for (Map.Entry<Integer, WebSocketSession> entry : entries) {
			if (userNetdisk.getId().intValue() == entry.getKey().intValue()) {
				// 用户重复登录
				throw new UserRepeatLoginException();
			}
		}
		//盐
		ByteSource credentialsSalt =  ByteSource.Util.bytes(userNetdisk.getUsername());
		SimpleAuthenticationInfo simpleAuthenticationInfo = 
				new SimpleAuthenticationInfo(userNetdisk, userNetdisk.getPassword(), credentialsSalt, realmName);
		return simpleAuthenticationInfo;
	}

}
