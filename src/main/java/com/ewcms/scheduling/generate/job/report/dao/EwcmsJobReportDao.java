/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.scheduling.generate.job.report.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;

/**
 * 
 * @author wu_zhijun
 *
 */
public interface EwcmsJobReportDao extends PagingAndSortingRepository<EwcmsJobReport, Long>, EwcmsJobReportDaoCustom {
}
