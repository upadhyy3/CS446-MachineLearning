package cs446.homework2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;


// author Shivam Upadhyay
public class FeatureGenerator {

    static String[] features;
    static String[] additionalFeatures;
    private static FastVector zeroOne;
    private static FastVector labels;

    static {
	features = new String[] { "firstName0","firstName1","firstName2","firstName3","firstName4","lastName0","lastName1","lastName2","lastName3","lastName4"}; //"firstName1"
	additionalFeatures = new String[] {"lengthoffirstname","Lengthof2ndname","SecondLetterAVowel(1)","SecondLetterAVowel(2)"};
	List<String> ff = new ArrayList<String>();

	for (String f : features) {
	    for (char letter = 'a'; letter <= 'z'; letter++) {
		ff.add(f + "=" + letter);
	    }
	}

	features = ff.toArray(new String[ff.size()]);

	zeroOne = new FastVector(2);
	zeroOne.addElement("0");
	zeroOne.addElement("1");

	labels = new FastVector(2);
	labels.addElement("+");
	labels.addElement("-");
    }

    public static Instances readData(String fileName) throws Exception {

	Instances instances = initializeAttributes();
	Scanner scanner = new Scanner(new File(fileName));

	while (scanner.hasNextLine()) {
	    String line = scanner.nextLine();

	    Instance instance = makeInstance(instances, line);

	    instances.add(instance);
	}

	scanner.close();

	return instances;
    }

    private static Instances initializeAttributes() {

	String nameOfDataset = "Badges";

	Instances instances;

	FastVector attributes = new FastVector(14);
	for (String featureName : features) {
	    attributes.addElement(new Attribute(featureName, zeroOne));
	 //   attributes.addElement(new Attribute(featureName));
	}
	for (String featureName : additionalFeatures) {
	    attributes.addElement(new Attribute(featureName, zeroOne));
	 //   attributes.addElement(new Attribute(featureName));
	}
	
	Attribute classLabel = new Attribute("Class",labels);
	attributes.addElement(classLabel);

	instances = new Instances(nameOfDataset, attributes, 0);

	instances.setClass(classLabel);

	return instances;

    }

    private static Instance makeInstance(Instances instances, String inputLine) {
	inputLine = inputLine.trim();

	String[] parts = inputLine.split("\\s+");
	String label = parts[0];
	String firstName = parts[1].toLowerCase();
	String lastName = parts[2].toLowerCase();
	
//	System.out.println(lastName);
//	System.out.println(firstName);
	Instance instance = new Instance(features.length + additionalFeatures.length+1);
	instance.setDataset(instances);
	Set<String> feats = new HashSet<String>();
	if(firstName.length()>=5)
	{
	for(int i=0;i<5;i++)
	{
	feats.add("firstName"+i+"=" + firstName.charAt(i));
	}
	}
	else
	{	int i =0 ;
	
		for(i=0;i<firstName.length();i++)
		{
		feats.add("firstName"+i+"=" + firstName.charAt(i));
		}
		while(i<5)
		{
			feats.add("firstName"+i+"=" + " ");
			i++;
		}
	}
	if(lastName.length()>=5)
	{
	for(int i=0;i<5;i++)
	{
	feats.add("lastName"+i+"=" + lastName.charAt(i));
	}
	}
	else
	{	int i =0 ;
	
		for(i=0;i<lastName.length();i++)
		{
		feats.add("lastName"+i+"=" + lastName.charAt(i));
		}
		while(i<5)
		{
			feats.add("lastName"+i+"=" + " ");
			i++;
		}
	}
	//loop for extracting the minimal features
	for (int featureId = 0; featureId < features.length; featureId++) {
	    Attribute att = instances.attribute(features[featureId]);

	    String name = att.name();
	    String featureLabel;
	    if (feats.contains(name)) {
		featureLabel = "1";
	   // instance.setValue(att, 1);
	    } 
	    else
	    {
		featureLabel = "0";
	    }
	    
	   instance.setValue(att, featureLabel);
	}
// Creating attributes for the additional features Length feature, Nth position as vowel
    Attribute firstNameLength = instances.attribute(additionalFeatures[0]);
    Attribute lastNameLength = instances.attribute(additionalFeatures[1]);
    Attribute firstVowel = instances.attribute(additionalFeatures[2]);
    Attribute lastVowel = instances.attribute(additionalFeatures[3]);
    String featureLabelfirst;
    String featureLabellast;
	String firstNameVowel ;
	String secondNameVowel;
	
	if(firstName.length() > 5 && lastName.length()>5)
	{
		featureLabelfirst="1";
		featureLabellast = "1";
		instance.setValue(firstNameLength, featureLabelfirst);
		instance.setValue(lastNameLength, featureLabellast);
		
	}
	else if(firstName.length() > 5 )
	{
		featureLabelfirst="1";
		featureLabellast = "0";
		instance.setValue(firstNameLength, featureLabelfirst);
		instance.setValue(lastNameLength, featureLabellast);
	}
	else if(lastName.length() > 5 )
	{
		featureLabelfirst="0";
		featureLabellast = "1";
		instance.setValue(firstNameLength, featureLabelfirst);
		instance.setValue(lastNameLength, featureLabellast);
	}
	else
	{
		featureLabelfirst="0";
		featureLabellast = "0";
		instance.setValue(firstNameLength, featureLabelfirst);
		instance.setValue(lastNameLength, featureLabellast);
	}
	List<Character> list = new ArrayList<Character>();
	list.add('a');
	list.add('e');
	list.add('i');
	list.add('o');
	list.add('u');
	
	
	
	if(firstName.length()>1 && list.contains(firstName.charAt(1)))
	{
		firstNameVowel = "1";
		instance.setValue(firstVowel, firstNameVowel);
	}
	else{
		firstNameVowel = "0";
		instance.setValue(firstVowel, firstNameVowel);	
	}
	
	if(lastName.length()>1 && list.contains(lastName.charAt(1)))
	{
		secondNameVowel = "1";
		instance.setValue(lastVowel, secondNameVowel);
	}
	else{
		secondNameVowel = "0";
		instance.setValue(lastVowel, secondNameVowel);
	}
	instance.setClassValue(label);

	return instance;
    }

    public static void main(String[] args) throws Exception {

//	if (args.length != 2) {
//	    System.err
//		    .println("Usage: FeatureGenerator input-badges-file features-file");
//	    System.exit(-1);
//	}
    String filepath = "badges.modified.data.all";
    String filename = "BadgesFeatures";
//	Instances data = readData(args[0]);
	Instances data = readData(filepath);
	ArffSaver saver = new ArffSaver();
	saver.setInstances(data);
	//saver.setFile(new File(args[1]));
	saver.setFile(new File(filename));
	saver.writeBatch();
    }
}
