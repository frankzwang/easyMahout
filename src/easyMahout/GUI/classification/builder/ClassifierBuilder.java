package easyMahout.GUI.classification.builder;

import org.apache.log4j.Logger;
import org.apache.mahout.classifier.df.mapreduce.Classifier;

import easyMahout.GUI.MainGUI;
import easyMahout.utils.Constants;

public class ClassifierBuilder {

	private final static Logger log = Logger.getLogger(ClassifierBuilder.class);

	private static String algorithm;
	private static int numCharacteristics;
	
	public static Classifier buildClassifier(){
		/*if (TypeRecommenderPanel.getSelectedType().equals(Constants.RecommType.USERBASED)) {
			DataModel model = DataModelRecommenderPanel.getDataModel();
			if (model != null) {
				UserSimilarity similarity = SimilarityRecommenderPanel.getUserSimilarity(model);
				UserNeighborhood neighborhood = NeighborhoodRecommenderPanel.getNeighborhood(similarity, model);
				return new GenericUserBasedRecommender(model, neighborhood, similarity);
			} else {
				log.error("Trying to run a recommender without datamodel loaded");
				MainGUI.writeResult("Trying to run a recommender without a dataModel loaded.", Constants.Log.ERROR);
				return null;
			}

		} else if (TypeRecommenderPanel.getSelectedType().equals(Constants.RecommType.ITEMBASED)) {
			DataModel model = DataModelRecommenderPanel.getDataModel();
			if (model != null) {
				ItemSimilarity similarity = SimilarityRecommenderPanel.getItemSimilarity(model);
				return new GenericItemBasedRecommender(model, similarity);
			} else {
				log.error("Trying to run a recommender without datamodel loaded");
				MainGUI.writeResult("Trying to run a recommender without a dataModel loaded.", Constants.Log.ERROR);
				return null;
			}

		} else if (TypeRecommenderPanel.getSelectedType().equals(Constants.RecommType.FACTORIZED_RECOMMENDER)) {
			DataModel model = DataModelRecommenderPanel.getDataModel();
			if (model != null) {				
				Factorizer factorizer = FactorizerRecommenderPanel.getFactorizer();
				if (factorizer != null) {
					CandidateItemsStrategy candidate = FactorizerRecommenderPanel.getCandidate();
					if (candidate != null) {
						try {
							return new SVDRecommender(model, factorizer, candidate);
						} catch (TasteException e) {
							// TODO Auto-generated catch block
							log.error("Error building the recommender.");
							MainGUI.writeResult("Error building the recommender.", Constants.Log.ERROR);
							e.printStackTrace();							
						}
					}
				} else {
					// TODO change error messages
					log.error("Factorizer couldn't be built successfully.");
					MainGUI.writeResult("Factorizer couldn't be built successfully.", Constants.Log.ERROR);
					return null;
				}
			} else {
				log.error("Trying to run a recommender without datamodel loaded");
				MainGUI.writeResult("Trying to run a recommender without a dataModel loaded.", Constants.Log.ERROR);
				return null;
			}

		} else {
			return null;
		}*/
		return null;		
	}	
}
