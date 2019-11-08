import numpy as np
from classify import getxy, sigm, gradient_descent


def nnet_predict(layers, x_mtx):
    a_mtx = np.copy(x_mtx)  # activation vectors: training examples in x
    for weight_mtx in layers:
        # TODO add bias term here
        a_mtx = sigm(weight_mtx.dot(a_mtx))
        return a_mtx

if __name__ == '__main__':
    x_mtx, y_vec = getxy('nnet/matlab/data2.txt')