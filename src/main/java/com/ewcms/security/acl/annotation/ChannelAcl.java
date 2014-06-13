package com.ewcms.security.acl.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author 吴智俊
 */
@Target({ METHOD })
@Retention(RUNTIME)
public @interface ChannelAcl {
	AclEnum[] acl();

	int position() default -1;
	
	String className() default "Long";
}
