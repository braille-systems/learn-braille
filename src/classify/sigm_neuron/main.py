import numpy as np
import math
import sys


@np.vectorize
def sigm(x):
    return 1 / (1 + math.exp(-x))


def getxy(filename):
    delimiter = ','
    x_mtx = []
    y_vec = []
    with open(filename) as fin:
        for line in fin:
            line_list = list(map(float, line.strip().split(delimiter)))
            x_mtx.append(line_list[:-1])
            y_vec.append(line_list[-1])
    # convert to numpy arrays
    x_mtx = np.array(x_mtx)
    y_vec = np.array(y_vec)
    # add intercept (bias) terms
    bias = 1
    x_mtx = np.insert(x_mtx, 0, bias, axis=1)
    return x_mtx, y_vec


def cost_and_grad(x_mtx, y_vec, theta_vec):
    train_size = len(y_vec)
    predict_vec = sigm(x_mtx.dot(theta_vec))
    cost = -(y_vec.dot(np.log(predict_vec)) + (1 - y_vec).dot(np.log(1 - predict_vec))) / train_size
    grad_vec = (x_mtx.T.dot(predict_vec - y_vec)) / train_size
    return cost, grad_vec


def gradient_descent(x_mtx, y_vec, theta_vec, learn_rate=0.001, num_iter=100000):
    for it in range(num_iter):
        cost, grad_vec = cost_and_grad(x_mtx, y_vec, theta_vec)
        theta_vec = theta_vec - grad_vec * learn_rate
        if not it % 500:
            sys.stdout.write('\r cost:' + str(round(cost, 4)) + ', iteration: ' + str(it))
    print('')
    return theta_vec, cost


if __name__ == '__main__':
    x_mtx, y_vec = getxy('matlab/data.txt')
    theta_init = np.array([0.0 for _ in range(len(x_mtx[0]))])
    init_cost, init_grad = cost_and_grad(x_mtx, y_vec, theta_init)
    print('initial cost: ' + str(init_cost))
    print('running gradient descent...')
    res_theta, res_cost = gradient_descent(x_mtx, y_vec, theta_init)
    print('model trained!')
    print('cost: ' + str(res_cost))
    print('theta: ' + str(res_theta))
