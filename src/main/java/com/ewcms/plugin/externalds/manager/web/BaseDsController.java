package com.ewcms.plugin.externalds.manager.web;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.plugin.externalds.generate.factory.DataSourceFactoryable;
import com.ewcms.plugin.externalds.generate.factory.init.EwcmsDataSourceFactory;
import com.ewcms.plugin.externalds.generate.service.EwcmsDataSourceServiceable;
import com.ewcms.plugin.externalds.manager.service.BaseDsService;
import com.ewcms.plugin.externalds.model.BaseDs;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/externalds")
public class BaseDsController {
	
	@Autowired
	private BaseDsService baseDsService;
	@Autowired
	private EwcmsDataSourceFactory ewcmsDataSourceFactory;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "plugin/externalds/index";
	}
	
	@RequestMapping(value = "isConnect/{baseDsId}", produces="text/plain;charset=UTF-8")
	public @ResponseBody String isConnect(@PathVariable(value = "baseDsId")Long baseDsId){
		EwcmsDataSourceServiceable service = null;
		Connection con = null;
		String result = "测试数据库连接失败,请确认填写的内容正确!";
		try{
			BaseDs baseDs = baseDsService.findByBaseDs(baseDsId);

			DataSourceFactoryable factory = (DataSourceFactoryable) ewcmsDataSourceFactory.getBean(baseDs.getClass());
			service = factory.createService(baseDs);
			con = service.openConnection();
			
			if (!con.isClosed())
				result = "测试数据库连接正确,您可以在以后的程序中使用!";
		}catch(Exception e){
		}finally{
			try{
				if (con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
			}
			try{
				if (service != null){
					service.closeConnection();
					service = null;
				}
			}catch(Exception e){
			}
		}
		return result;
	}
}
