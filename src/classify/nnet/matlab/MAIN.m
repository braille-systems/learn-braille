
%% ============ Load Data ===================
%  The first two columns (x1, x2) -  input vector 
%  The third column (y) contains the label (0 or 1).
data = load('data2.txt');
X = data(:, [1, 2]); y = data(:, 3);

%% ============ Setup ============
% m - number of training examples, n - dimension of input vector
[m, n] = size(X); 
input_layer_size  = n;
hidden_layer_size = 5;
num_labels = 1;
initial_Theta1 = randInitializeWeights(input_layer_size, hidden_layer_size);
initial_Theta2 = randInitializeWeights(hidden_layer_size, num_labels);

initial_nn_params = [initial_Theta1(:) ; initial_Theta2(:)];
								   
%%  ============ Train neural network ============
% Create "short hand" for the cost function to be minimized
shortCostFunction = @(p) costFunction(p, input_layer_size, hidden_layer_size, X, y);
options = optimset('MaxIter', 50);
[nn_params, cost] = fmincg(shortCostFunction, initial_nn_params, options);


% Obtain Theta1 and Theta2 back from nn_params
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));
Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));


plotDecisionBoundary(Theta1, Theta2, X, y, hidden_layer_size);

