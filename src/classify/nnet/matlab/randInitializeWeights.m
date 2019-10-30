function W = randInitializeWeights(L_in, L_out)
%RANDINITIALIZEWEIGHTS Randomly initialize the weights of a layer with L_in
%incoming connections and L_out outgoing connections to small values

epsilon_init = 1.2; %suggested in the description of the task by Andrew NG
W = rand(L_out, 1 + L_in) * 2 * epsilon_init - epsilon_init;

end
