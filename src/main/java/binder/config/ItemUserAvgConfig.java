package binder.config;

import org.slf4j.Logger;

public class ItemUserAvgConfig implements AbstractConfig {
	
	@Override
	public void logConfig(Logger logger) {
		logger.info("Item User Average");
	}

}
