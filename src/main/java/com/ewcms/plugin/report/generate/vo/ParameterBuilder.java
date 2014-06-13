package com.ewcms.plugin.report.generate.vo;

import java.io.Serializable;
import java.util.Map;

import com.ewcms.plugin.report.model.TextReport;

/**
 * @author 吴智俊
 */
public class ParameterBuilder implements Serializable {

	private static final long serialVersionUID = -6953111348682302225L;

	private Long reportId;
	private String reportType;
	private TextReport.Type textType;
	private Map<String, String> paramMap;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public TextReport.Type getTextType() {
		return textType;
	}

	public void setTextType(TextReport.Type textType) {
		this.textType = textType;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reportId == null) ? 0 : reportId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterBuilder other = (ParameterBuilder) obj;
		if (reportId == null) {
			if (other.reportId != null)
				return false;
		} else if (!reportId.equals(other.reportId))
			return false;
		return true;
	}
}
