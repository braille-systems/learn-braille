function [J grad] = costFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   X, y)
								   
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 1, (hidden_layer_size + 1));

m = size(X, 1);
%making hypothesis
X = [ones(m, 1), X];
z2 = X * Theta1';
A2 = sigmoid(z2);
m2 = size(A2, 1);
A2 = [ones(m2, 1), A2];
z3 = A2 * Theta2';
A3 = sigmoid(z3); %hypothesis (m x 1)

%computing cost function
J = y .* log(A3) + (1 - y) .* log(1 - A3);
J = - 1 / m * sum(sum(J));



Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

%calculating errors of node j in layer l

for i = 1:m,
  a3 = A3(i,:)';
  a2 = A2(i, :)';
  a1 = X(i, :)';
  yi = y(i, :)';
  delta_3 = a3 - yi;
  delta_2 = (Theta2' * delta_3) .* a2 .* (1 - a2);
  delta_2 = delta_2(2:end); % exclude bias term
  
  Theta2_grad = Theta2_grad + delta_3 * a2';
  Theta1_grad = Theta1_grad + delta_2 * a1';
endfor

Theta1_grad = Theta1_grad ./ m;
Theta2_grad = Theta2_grad ./ m;

% unrolling gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];

end