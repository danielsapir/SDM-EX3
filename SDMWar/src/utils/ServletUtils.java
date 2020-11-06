package utils;

import SDM.SDMEngine;
import SDM.UserManager;

import javax.servlet.ServletContext;

public class ServletUtils {

	private static final String SDMENGINE_ATTRIBUTE_NAME = "sdmengine";
	private static final String USER_MANAGER_ATTRIBUTE_NAME = "usermanager";

	private static final Object sdmEngineLock = new Object();
	private static final Object userManagerLock = new Object();

	public static SDMEngine getSDMEngine(ServletContext servletContext) {

		if(servletContext.getAttribute(SDMENGINE_ATTRIBUTE_NAME) == null) {
			synchronized (sdmEngineLock) {
				if (servletContext.getAttribute(SDMENGINE_ATTRIBUTE_NAME) == null) {
					servletContext.setAttribute(SDMENGINE_ATTRIBUTE_NAME, new SDMEngine());
				}
			}
		}

		return (SDMEngine) servletContext.getAttribute(SDMENGINE_ATTRIBUTE_NAME);
	}


	public static UserManager getUserManager(ServletContext servletContext) {

		if(servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
			synchronized (userManagerLock) {
				if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
					servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
				}
			}
		}

		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}
}
