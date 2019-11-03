function [theta, cost] = gradientDescent(theta, X, y, costFunction,...
   numIter = 60000, rate = 0.001)
   for it = 1:numIter,
     [cost, grad] = costFunction(theta, X, y);
     if mod(it, 500) == 0,
       cost
     endif
     theta -= grad * rate;
   endfor
endfunction