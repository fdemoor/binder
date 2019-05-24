package binder;

import binder.config.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.yaml.snakeyaml.Yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.COCLUSTRecommender;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluatorKFold;
import org.apache.mahout.cf.taste.impl.common.DataPreprocessing;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;
import org.apache.mahout.cf.taste.impl.eval.AbstractKFoldRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluatorKFold;
import org.apache.mahout.cf.taste.impl.eval.Fold;
import org.apache.mahout.cf.taste.impl.eval.IRStatisticsRelPercentageImpl;
import org.apache.mahout.cf.taste.impl.eval.KFoldDataSplitter;
import org.apache.mahout.cf.taste.impl.eval.KFoldRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.KFoldRecommenderPerUserEvaluator;
import org.apache.mahout.cf.taste.impl.eval.KFoldRecommenderPredictionEvaluator;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.PerUserStatistics;
import org.apache.mahout.cf.taste.eval.PredictionStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.common.RandomUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Evaluator {

	final static String prefix = "src/main/resources/";
	private static Logger logger = LoggerFactory.getLogger(Evaluator.class);
	private static Logger logr = LoggerFactory.getLogger("Pred");
	private static Logger logcoclust = LoggerFactory.getLogger("Coclust");
	private static Logger logir = LoggerFactory.getLogger("IRStats");
	private static Logger logreld = LoggerFactory.getLogger("Reldist");
	private static Logger logt = LoggerFactory.getLogger("Time");
	private static Logger logpu = LoggerFactory.getLogger("PerUser");
	private static Logger logcfg = LoggerFactory.getLogger("Config");

	public static void main(String[] args) {

		String opt = System.getProperty("OPT");
		logcfg.info("Logs are stored in {}", opt);

		String cfgFileName = prefix + "default_config.yml";

		/* Check command line arguments */
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption("c", "config", true, "path of config file, otherwise default used");
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("config")) {
				cfgFileName = line.getOptionValue("config");
			}
		} catch (ParseException exp) {
		}

		/* Load configuration file */
		logcfg.info("Using {} as configuration file", cfgFileName);
		Config cfg = null;
		Yaml yaml = new Yaml();
		try (InputStream in = Files.newInputStream(Paths.get(cfgFileName))) {
			cfg = yaml.loadAs(in, Config.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Couldn't read main configuration file");
			return;
		}
		logcfg.info("=== MAIN CONFIGURATION ===");
		cfg.logConfig(logcfg);

		RandomUtils.useTestSeed();

		if (!cfg.getDoPredEval() && !cfg.getDoIREval() && !cfg.getDoCoclustEval() && !cfg.getDoRelDistEval()
				&& !cfg.getDoTimeEvel() && !cfg.getDoPerUserEval()) {
			logger.info("Nothing to do");
			return;
		}

		/* Load dataset */
		logger.info("Loading dataset");
		DataModel model = null;
		try {
			model = new FileDataModel(new File(cfg.getDataset()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Couldn't read dataset");
			return;
		}
		if (cfg.getNormalize()) {
			try {
				logger.info("Normalizing dataset");
				model = DataPreprocessing.normalize(model);
			} catch (TasteException e) {
				e.printStackTrace();
				logger.error("Couldn't normalize dataset");
				return;
			}
		}
		if (cfg.getBinarize()) {
			try {
				logger.info("Binarizing dataset");
				model = DataPreprocessing.binarize(model, cfg.getThreshold());
			} catch (TasteException e) {
				e.printStackTrace();
				logger.error("Couldn't binarize dataset");
				return;
			}
		}
		logger.info("Done with dataset");

		/* Read configuration files of all recommender algorithms specified */
		HashMap<String, AbstractConfig> configs = new HashMap<String, AbstractConfig>(cfg.getConfigs().size());
		for (String s : cfg.getConfigs()) {
			AbstractConfig c = null;
			Yaml yml = new Yaml();
			if (s.equals("random")) {
				c = new RandomConfig();
			} else if (s.equals("itemavg")) {
				c = new ItemAvgConfig();
			} else if (s.equals("itemuseravg")) {
				c = new ItemUserAvgConfig();
			} else {
				try (InputStream in = Files.newInputStream(Paths.get(prefix + s))) {
					if (s.contains("ubknn")) {
						c = yml.loadAs(in, UBKNNConfig.class);
					} else if (s.contains("ibknn")) {
						c = yml.loadAs(in, IBKNNConfig.class);
					} else if (s.contains("mf")) {
						c = yml.loadAs(in, MFConfig.class);
					} else if (s.contains("coclust")) {
						c = yml.loadAs(in, COCLUSTConfig.class);
					} else if (s.contains("nbcf")) {
						c = yml.loadAs(in, NBCFConfig.class);
					} else if (s.contains("bbcf")) {
						c = yml.loadAs(in, BBCFConfig.class);
					} else if (s.contains("bicainet")) {
						c = yml.loadAs(in, BicaiNetConfig.class);
					} else if (s.contains("bcn")) {
						c = yml.loadAs(in, BCNConfig.class);
					} else {
						logger.error("Unrecognized algorithm");
						return;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error("Couldn't read specific configuration file {}", s);
					return;
				}
			}
			configs.put(s, c);
		}

		/* Run the evaluation */

		if (cfg.getDoCoclustEval()) {
			try {
				runCoclustIterEval(model, cfg);
			} catch (TasteException e) {
				e.printStackTrace();
				logger.error("Error during evaluation");
				return;
			}
		}

		if (cfg.getDoRelDistEval()) {
			try {
				for (String strategy : cfg.getStrategies()) {
					runRelevantDistEval(model, cfg, strategy, cfg.getThreshold());
				}
			} catch (TasteException e) {
				e.printStackTrace();
				logger.error("Error during evaluation");
				return;
			}
		}

		try {
			KFoldRecommenderPredictionEvaluator evaluatorPred = new KFoldRecommenderPredictionEvaluator(model,
					cfg.getFolds());
			KFoldRecommenderIRStatsEvaluator evaluatorIRStats = new KFoldRecommenderIRStatsEvaluator(model,
					cfg.getFolds());
			KFoldRecommenderPerUserEvaluator evaluatorPerUser = new KFoldRecommenderPerUserEvaluator(model,
					cfg.getFolds());

			if (cfg.getFilterBBCF()) {
				BBCFConfig cc = new BBCFConfig();
				cc.setBiclustering("qubic");
				cc.setConsistency((float) 0.95);
				cc.setSize(100);
				cc.setOverlap(1);
				cc.setK(20);
				evaluatorIRStats.restrainUserIDsWithCoverage(
						new BinderRecommenderBuilder(cc, "trainingitems", cfg.getThreshold()), cfg.getR());
			}

			Iterator<Entry<String, AbstractConfig>> it = configs.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, AbstractConfig> pair = it.next();
				logcfg.info("=== SPECIFIC CONFIGURATION #{} ===", pair.getKey());
				AbstractConfig c = (AbstractConfig) pair.getValue();
				c.logConfig(logcfg);
				String name = pair.getKey();
				for (String strategy : cfg.getStrategies()) {
					RecommenderBuilder builder = new BinderRecommenderBuilder(c, strategy, cfg.getThreshold());
					if (cfg.getDoPredEval()) {
						runPredEval(evaluatorPred, builder, cfg, name);
					}
					if (cfg.getDoIREval()) {
						runIREval(evaluatorIRStats, builder, model, cfg, name,
								strategy + String.valueOf(cfg.getThreshold()));
					}
					if (cfg.getDoTimeEvel()) {
						runTimeEval(builder, model, cfg, name, strategy + String.valueOf(cfg.getThreshold()));
					}
					if (cfg.getDoPerUserEval()) {
						runPerUserEval(evaluatorPerUser, builder, cfg, name,
								strategy + String.valueOf(cfg.getThreshold()));
					}
				}
			}
		} catch (TasteException e) {
			e.printStackTrace();
			logger.error("Error during evaluation");
			return;
		}
	}

	private static void runPredEval(KFoldRecommenderPredictionEvaluator evaluator, RecommenderBuilder builder,
			Config cfg, String name) throws TasteException {
		for (int k = 0; k < cfg.getNruns(); k++) {
			PredictionStatistics results = evaluator.evaluate(builder);
			logr.info("{},{},{},{}", name, results.getRMSE(), results.getMAE(), results.getNoEstimate());
		}
	}

	private static void runPerUserEval(KFoldRecommenderPerUserEvaluator evaluator, RecommenderBuilder builder,
			Config cfg, String name, String strategy) throws TasteException {
		for (int k = 0; k < cfg.getNruns(); k++) {
			PerUserStatistics results = evaluator.evaluate(builder, cfg.getR(), (double) cfg.getThreshold());
			LongPrimitiveIterator it = results.getUserIDs();
			while (it.hasNext()) {
				long userID = it.nextLong();
				logpu.info("{},{},{},{},{},{},{},{},{}", userID, name, strategy, cfg.getR(), results.getRMSE(userID),
						results.getMAE(userID), results.getPrecision(userID), results.getRecall(userID),
						results.getNormalizedDiscountedCumulativeGain(userID));
			}

		}
	}

	private static void runIREval(KFoldRecommenderIRStatsEvaluator evaluatorIRStats, RecommenderBuilder builder,
			DataModel model, Config cfg, String name, String strategy) throws TasteException {
		IRStatistics irstats = null;
		for (int k = 0; k < cfg.getNruns(); k++) {
			for (int R = cfg.getOneR() ? cfg.getR() : 5; R <= cfg.getR(); R += 5) {
				irstats = evaluatorIRStats.evaluate(builder, R, cfg.getBinarize() ? 1 : cfg.getThreshold());
				logir.info("{},{},{},{},{},{},{},{},{},{},{},-1,{},{}", name, R, irstats.getPrecision(),
						irstats.getRecall(), irstats.getF1Measure(), irstats.getReachAtLeastOne(),
						irstats.getNormalizedDiscountedCumulativeGain(), irstats.getFallOut(), irstats.getReachAll(),
						irstats.getItemCoverage(), strategy, irstats.getPerPrecision(), irstats.getPerRecall());
//				if (irstats instanceof IRStatisticsRelPercentageImpl) {
//					IRStatisticsRelPercentageImpl irstatsRel = (IRStatisticsRelPercentageImpl) irstats;
//					for (int group = 0; group < 11; group++) {
//						IRStatistics irstats2 = irstatsRel.getGroupedResults(group);
//						logir.info("{},{},{},{},{},{},{},{},{},{},{},{},{},{}", name, R, irstats2.getPrecision(),
//								irstats2.getRecall(), irstats2.getF1Measure(), irstats2.getReachAtLeastOne(),
//								irstats2.getNormalizedDiscountedCumulativeGain(), irstats2.getFallOut(),
//								irstats2.getReachAll(), irstats2.getItemCoverage(), strategy, group,
//								irstats2.getPerPrecision(), irstats2.getPerRecall());
//					}
//				}
			}
		}
	}

	private static void runCoclustIterEval(DataModel model, Config cfg) throws TasteException {
		KFoldRecommenderPredictionEvaluator evaluator = new KFoldRecommenderPredictionEvaluator(model, cfg.getFolds());
//		int maxIter = 201;
		for (int k = 0; k < cfg.getNruns(); k++) {
//			for (int iter = 5; iter < maxIter; iter += 10) {
			List<Integer> values = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 5, 10, 15, 20));
			for (int i : values) {
				for (int j : values) {
					COCLUSTRecommenderBuilder builder = new COCLUSTRecommenderBuilder(i, j, 30);
					PredictionStatistics r = evaluator.evaluate(builder);
					logcoclust.info("{},{},{},{},{},{}", i, j, r.getRMSE(), r.getMAE(), r.getNoEstimate(),
							r.getMoreInfo());
				}
			}
		}

	}

	private static void runRelevantDistEval(DataModel model, Config cfg, String strategy, float threshold)
			throws TasteException {
		KFoldDataSplitter folds = new KFoldDataSplitter(model, cfg.getFolds());
		RecommenderBuilder builder = new BinderRecommenderBuilder(new RandomConfig(), strategy, cfg.getThreshold());

		Iterator<Fold> itF = folds.getFolds();
		while (itF.hasNext()) {

			Fold fold = itF.next();
			FastByIDMap<PreferenceArray> testPrefs = fold.getTesting();
			DataModel trainingModel = fold.getTraining();
			Recommender recommender = builder.buildRecommender(trainingModel, fold);

			LongPrimitiveIterator it = model.getUserIDs();
			while (it.hasNext()) {

				long userID = it.nextLong();
				double per;
				FastIDSet candidateItemsIDs = recommender.getCandidateItems(userID);
				int numCandidateItems = candidateItemsIDs.size();
				if (numCandidateItems == 0) {
					continue;
				}
				PreferenceArray prefs = testPrefs.get(userID);
				if (prefs == null) {
					per = 0;
				} else {
					FastIDSet relevantItemIDs = new FastIDSet(prefs.length());
					for (int i = 0; i < prefs.length(); i++) {
						if (prefs.getValue(i) >= cfg.getThreshold() && candidateItemsIDs.contains(prefs.getItemID(i))) {
							relevantItemIDs.add(prefs.getItemID(i));
						}
					}
					int numRelevantItems = relevantItemIDs.size();
					per = (double) numRelevantItems / (double) numCandidateItems;
				}
				logreld.info("{},{}", per, strategy + String.valueOf(threshold));
			}

		}
	}

	static class TimeFunc {
		static long call() {
			return System.nanoTime();
//	    	return System.currentTimeMillis();
		}
	}

	private static void runTimeEval(RecommenderBuilder builder, DataModel model, Config cfg, String name,
			String strategy) throws TasteException {

		for (int k = 0; k < cfg.getNruns(); k++) {
			KFoldDataSplitter folds = new KFoldDataSplitter(model, cfg.getFolds());
			for (int R = cfg.getOneR() ? cfg.getR() : 5; R <= cfg.getR(); R += 5) {
				RunningAverage time = new FullRunningAverage();
				Iterator<Fold> itF = folds.getFolds();
				while (itF.hasNext()) {

					Fold fold = itF.next();
					DataModel trainingModel = fold.getTraining();
					long t1, t2;
					long sum = 0;

					t1 = TimeFunc.call();
					Recommender recommender = builder.buildRecommender(trainingModel, fold);
					t2 = TimeFunc.call();
					sum += t2 - t1;

					LongPrimitiveIterator it = model.getUserIDs();
					while (it.hasNext()) {
						long userID = it.nextLong();
						t1 = TimeFunc.call();
						recommender.recommend(userID, R);
						t2 = TimeFunc.call();
						sum += t2 - t1;
					}
					time.addDatum(sum);
				}
				logt.info("{},{},{},{}", name, R, time.getAverage(), strategy);
			}
		}
	}
}
