j1 = 3;
j2 = 2;
thetaj1_optimal = nn_params(j1);
thetaj2_optimal = nn_params(j2);
thetaj1_range = -10:0.5:10;
thetaj2_range = -10:0.5:10;
[j1range,j2range]=meshgrid(thetaj1_range,thetaj2_range);
costs = zeros(size(j1range));

for i = 1:size(j1range, 1),
  for k = 1:size(j1range, 2),
    nn_params(j1) = j1range(i,k);
    nn_params(j2) = j2range(i,k);
    costs(i,k) = costFunction(nn_params, ...
                                   n, ...
                                   hidden_layer_size, ...
                                   X, y);
  endfor
endfor

nn_params(j1) = thetaj1_optimal; # set things back
nn_params(j2) = thetaj2_optimal; # set things back

figure
hold on
meshc(j1range,j2range, costs);