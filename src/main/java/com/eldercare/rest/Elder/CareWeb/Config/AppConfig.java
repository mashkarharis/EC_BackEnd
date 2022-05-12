package com.eldercare.rest.Elder.CareWeb.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eldercare.rest.Elder.CareWeb.Filter.CustomWebFilter;

@Configuration
public class AppConfig {

	@Bean
	public FilterRegistrationBean<CustomWebFilter> filterRegistrationBean() {
		FilterRegistrationBean<CustomWebFilter> registrationBean = new FilterRegistrationBean();
		CustomWebFilter customWebFilter = new CustomWebFilter();

		registrationBean.setFilter(customWebFilter);
		registrationBean.addUrlPatterns("/web/private/*");
		registrationBean.setOrder(2); // set precedence
		return registrationBean;
	}
}