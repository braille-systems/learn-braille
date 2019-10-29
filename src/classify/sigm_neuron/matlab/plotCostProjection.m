#  PLOTCOSTPROJECTION.M: plot J(Theta) where Theta[j]
#  varies in range 'thetaj_range'
#  assuming Theta matrix is already calculated and X, y given
#  uses costFunction.m to calculate J(Theta)

j1 = 3;
j2 = 2;
thetaj1_optimal = theta(j1);
thetaj2_optimal = theta(j2);
thetaj1_range = -0.5:0.01:1;
thetaj2_range = -0.5:0.01:1;
[j1range,j2range]=meshgrid(thetaj1_range,thetaj2_range);
costs = zeros(size(j1range));

for i = 1:size(j1range, 1),
  for k = 1:size(j1range, 2),
    theta(j1) = j1range(i,k);
    theta(j2) = j2range(i,k);
    costs(i,k) = costFunction(theta, X, y);
  endfor
endfor

theta(j1) = thetaj1_optimal; # set things back
theta(j2) = thetaj2_optimal; # set things back

figure
hold on
meshc(j1range,j2range, costs);
