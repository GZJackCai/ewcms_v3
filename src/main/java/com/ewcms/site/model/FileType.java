package com.ewcms.site.model;

/**
 * 文件属性
 * 
 * @author wu_zhijun
 *
 */
public enum FileType {
	DIRECTORY("目录"), FILE("文件");
	private String description;

	private FileType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}