package com.devglan.error;

import org.springframework.context.annotation.Bean;

import com.devglan.config.DefaultApiExceptionHandler;

/*
 * Common Beans used by all Api Services
 */
public class ApiServiceConfig {
	
	@Bean
	public DefaultApiExceptionHandler apiServiceExceptionHandler() {
		return new DefaultApiExceptionHandler();
	}
}
