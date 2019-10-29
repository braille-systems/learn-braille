
%% ============ Load Data ===================
%  The first two columns (x1, x2) -  input vector 
%  The third column (y) contains the label (0 or 1).
data = load('data.txt');
X = data(:, [1, 2]); y = data(:, 3);

%% ============ Setup ============
% m - number of training examples, n - dimension of input vector
[m, n] = size(X); 
% Add intercept term to X
X = [ones(m, 1) X];
% Initialize fitting parameters
initial_theta = zeros(n + 1, 1);

%% ============= Optimizing using built-in function 'fminunc'  =============
options = optimset('GradObj', 'on', 'MaxIter', 400);
[theta, cost] = fminunc(@(t)(costFunction(t, X, y)), initial_theta, options);

fprintf('Cost at theta found by fminunc: %f\n', cost);
fprintf('theta: \n');
fprintf(' %f \n', theta);
plotDecisionBoundary(theta, X, y);


%% ============== Computing accuracy on training set ==============

p = predict(theta, X);
fprintf('Train Accuracy: %f\n', mean(double(p == y)));
