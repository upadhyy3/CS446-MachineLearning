% This function computes the label for the given
% feature vector x using the linear separator represented by 
% the weight vector w and the threshold theta.
% YOU NEED TO WRITE THIS FUNCTION.

function y = computeLabel(x, w, theta)

product = w'*x + theta
[m,n] = size(product)
for i=1:n
    value = product(m,i:i)
    if value >= 0
    label(i:i,1) = 1
    else
    label(i:i,1) = -1
    end
end
y=label
end
