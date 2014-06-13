/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.externalds.generate.factory.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ewcms.plugin.BaseRuntimeException;
import com.ewcms.plugin.externalds.generate.factory.DataSourceFactoryable;
import com.ewcms.plugin.externalds.generate.service.EwcmsDataSourceServiceable;
import com.ewcms.plugin.externalds.generate.service.jdbc.JdbcDataSourceService;
import com.ewcms.plugin.externalds.model.BaseDs;
import com.ewcms.plugin.externalds.model.JndiDs;

/**
 * Jndi数据源工厂模式
 *
 * @author 吴智俊
 */
@Component(value = "jndiDataSourceFactory")
public class JndiDataSourceFactory implements DataSourceFactoryable {

	private static final Logger logger = LoggerFactory.getLogger(JndiDataSourceFactory.class);
    private Context ctx = null;

    public JndiDataSourceFactory() {
        try {
            ctx = new InitialContext();
        } catch (NamingException e) {
            logger.error("NamingException", e);
            throw new BaseRuntimeException(e);
        }
    }

    @Override
    public EwcmsDataSourceServiceable createService(BaseDs alqcDataSource) {
        try {
            if (!(alqcDataSource instanceof JndiDs)) {
            	logger.error("无效的Jndi数据源");
                throw new BaseRuntimeException("无效的Jndi数据源", new Object[]{alqcDataSource.getClass()});
            }
            JndiDs jndiDataSource = (JndiDs) alqcDataSource;

            String jndiName = jndiDataSource.getJndiName();

            DataSource ds = (DataSource) ctx.lookup(jndiName);
            return new JdbcDataSourceService(ds);
        } catch (NamingException e) {
            logger.error("NamingException", e);
            throw new BaseRuntimeException(e);
        }
    }
}
