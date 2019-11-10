import numpy as np
from classify import getxy, sigm, sigm_grad, cross_entropy, gradient_descent
# TODO: layers: list->np.array (make 3-d array for speed and simplicity)


def __bias() -> int:
    return 1  # constant - bias term in activation matrices (always =1)


def predict(layers: list, x_vec: np.array) -> np.array:
    """
    :param layers: list of weights matrices (2-dimensional np.arrays)
    :param x_vec: input vector x (1-dimensional np.array)
    :return: hypothesis of neural network
    """
    a_vec = np.copy(x_vec)  # activation vectors = input vectors
    for weight_mtx in layers:
        a_vec = np.insert(a_vec, 0, __bias())  # add bias term
        a_vec = sigm(weight_mtx.dot(a_vec))  # compute next layer inputs
    return a_vec


def predict_many(layers: list, x_mtx: np.array) -> np.array:
    """
    Vectorized 'predict'. Takes multiple input vectors at a time for the sake of speed.
    :param layers: list of weights matrices (2-dimensional np.arrays)
    :param x_mtx: 2-dimensional np.array of inputs : [-x1-,
                                                      -x2-,
                                                      ...]
    :return: matrix of neural network outputs
    """
    a_mtx = np.copy(x_mtx)  # activation vectors = input vectors
    for weight_mtx in layers:
        a_mtx = np.insert(a_mtx, 0, __bias(), axis=1)  # add bias term
        a_mtx = sigm(a_mtx.dot(weight_mtx.T))  # compute next layer inputs
    return a_mtx


def __activations(layers: list, x_mtx: np.array) -> list:
    """
    Same as 'predict_many', but returns all activations in all layers (not only hypothesis)
    :return: list of activation matrices
    """
    a_mtx = np.copy(x_mtx)
    a_list = [a_mtx]
    for weight_mtx in layers:
        a_mtx = a_list[-1]
        a_mtx = np.insert(a_mtx, 0, __bias(), axis=1) # add bias term
        a_mtx = sigm(a_mtx.dot(weight_mtx.T))  # compute next layer inputs
        a_list.append(a_mtx)
    return a_list


def gradients(x_mtx: np.array, y_mtx: np.array, layers: list) -> list:
    a_list = __activations(layers, x_mtx)
    # calculating errors of each node in each layer
    num_layers = len(layers)
    num_train = len(x_mtx)
    grads = [np.zeros(layer.shape) for layer in layers]  # initialize gradients with zeros
    for x_idx in range(num_train):
        ai_list = []
        for a_mtx in a_list:
            ai_list.append(a_mtx[x_idx])
        y_vec = y_mtx[x_idx]
        deltai_list = [ai_list[-1] - y_vec]
        for layer_idx in range(num_layers - 1, -1, -1):  # for layers in reversed order...
            delta_next = deltai_list[0]  # errors of the next layer (or of hypothesis, if the layer is last)
            activ_vec = np.insert(ai_list[layer_idx], 0, __bias())
            weight_mtx = layers[layer_idx]
            delta_vec = np.multiply(weight_mtx.T.dot(delta_next),sigm_grad(activ_vec))
            delta_vec = delta_vec[1:]  # exclude bias term
            deltai_list.insert(0, delta_vec)
            grads[layer_idx] += np.array([delta_next]).T.dot(np.array([activ_vec]))
    for layer_idx in range(num_layers):
        grads[layer_idx] /= num_train
    return grads


def cost_crossentropy(x_mtx: np.array, y_mtx: np.array, layers: list) -> float:
    predict_mtx = predict_many(layers, x_mtx)
    cost = cross_entropy(y_mtx, predict_mtx)
    return cost


def cost_and_grad(x_mtx: np.array, y_mtx: np.array, layers: list):
    cost = cost_crossentropy(x_mtx, y_mtx, layers)
    grads = np.array(gradients(x_mtx, y_mtx, layers))
    grads = grads.reshape(np.product(grads.shape))
    return cost, grads


if __name__ == '__main__':
    x_mtx, y_vec = getxy('nnet/matlab/data2.txt')
