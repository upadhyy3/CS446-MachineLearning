package cs446.homework2;

import weka.classifiers.Classifier;
//import weka.classifiers.Sourcable;
//import weka.classifiers.UpdateableClassifier;
//import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.NoSupportForMissingValuesException;
import weka.core.Capabilities.Capability;
import java.util.Arrays;


public class SGD extends Classifier{

	/**
	 * 
	 */
    /** Class attribute of dataset. */
 //   private Attribute m_ClassAttribute;
	private double alpha;
	private double[] theta;
	private double[] thetabefore;
	private double difference ;
//	  private double m_ClassValue;
	private int numberOfIterations = 1500;
	private double label;
	private static final long serialVersionUID = -5313505361123620399L;

	public SGD (int noOfAttributes,double alpha,double difference)
	{
		this.difference = difference;
		this.alpha = alpha;
		theta = new double[noOfAttributes];
		thetabefore = new double[noOfAttributes];
		//Random rand = new Random();
		Arrays.fill(theta, 0);
		Arrays.fill(thetabefore, 0);
	}
	
    public Capabilities getCapabilities() {
	Capabilities result = super.getCapabilities();
	result.disableAll();

	// attributes
	result.enable(Capability.NOMINAL_ATTRIBUTES);

	// class
	result.enable(Capability.NOMINAL_CLASS);
	result.enable(Capability.MISSING_CLASS_VALUES);

	// instances
	result.setMinimumNumberInstances(0);

	return result;
    }
	public void print()
	{
		for(int i =0;i<theta.length;i++)
		{
		System.out.println(theta[i]);
		}
	}
	@Override
	public void buildClassifier(Instances data) throws Exception {
		// TODO Auto-generated method stub
		// can classifier handle the data?
		getCapabilities().testWithFail(data);

		// remove instances with missing class
		data = new Instances(data);
		data.deleteWithMissingClass();
		updateClassifier(data);
		
	}
	public double dotproduct(double[] t, Instance temp)
	{
		double result = 0;
		for(int i=0;i<temp.numAttributes()-1;i++)
		{//System.out.println(temp.value(i));
			result=result+t[i]*temp.value(i);
		}
		//System.out.println(result);
		return result;
	}
	public double classify(double[] t, Instance temp)
	{
		double result = 0;
		for(int i=0;i<temp.numAttributes()-1;i++)
		{
			result=result+t[i]*temp.value(i);
		}
		return result;
	}
	//Computes the L2 norm of error for exit criteria
	public double difference(double[] theta,double[] thetabefore)
	{
		double result = 0;
		for(int i=0;i<theta.length-1;i++)
		{//System.out.println(temp.value(i));
			result+=Math.pow(theta[i]-thetabefore[i],2);
		}
		
		//System.out.println(result);
		return (result/2);
	}
	public void updateClassifier(Instances data) throws Exception
	{
//		System.out.println(current);
	//	System.out.println(current.value(current.numAttributes()));

		outerloop:
		for(int j=0 ; j<numberOfIterations;j++)
		{
			for(int k=0;k<data.numInstances();k++)
			{
				
				Instance current = data.instance(k);
				if(current.value(current.numAttributes()-1)==1.0)
				 {
					label = -1.0;
				 }
				else
				 {
					label = 1.0;
				 }
				for(int i =0;i<current.numAttributes()-1;i++)
				{

					theta[i] = theta[i] + alpha*(label-dotproduct(theta,current))*current.value(i);
					
				}
			}
			if (difference(theta,thetabefore)<difference)
			{
				System.out.println("Broke after:");
				System.out.println(j);
				break outerloop;
			}
			thetabefore=Arrays.copyOf(theta, theta.length);
			}
	}
	
    /**
     * Classifies a given test instance using the decision tree.
     * 
     * @param instance
     *            the instance to be classified
     * @return the classification
     * @throws NoSupportForMissingValuesException
     *             if instance has missing values
     */
    public double classifyInstance(Instance instance)
	    throws NoSupportForMissingValuesException {

	if (instance.hasMissingValue()) {
	    throw new NoSupportForMissingValuesException(
		    "SGD: no missing values, " + "please.");
	}
	if (dotproduct(theta,instance)>0) {
	    return 0;
	} else {
	    return 1;
	}
    }

}


