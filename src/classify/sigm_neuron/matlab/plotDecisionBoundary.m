function plotDecisionBoundary(theta, X, y)
%   PLOTDECISIONBOUNDARY(theta, X,y)
%   plots output of trained two-class logistic regression classifier
%   with two input variables x1, x2.
%   Also plots training data points.
%   Theta is a parameter vector for logistic regression.
%   X = [x(1); x(2); ...] - training data inputs
%   y = [y(1); y(2); ...] - training data outputs


%3d plot
xl = linspace (30, 100, 100);
yl = linspace (30, 100, 100);

[xx,yy]=meshgrid(xl,yl);
zz = zeros(size(xx));
for i = 1:size(zz,1),
  for j = 1:size(zz,2),
    zz(i,j) =  sigmoid([1, xx(i,j) yy(i,j)] * theta);
  endfor
endfor

figure
hold on
title(['Output of logistic regression'])
meshc(xx,yy, zz);
colorbar
xlabel('x_1')
ylabel('x_2')

% Find Indices of Positive and Negative Examples
pos = find(y==1); neg = find(y == 0);
% Plot Examples
z_scatter = ones(size(X(pos, 1)));
a=scatter3(X(pos, 2), X(pos, 3), z_scatter, 'filled', 'DisplayName','input data (positive class)');
z_scatter = ones(size(X(neg, 1)));
b=scatter3(X(neg, 2), X(neg, 3), z_scatter, 'filled', 'DisplayName','input data (negative class)');

legend

end
