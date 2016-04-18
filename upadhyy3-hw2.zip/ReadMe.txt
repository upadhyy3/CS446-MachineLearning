Name : Shivam Upadhyay
Email: upadhyy3@illinois.edu

SGD.java : Has the implementation of SGD
FeatureGenerator.java: Has the implementation of Feature vector generation
Stumps.java: Has the implementation of generation of Decision Stumps
WekaTester.java : is calling all the algorithms and performing the cross validation 

1)To run the application first run the FeatureGenerator.java which creates the "BadgesFeatures.arff" from "badges.modified.data.all" which should be present in the same folder as src folder is present

2) Then run the wekaTester.java which takes "BadgesFeatures.arff" as input (Should be present in the same folder as src folder) and displays the ouput of 5 fold validation