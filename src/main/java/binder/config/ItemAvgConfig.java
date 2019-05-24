package binder.config;

import org.slf4j.Logger;

public class ItemAvgConfig implements AbstractConfig {
	
	@Override
	public void logConfig(Logger logger) {
		logger.info("Item Average");
	}

}
