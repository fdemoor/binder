package binder.config;

import org.slf4j.Logger;

public class BCNConfig implements AbstractConfig {
	
	private int level; /* Recursion level for neighborhood biclusters search */
	
	public void setLevel(int n) {
		this.level = n;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	@Override
	public void logConfig(Logger logger) {
		logger.info("BCN");
		logger.info("Level: {}", this.level);
	}

}
