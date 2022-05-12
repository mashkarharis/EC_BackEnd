package com.eldercare.rest.Elder.CareWeb.Filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.eldercare.rest.Elder.CareWeb.Services.AdminService;

public class CustomWebFilter implements Filter {

	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomWebFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("########## Initiating CustomWebFilter filter ##########");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String token = request.getHeader("Authorization");
		System.out.println(token);
		boolean valid = false;
		try {
			AdminService adminService=new AdminService();
			valid = adminService.introspect(token);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			valid = false;
		}

		LOGGER.info("This Filter is only called when request is mapped for /web/private resource");

		if (!valid) {
			response.setStatus(401);
			return;
		}

		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
