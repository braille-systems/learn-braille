import numpy as np
from classify import getxy, sigm, cross_entropy, gradient_descent


def predict(layers, x_vec):
    """
    :param layers: list of weights matrices (2-dimensional np.arrays)
    :param x_vec: input vector x (1-dimensional np.array)
    :return: hypothesis of neural network
    """
    a_vec = np.copy(x_vec)  # activation vectors = input vectors
    bias = 1
    for weight_mtx in layers:
        a_vec = np.insert(a_vec, 0, bias)  # add bias term
        a_vec = sigm(weight_mtx.dot(a_vec))  # compute next layer inputs
    return a_vec


def predict_many(layers, x_mtx):
    """
    Vectorized 'predict'. Takes multiple input vectors at a time for the sake of speed.
    :param layers: list of weights matrices (2-dimensional np.arrays)
    :param x_mtx: 2-dimensional np.array of inputs : [-x1-,
                                                      -x2-,
                                                      ...]
    :return: matrix of neural network outputs
    """
    a_mtx = np.copy(x_mtx)  # activation vectors = input vectors
    bias = 1
    for weight_mtx in layers:
        a_mtx = np.insert(a_mtx, 0, bias, axis=1)  # add bias term
        a_mtx = sigm(a_mtx.dot(weight_mtx.T))  # compute next layer inputs
    return a_mtx


def cost_and_grad(x_mtx, y_mtx, layers):
    train_size = len(y_mtx)
    predict_mtx = predict_many(layers, x_mtx)
    cost = cross_entropy(y_mtx, predict_mtx)
    # TODO calculate gradients
    return cost, None



if __name__ == '__main__':
    x_mtx, y_vec = getxy('nnet/matlab/data2.txt')
