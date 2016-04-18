package cs446.homework2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class Stumps {

//	private static int folds=5;
//	private static long seed = 2;
	private static int sampleSize = 50;
	  static String[] features;
	  private static FastVector zeroOne;
	  private static FastVector labels;
	  private static String filename = "BadgesStump";
static{
	features = new String[100];
	List<String> ff = new ArrayList<String>();
	for(int i=0;i<100;i++)
	{
		ff.add("Stump"+i);
		
	}
	features = ff.toArray(new String[ff.size()]);
	for(int i=0;i<100;i++)
	{
		//System.out.println(features[i]);
		
	}

	zeroOne = new FastVector(2);
	zeroOne.addElement("0");
	zeroOne.addElement("1");

	labels = new FastVector(2);
	labels.addElement("+");
	labels.addElement("-");
}

private static Instances initializeAttributes() {

	String nameOfDataset = "BadgesStump";

	Instances instances;

	FastVector attributes = new FastVector(100);
	for (String featureName : features) {
	    attributes.addElement(new Attribute(featureName, zeroOne));
	 //   attributes.addElement(new Attribute(featureName));
	}
	Attribute classLabel = new Attribute("Class",labels);
	attributes.addElement(classLabel);

	instances = new Instances(nameOfDataset, attributes, 0);

	instances.setClass(classLabel);

	return instances;

 }


public static Instance makeInstance(Instances instances,double[] vector, double classlabel)
{
	String label;	
	
	Instance instance = new Instance(features.length + 1);
	instance.setDataset(instances);
	for (int featureId = 0; featureId < features.length; featureId++) {
	    Attribute att = instances.attribute(features[featureId]);
	    String featureLabel;
	    if (vector[featureId]==1) {
		featureLabel = "1";
	    } 
	    else
	    {
		featureLabel = "0";
	    }
	   instance.setValue(att, featureLabel);
	}
	if(classlabel==1)
	{
		label = "-";
	}
	else
	{
		label = "+";
	}
	instance.setClassValue(label);
	
	return instance;
}
	
public static Instances StumpFeatureGenerator(Id3[] stump,Instances data) throws Exception
	{
		Instances stumpinstances = initializeAttributes();
		double[] instanceVector = new double[100];
		for(int i =0; i<data.numInstances();i++)
		{
			for(int j = 0;j<100;j++)
			{
			instanceVector[j]=stump[j].classifyInstance(data.instance(i));
			}
		Instance stumpinst = makeInstance(stumpinstances,instanceVector, data.instance(i).classValue());
		stumpinstances.add(stumpinst);
		}	
		
		return stumpinstances;
	}
	
	public Instances CreateStumps(Instances data) throws Exception
	{
	Id3[] stumps = new Id3[100];
	data.setClassIndex(data.numAttributes() - 1);
	int trainSize = (int) Math.round(data.numInstances()*sampleSize/100);
	for(int n=0;n<100;n++)
	{
	Random rand = new Random(n);   // create seeded number generator
	Instances randData = new Instances(data); 
	randData.randomize(rand); 
	Instances trainData = new Instances(randData,0,trainSize); 
	Id3 classifier = new Id3();
	classifier.setMaxDepth(4);
	classifier.buildClassifier(trainData);
	stumps[n] = classifier;
//	System.out.println(classifier);
	}
	
	Instances stumpData=StumpFeatureGenerator(stumps,data);
	ArffSaver saver = new ArffSaver();
	saver.setInstances(stumpData);
	//saver.setFile(new File(args[1]));
	saver.setFile(new File(filename));
	saver.writeBatch();
	return stumpData;
    }
	
}
