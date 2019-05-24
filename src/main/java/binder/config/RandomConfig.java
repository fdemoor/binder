package binder.config;

import org.slf4j.Logger;

public class RandomConfig implements AbstractConfig {
	
	@Override
	public void logConfig(Logger logger) {
		logger.info("Random");
	}

}
