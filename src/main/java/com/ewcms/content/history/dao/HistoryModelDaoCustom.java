package com.ewcms.content.history.dao;

import java.util.Date;

public interface HistoryModelDaoCustom {
	void delByBeforeDate(final Date createDate);
}
