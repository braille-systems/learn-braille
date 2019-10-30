function p = predict(Theta1, Theta2, X)
%PREDICT Predict the label of an input given a trained neural network
% neural network has one hidden layer:
%
% [input] |    [hidden layer]       |      [output layer]->[hypothesis]
%    X    -> *theta1->sigmoid->(A2) -> *theta2->sigmoid->(A3)

m = size(X, 1); %number of traninig examples
X = [ones(m, 1), X]; % add bias (intercept) terms to input data
z2 = X * Theta1'; 
A2 = sigmoid(z2);
m2 = size(A2, 1);
A2 = [ones(m2, 1), A2];
z3 = A2 * Theta2';
A3 = sigmoid(z3); %hypothesis (m x 1)
p = A3;

end
