package binder;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.impl.eval.Fold;
import org.apache.mahout.cf.taste.impl.recommender.COCLUSTRecommender;

public class COCLUSTRecommenderBuilder implements RecommenderBuilder {
	
	private final int k;
	private final int l;
	private final int iter;
	
	COCLUSTRecommenderBuilder(int k, int l, int iter) throws TasteException {
		this.k = k;
		this.l = l;
		this.iter = iter;
	}
	
	@Override
	public Recommender buildRecommender(DataModel dataModel) throws TasteException {
		return new COCLUSTRecommender(dataModel, this.k, this.l, this.iter);
	}

	@Override
	public Recommender buildRecommender(DataModel dataModel, Fold f) throws TasteException {
		return buildRecommender(dataModel);
	}

}
