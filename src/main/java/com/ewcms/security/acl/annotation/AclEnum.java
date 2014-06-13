package com.ewcms.security.acl.annotation;

/**
 * @author 吴智俊
 */
public enum AclEnum {
	VIEW_ARTICLE("viewa", "查看文章"), 
	WRITER_ARTICLE("writera", "修改文章"), 
	DELETE_ARTICLE("deletea", "删除文章"), 
	VERIFY_ARTICLE("verifya", "审核文章"), 
	PUBLISH_ARTICLE("publisha", "发布文章"),
	VIEW_CHANNEL("viewc", "查看栏目"),
	WRITER_CHANNEL("writerc", "修改栏目"), 
	DELETE_CHANNEL("deletec", "删除栏目"),
	ADMIN_CHANNEL("*", "管理员");
	
	private String expression;
	private String description;

	private AclEnum(String expression, String description) {
		this.expression = expression;
		this.description = description;
	}

	public String getExpression() {
		return expression;
	}

	public String getDescription() {
		return description;
	}
}
