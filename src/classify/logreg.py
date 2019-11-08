import numpy as np
from classify import getxy, sigm, gradient_descent


def cost_and_grad(x_mtx, y_vec, theta_vec):
    train_size = len(y_vec)
    predict_vec = sigm(x_mtx.dot(theta_vec))
    cost = -(y_vec.dot(np.log(predict_vec)) + (1 - y_vec).dot(np.log(1 - predict_vec))) / train_size
    grad_vec = (x_mtx.T.dot(predict_vec - y_vec)) / train_size
    return cost, grad_vec


if __name__ == '__main__':
    x_mtx, y_vec = getxy('sigm_neuron/matlab/data.txt')
    # add intercept (bias) terms
    bias = 1
    x_mtx = np.insert(x_mtx, 0, bias, axis=1)
    theta_init = np.array([0.0 for _ in range(len(x_mtx[0]))])
    init_cost, init_grad = cost_and_grad(x_mtx, y_vec, theta_init)
    print('initial cost: ' + str(init_cost))
    print('running gradient descent...')
    res_theta, res_cost = gradient_descent(x_mtx, y_vec, theta_init, cost_and_grad)
    print('model trained!')
    print('cost: ' + str(res_cost))
    print('theta: ' + str(res_theta))
