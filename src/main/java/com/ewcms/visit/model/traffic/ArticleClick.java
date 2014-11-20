package com.ewcms.visit.model.traffic;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author wu_zhijun
 *
 */
@Entity
@Table(name = "plugin_visit_articleclick")
public class ArticleClick implements Serializable {

	private static final long serialVersionUID = 906993409704978626L;

	@EmbeddedId
	private ArticleClickPk articleClickPk;
	@Column(name = "pageView_sum")
	private Long pageViewSum = 0L;
	@Column(name = "stickTime_avg")
	private Double stickTimeAvg = 0D;

	public ArticleClick() {
	}
	
	public ArticleClick(Long pageViewSum, Double stickTimeAvg){
		super();
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}
	
	public ArticleClick(String channelName, String articleTitle, String url, String created, Long pageViewSum, Double stickTimeAvg){
		super();
		if (articleClickPk == null) articleClickPk = new ArticleClickPk(channelName, articleTitle, url, created);
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
	}
	
	public ArticleClickPk getArticleClickPk() {
		return articleClickPk;
	}

	public void setArticleClickPk(ArticleClickPk articleClickPk) {
		this.articleClickPk = articleClickPk;
	}

	public Long getPageViewSum() {
		return pageViewSum;
	}

	public void setPageViewSum(Long pageViewSum) {
		this.pageViewSum = pageViewSum;
	}

	public Double getStickTimeAvg() {
		return stickTimeAvg;
	}

	public void setStickTimeAvg(Double stickTimeAvg) {
		this.stickTimeAvg = stickTimeAvg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((articleClickPk == null) ? 0 : articleClickPk.hashCode());
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
		ArticleClick other = (ArticleClick) obj;
		if (articleClickPk == null) {
			if (other.articleClickPk != null)
				return false;
		} else if (!articleClickPk.equals(other.articleClickPk))
			return false;
		return true;
	}
}
