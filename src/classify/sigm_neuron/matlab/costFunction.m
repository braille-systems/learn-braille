function [J, grad] = costFunction(theta, X, y)
%COSTFUNCTION Compute cost and gradient for logistic regression

m = length(y); % number of training examples
h = sigmoid(X* theta); %hypothesis
J = -1./m  * sum(y .* log(h) + (1-y) .* log(1-h)); % cost function
grad = 1./m .* X'*(h-y); % gradients

end
