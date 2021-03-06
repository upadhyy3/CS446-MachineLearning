% This function solves the LP problem for a given weight vector
% to find the threshold theta.
% YOU NEED TO FINISH IMPLEMENTATION OF THIS FUNCTION.

function [theta,delta] = findLinearThreshold(data,w)
%% setup linear program
[m, np1] = size(data);
n = np1-1;

% write your code here

%% solve the linear program
%adjust for matlab input: A*x <= b
y = data(1:m,np1:np1)
dummy = y;
%adjusting for matrix product
for j =1:n-1
    dummy = [dummy y];
end

v = ones(m+1,1); 
%equation will be equivalent to (yi*xi) w + (yi) theta + delta >= 1
A = [data(1:m,1:n).*dummy]; 
% A is now (yi*xi)  + (yi) 
A = [A y];
lastRow = zeros(n+1,1);
% las row of the vector will be [0 0 0 0 0 ...1] to tackle the condition
% delta >= 0 that means all the weight vectors are zero:
% (x11y1) w1 + (x12y1)w2.... (xny1) wn + y1 theta + (1) delta
%  .
%  .
%  .
%  (xn1yn) w1 + (xn2yn)wn... (xnnyn)wn + yn theta + (1) delta
%   0 + 0 .........................................+ (1) delta
A = [A;lastRow'];
% apending the coe
A = [A v];
%A = [1 1 1 0; 1 0 1 0 ; 1 1 1 1 ; 0 0 0 0];
b = ones(m,1);
bLastRow = zeros(1,1);
b = [b ;bLastRow];
c = A(m+1:m+1,1:n+2);
[t, z] = linprog(c, -A, -b, [], [], [w' -inf -inf], [w' inf inf]);

%% obtain w,theta,delta from t vector
theta = t(n+1);
delta = t(n+2);

end
