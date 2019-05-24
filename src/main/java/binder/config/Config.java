package binder.config;

import org.slf4j.Logger;

import java.util.List;

public class Config implements AbstractConfig {

	private long seed; /* Seed for PRNG */
	private String dataset; /* Path to the dataset file */
	private int folds; /* Number of folds in cross validation */
	private List<String> configs;
	private int nbruns; /* Number of runs for each evaluation */
	private float threshold; /* Like-threshold for ratings */
	private int r; /*
					 * Max number of recommendations for precision@r and recall@r, will go from 5 to
					 * r with step 5
					 */
	private boolean oneR; /* Run eval with only one r instead of iterations */
	private boolean normalize; /* Normalize dataset by subtracting user mean rating */
	private boolean binarize; /* Binarize ratings into 0 or 1 */
	private boolean doPredEval; /* Run RMSE and MAE evaluation */
	private boolean doIREval; /* Run IRStats evaluation */
	private boolean doCoclustEval; /* Run COCLUST evaluation */
	private boolean doRelDistEval; /* Run relevant items distribution evaluation */
	private boolean doTimeEval; /* Run the time performance evaluation */
	private List<String> strategies; /* Strategy for candidate item selection */
	private boolean filterBBCF; /* Filter users in testing sets to have BBCF with ReachAtLeastOne of 1 */
	private boolean doPerUserEval; /* Run pred and irstats per user evaluation */

	public void setSeed(long s) {
		this.seed = s;
	}

	public long getSeed() {
		return this.seed;
	}

	public void setDataset(String s) {
		this.dataset = s;
	}

	public String getDataset() {
		return this.dataset;
	}

	public void setFolds(int n) {
		this.folds = n;
	}

	public int getFolds() {
		return this.folds;
	}

	public void setConfigs(List<String> l) {
		this.configs = l;
	}

	public List<String> getConfigs() {
		return this.configs;
	}

	public void setNruns(int n) {
		this.nbruns = n;
	}

	public int getNruns() {
		return this.nbruns;
	}

	public void setThreshold(float x) {
		this.threshold = x;
	}

	public float getThreshold() {
		return this.threshold;
	}

	public void setR(int n) {
		this.r = n;
	}

	public int getR() {
		return this.r;
	}

	public void setNormalize(boolean b) {
		this.normalize = b;
	}

	public boolean getNormalize() {
		return this.normalize;
	}

	public void setBinarize(boolean b) {
		this.binarize = b;
	}

	public boolean getBinarize() {
		return this.binarize;
	}

	public void setDoPredEval(boolean b) {
		this.doPredEval = b;
	}

	public boolean getDoPredEval() {
		return this.doPredEval;
	}

	public void setDoIREval(boolean b) {
		this.doIREval = b;
	}

	public boolean getDoIREval() {
		return this.doIREval;
	}

	
	public void setDoCoclustEval(boolean b) {
		this.doCoclustEval = b;
	}

	public boolean getDoCoclustEval() {
		return this.doCoclustEval;
	}
	
	public void setStrategies(List<String> l) {
		this.strategies = l;
	}
	
	public List<String> getStrategies() {
		return this.strategies;
	}
	
	public void setDoRelDistEval(boolean b) {
		this.doRelDistEval = b;
	}
	
	public boolean getDoRelDistEval() {
		return this.doRelDistEval;
	}
	
	public void setDoTimeEval(boolean b) {
		this.doTimeEval = b;
	}
	
	public boolean getDoTimeEvel() {
		return this.doTimeEval;
	}
	
	public void setOneR(boolean b) {
		this.oneR = b;
	}
	
	public boolean getOneR() {
		return this.oneR;
	}
	
	public void setFilterBBCF(boolean b) {
		this.filterBBCF = b;
	}
	
	public boolean getFilterBBCF() {
		return this.filterBBCF;
	}
	
	public void setDoPerUserEval(boolean b) {
		this.doPerUserEval = b;
	}
	
	public boolean getDoPerUserEval() {
		return this.doPerUserEval;
	}
	
	@Override
	public void logConfig(Logger logger) {
		logger.info("Seed: {}", String.valueOf(this.seed));
		logger.info("Dataset path: {}", this.dataset);
		logger.info("Number of folds: {}", String.valueOf(this.folds));
		logger.info("List of the configuration files of recommender algorithms to run: {}", this.configs.toString());
		logger.info("Number of runs: {}", this.nbruns);
		logger.info("Like-threshold: {}", this.threshold);
		logger.info("Max number of recommendations to provide: {}", this.r);
		logger.info("Only one R: {}", this.oneR);
		logger.info("Normalize: {}", this.normalize);
		logger.info("Binarize: {}", this.binarize);
		logger.info("Run prediction evaluation: {}", this.doPredEval);
		logger.info("Run IRStats evaluation: {}", this.doIREval);
		logger.info("Run COCLUST evaluation: {}", this.doCoclustEval);
		logger.info("Run relevant items distribution evaluation: {}", this.doRelDistEval);
		logger.info("Run time performance evaluation: {}", this.doTimeEval);
		logger.info("Candidate item selection strategies: {}", this.strategies.toString());
		logger.info("Filter BBCF: {}", this.filterBBCF);
		logger.info("Run per user evaluation: {}", this.doPerUserEval);
	}

}
