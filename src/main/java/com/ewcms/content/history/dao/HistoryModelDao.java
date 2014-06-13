package com.ewcms.content.history.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.history.model.HistoryModel;

public interface HistoryModelDao extends PagingAndSortingRepository<HistoryModel, Long>, JpaSpecificationExecutor<HistoryModel>, HistoryModelDaoCustom{

}
