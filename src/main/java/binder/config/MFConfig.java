package binder.config;

import org.slf4j.Logger;

public class MFConfig implements AbstractConfig {
	
	private String factorizer; /* Algorithm for matrix factorization */
	private int nbfeatures; /* Number of features */
	private double lambda; /* Regularization parameter */
	private int nbiterations; /* Number of iterations */
	
	public void setFactorizer(String s) {
		this.factorizer = s;
	}
	
	public String getFactorizer() {
		return this.factorizer;
	}
	
	public void setNbfeatures(int n) {
		this.nbfeatures = n;
	}
	
	public int getNbfeatures() {
		return this.nbfeatures;
	}
	
	public void setLambda(double x) {
		this.lambda = x;
	}
	
	public double getLambda() {
		return this.lambda;
	}
	
	public void setNbiterations(int n) {
		this.nbiterations = n;
	}
	
	public int getNbiterations() {
		return this.nbiterations;
	}
	
	@Override
	public void logConfig(Logger logger) {
		logger.info("Matrix Factorization");
		logger.info("Factorizer algorithm: {}", this.factorizer);
		logger.info("Number of features: {}", this.nbfeatures);
		logger.info("Regularization parameter (lambda): {}", this.lambda);
		logger.info("Number of iterations: {}", this.nbiterations);
	}

}
