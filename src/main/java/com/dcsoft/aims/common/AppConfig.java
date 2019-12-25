package com.dcsoft.aims.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * 在spring boot项目里建议全局初始化ObjectMapper
 * @author fpb
 *
 */
@Configuration
public class AppConfig {

	@Bean
	public ObjectMapper ObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new ParameterNamesModule())
				.registerModule(new Jdk8Module())
				.registerModule(new JavaTimeModule());
		
		objectMapper.findAndRegisterModules();
		// 忽略json字符串中不识别的属性
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
		
		
		return objectMapper;

	}
}
