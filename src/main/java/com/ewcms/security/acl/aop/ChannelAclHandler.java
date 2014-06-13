package com.ewcms.security.acl.aop;

import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ewcms.security.acl.annotation.AclEnum;
import com.ewcms.security.acl.annotation.ChannelAcl;
import com.ewcms.security.acl.util.AclUtil;
import com.ewcms.site.model.Channel;
import com.ewcms.util.EwcmsContextUtil;

/**
 * @author 吴智俊
 */
@Component
@Aspect
public class ChannelAclHandler {
	protected static final Logger logger = LoggerFactory.getLogger(ChannelAclHandler.class);
	
	@Pointcut("@annotation(achannelAcl)")
	private void assertUnauthorized(ChannelAcl achannelAcl) {
	}

	@Before(value = "assertUnauthorized(achannelAcl)", argNames = "achannelAcl")
	public void judgment(JoinPoint pjp, ChannelAcl achannelAcl) {
		try{
			boolean hasAtLeastOnePermission = false;
			int position = achannelAcl.position();
			String className = achannelAcl.className();
			
			Long channelId = null;
			Object[] args = pjp.getArgs();
			if (className.toLowerCase().equals("long")){
				channelId = (Long) args[position];
			}else if (className.toLowerCase().equals("channel")){
				Channel channel = (Channel)args[position];
				channelId = channel.getId();
			}
			
			if (channelId == null) throw new UnauthorizedException();
			
			AclEnum[] aclEnums = achannelAcl.acl();
			for (AclEnum aclEnum : aclEnums){
				if (EwcmsContextUtil.isPermitted(AclUtil.PREFIX + ":" + aclEnum.getExpression() + ":" + channelId.longValue())) {
					hasAtLeastOnePermission = true;
					break;
				}
			}
			
			if (!hasAtLeastOnePermission) throw new UnauthorizedException();
		}catch(Exception e){
			throw new UnauthorizedException();
		}
		
	}
}
