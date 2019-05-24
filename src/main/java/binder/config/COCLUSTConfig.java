package binder.config;

import org.slf4j.Logger;

public class COCLUSTConfig implements AbstractConfig {
	
	private int nbUserClusters; /* Number of user clusters */
	private int nbItemClusters; /* Number of item clusters */
	private int nbMaxIterations; /* Maximum number of iterations */
	
	public void setNbUserClusters(int n) {
		this.nbUserClusters = n;
	}
	
	public int getNbUserClusters() {
		return this.nbUserClusters;
	}
	
	public void setNbItemClusters(int n) {
		this.nbItemClusters = n;
	}
	
	public int getNbItemClusters() {
		return this.nbItemClusters;
	}
	
	public void setNbMaxIterations(int n) {
		this.nbMaxIterations = n;
	}
	
	public int getNbMaxIterations() {
		return this.nbMaxIterations;
	}
	
	@Override
	public void logConfig(Logger logger) {
		logger.info("COCLUST");
		logger.info("Number of user clusters: {}", this.nbUserClusters);
		logger.info("Number of item clusters: {}", this.nbItemClusters);
		logger.info("Maximum number of iterations: {}", this.nbMaxIterations);
	}

}
