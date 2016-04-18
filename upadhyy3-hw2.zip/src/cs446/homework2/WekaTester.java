package cs446.homework2;

import java.io.File;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.core.Instances;
import cs446.homework2.Id3;
import cs446.homework2.SGD;

public class WekaTester {
	
	private static double alphaForSGDFeatures = 0.001;
	private static double differenceForSGDFeatures = 1E-5 ;
	private static double alphaForSGDStump = 0.04;
	private static double differenceForSGDStump = 1E-11 ;
	
	public static void StumFeatureGenerator(Id3[] stump,Instances data) throws Exception
	{
		for(int i =0; i<data.numInstances();i++)
		{
			System.out.println(stump[i].classifyInstance(data.instance(i)));
		}	
	}

    public static void main(String[] args) throws Exception {

//	if (args.length != 1) {
//	    System.err.println("Usage: WekaTester arff-file");
//	    System.exit(-1);
//	}

	// Load the data
//	String filepath = "C:\\Users\\admin\\CS446\\workspace\\HW2\\BadgesFeatures";
    String filepath = "BadgesFeatures";
	Instances data = new Instances(new FileReader(new File(filepath)));
	int folds=5;
	long seed = 1;
	// Randomizing the data once
	Random rand = new Random(seed);   // create seeded number generator
	Instances randData = new Instances(data);   // create copy of original data
	randData.randomize(rand);         // randomize data with number generator
	data.setClassIndex(data.numAttributes() - 1);
	randData.setClassIndex(randData.numAttributes()-1);
	//Iterating through all the folds and evaluating for that particular split for test and train data
	for(int n=0;n<folds;n++)
	{
	//Training and testing data
	Instances train = randData.trainCV(folds,n);
	Instances test = randData.testCV(folds, n);
	
	//Id3 Classifier with max depth
	Id3 classifierMax = new Id3();
	classifierMax.setMaxDepth(-1);
	classifierMax.buildClassifier(train);
	Evaluation evaluationId3Max = new Evaluation(test);
	evaluationId3Max.evaluateModel(classifierMax, test);
	System.out.println("------------------------------------");
	System.out.println("$$$$$$ID3 Evaluation for full tree$$$$$$");
	System.out.println(evaluationId3Max.toSummaryString());
	System.out.println(evaluationId3Max.toMatrixString());
	System.out.println("------------------------------------");

	
//Id3 Classifier with max depth 4
	Id3 classifier = new Id3();
	classifier.setMaxDepth(4);
	classifier.buildClassifier(train);
	Evaluation evaluationId3Pruned = new Evaluation(test);
	evaluationId3Pruned.evaluateModel(classifier, test);
	System.out.println("------------------------------------");
	System.out.println("$$$$$$ID3 EVALUATION MAX DEPTH 4$$$$$$");
	System.out.println(evaluationId3Pruned.toSummaryString());
	System.out.println(evaluationId3Pruned.toMatrixString());
	System.out.println("------------------------------------");

	
	
//	Id3 Max depth 8
	classifier.setMaxDepth(8);
	classifier.buildClassifier(train);
	Evaluation evaluationId3 = new Evaluation(test);
	evaluationId3.evaluateModel(classifier, test);
	if(n==4)
	System.out.println("------------------------------------");
	System.out.println("$$$$$$ID3 EVALUATION MAX DEPTH 8$$$$$$");
	System.out.println(evaluationId3.toSummaryString());
	System.out.println(evaluationId3.toMatrixString());
	System.out.println("------------------------------------");
	
//Evaluation of SGD on features
	SGD sgdClassifier = new SGD(train.numAttributes()-1,alphaForSGDFeatures,differenceForSGDFeatures);
	sgdClassifier.buildClassifier(train);
	Evaluation evaluationSGD = new Evaluation(test);
	evaluationSGD.evaluateModel(sgdClassifier, test);
	System.out.println("------------------------------------");
	System.out.println("$$$$$$SGD ON Features EVALUATION$$$$$$");
	System.out.println(evaluationSGD.toSummaryString());
	System.out.println(evaluationSGD.toMatrixString());
	System.out.println("------------------------------------");
//	
// Evaluation on Stumps using SGD	
	Stumps st = new Stumps();
	Instances stumptrainData=st.CreateStumps(train);
	Instances stumptestData=st.CreateStumps(test);
	SGD sgdClassifierStumps = new SGD(stumptrainData.numAttributes()-1,alphaForSGDStump,differenceForSGDStump);
	sgdClassifierStumps.buildClassifier(stumptrainData);
	Evaluation evaluationSGDStumps = new Evaluation(stumptestData);
	evaluationSGDStumps.evaluateModel(sgdClassifierStumps, stumptestData);
	System.out.println("------------------------------------");
	System.out.println("$$$$$$SGD Stumps EVALUATION$$$$$$");
	System.out.println(evaluationSGDStumps.toSummaryString());
	System.out.println(evaluationSGDStumps.toMatrixString());
	System.out.println("------------------------------------");
	System.out.println("$$$$$$$$END OF FOLD$$$$$$$$$");
	}

	}
    
}

