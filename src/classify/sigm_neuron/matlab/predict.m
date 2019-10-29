function p = predict(theta, X)
%   p = PREDICT(theta, X) computes the labels for X using a 
%   threshold at 0.5 (i.e., if sigmoid(theta'*x) >= 0.5, predict 1)

p = sigmoid(X * theta); % hypothesis vector
p = (p >= 0.5);

end
