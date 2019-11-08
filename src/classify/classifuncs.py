import numpy as np
import math
import sys


@np.vectorize
def sigm(x):
    return 1 / (1 + math.exp(-x))


@np.vectorize
def _single_cost(ans, predict):
    return -ans * np.log(predict) - (1 - ans) * np.log(1 - predict)


def cross_entropy(ans_mtx, predict_mtx):
    return np.sum(_single_cost(ans_mtx, predict_mtx)) / len(ans_mtx)


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
    return x_mtx, y_vec


def gradient_descent(x_mtx, y_vec, theta_vec, cost_and_grad, learn_rate=0.001, num_iter=300000):
    for it in range(num_iter):
        cost, grad_vec = cost_and_grad(x_mtx, y_vec, theta_vec)
        theta_vec = theta_vec - grad_vec * learn_rate
        if not it % 500:
            sys.stdout.write('\r cost:' + str(round(cost, 4)) + ', iteration: ' + str(it))
    print('')
    return theta_vec, cost
