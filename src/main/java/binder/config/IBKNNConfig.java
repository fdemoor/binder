package binder.config;

import org.slf4j.Logger;

public class IBKNNConfig implements AbstractConfig {
	
	private int k; /* Number of nearest neighbors as in kNN */
	private String similarity; /* Similarity metric */
	
	public void setK(int kk) {
		this.k = kk;
	}
	
	public int getK() {
		return this.k;
	}
	
	public void setSimilarity(String s) {
		this.similarity = s;
	}
	
	public String getSimilarity() {
		return this.similarity;
	}
	
	@Override
	public void logConfig(Logger logger) {
		logger.info("IBKNN");
		logger.info("Number of nearest neighbors: {}", String.valueOf(this.k));
		logger.info("Similarity metric: {}", this.similarity);
	}

}
