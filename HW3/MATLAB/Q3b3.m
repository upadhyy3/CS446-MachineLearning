data = readFeatures('hw1sample2d.txt', 2);
[w,theta,delta] = findLinearDiscriminant(data);
x = [data(1:4,1:2)]
computeLabel(x, w, theta)