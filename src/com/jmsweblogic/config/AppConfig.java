package com.jmsweblogic.config;

import java.util.ResourceBundle;

public class AppConfig {
	public static final ResourceBundle JMS_CONFIG;
	static {
		JMS_CONFIG = ResourceBundle.getBundle("com.jmsweblogic.config.jms");
	}
}
