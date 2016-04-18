

data = readFeatures('hw1sample2d.txt', 2);
plot2dData(data);
[w,theta,delta] = findLinearDiscriminant(data);
plot2dSeparator(w, theta);