package com.ewcms.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ewcms.content.resource.model.Resource;
import com.ewcms.content.resource.service.ResourceService;

/**
 * @author 吴智俊
 */
public class ResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 8534030248495903180L;

	private static final String DOWNLOAD_TYPE = "application/x-msdownload";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
		try {
			String uri = request.getRequestURI();
			String contextPath = request.getContextPath();
			uri.replace(contextPath, "");
			createResource(uri, byteArrayOutStream);
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		initResponseHeader(response);
		responseResource(response, byteArrayOutStream.toByteArray());
		byteArrayOutStream.close();
	}

	private void createResource(final String uri, final OutputStream stream) throws IOException {
		ResourceService resourceService = getResourceService();
		Resource resource = resourceService.getResourceByUri(uri);
		if (resource == null) {
			return;
		}
		String realPath = resource.getPath();
		if (StringUtils.endsWith(resource.getThumbUri(), uri)) {
			realPath = resource.getThumbPath();
		}
		try {
			IOUtils.copy(new FileInputStream(realPath), stream);
		} catch (Exception e) {
		}
	}

	private void initResponseHeader(final HttpServletResponse response) {
		response.setHeader("Content-Language", "zh-CN");
		response.setHeader("Cache-Control", "no-store");
		// set on cache
		// /Http 1.0 header
		response.setDateHeader("Expires", 0);
		response.addHeader("Pragma", "no-cache");
		// Http 1.1 header
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType(DOWNLOAD_TYPE);
	}

	private void responseResource(HttpServletResponse response, byte[] imageBytes) throws IOException {
		OutputStream stream = response.getOutputStream();
		stream.write(imageBytes);
		stream.flush();
		stream.close();
	}

	protected ResourceService getResourceService() {
		ServletContext application = getServletContext();
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(application);
		return (ResourceService) wac.getBean("resourceService");
	}
}
