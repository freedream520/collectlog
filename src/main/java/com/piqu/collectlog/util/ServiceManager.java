package com.piqu.collectlog.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ServiceManager {

	private static ServiceManager serverManager = null;
	private ApplicationContext context = null;

	private ServiceManager() {
		context = new ClassPathXmlApplicationContext("mongodb.xml");
	}

	public static ServiceManager newInstance() {
		if (null == serverManager) {
			serverManager = new ServiceManager();
		}

		return serverManager;
	}

	public <T> T getBean(Class<T> className) {

		T t = context.getBean(className);
		return t;
	}
	
	public String getVlue(String source,String key) throws IOException{
		Properties properties = PropertiesLoaderUtils.loadAllProperties(source);
		String value = (String) properties.get(key);
		
		return value;
	}

}
