% This function plots the linear discriminant.
% YOU NEED TO IMPLEMENT THIS FUNCTION

function plot2dSeparator(w, theta)

x = 0:0.1:2;
con = theta/w(1:1)
y = -w(1)*x/w(2)-theta/w(2);
plot(x,y);

end
