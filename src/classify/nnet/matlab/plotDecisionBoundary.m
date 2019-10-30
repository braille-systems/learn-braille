function plotDecisionBoundary(Theta1, Theta2, X, y, hidden_layer_size)
%   PLOTDECISIONBOUNDARY(theta, X,y) 
%   plots outputs of two-layer classification neural network
%   with one hidden and one output layer
%   X is a 3*m matrix of training examples, where m is a number of data points
%   y is a vector of labels (0 or 1) for each data point

xl = linspace (-1, 1.5, 100);
yl = linspace (-0.8, 1.2, 100);
[xx,yy]=meshgrid(xl,yl);
zz = zeros(size(xx));
for i = 1:size(zz,1),
  for j = 1:size(zz,2),
    zz(i,j) = predict(Theta1, Theta2, [xx(i,j) yy(i,j)]);
  endfor
endfor

figure
hold on
title(['Output of network (' num2str(hidden_layer_size) ' units in hidden layer)'])
meshc(xx,yy, zz);
colorbar
xlabel('x_1')
ylabel('x_2')

% Find Indices of Positive and Negative Examples
pos = find(y==1); neg = find(y == 0);
% Plot Examples
z_scatter_pos = ones(size(X(pos, 1)));
a=scatter3(X(pos, 1), X(pos, 2), z_scatter_pos, 'filled', 'DisplayName','input data (positive class)');
z_scatter_neg = ones(size(X(neg, 1)));
b=scatter3(X(neg, 1), X(neg, 2), z_scatter_neg, 'filled', 'DisplayName','input data (negative class)');

legend

end
