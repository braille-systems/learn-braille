from mathfunctions import *


def getxy(filename):
    delimiter = ','
    x_mtx = []
    y_vec = []
    with open(filename) as fin:
        for line in fin:
            line_list = list(map(float, line.strip().split(delimiter)))
            x_mtx.append(line_list[:-1])
            y_vec.append(line_list[-1])
    # add intercept (bias) terms
    bias = 1
    for row in x_mtx:
        row.insert(0, bias)
    return x_mtx, y_vec

def normalize(x_mtx):
    xmin = float('inf')
    xmax = -float('inf')
    for row in x_mtx:
        for elem in row[1:]:  # exclude bias term
            xmin = elem if elem < xmin else xmin
            xmax = elem if elem > xmax else xmax
    maxdiff = xmax - xmin
    mean = 0.5*(xmax+xmin)
    for row in x_mtx:
        for irow in range(1,len(row)):
            row[irow] -= mean
            row[irow] /= maxdiff

def cost_and_grad(x_mtx, y_vec, theta_vec):
    train_size = len(y_vec)
    cost = 0
    predict_vec = []
    for x_vec, label in zip(x_mtx, y_vec):
        hypothesis = sigm(mult(x_vec, theta_vec))
        cost -= label * math.log(hypothesis) + (1 - label) * math.log(1 - hypothesis)
        predict_vec.append(hypothesis)
    cost /= train_size
    # grad(theta) = (x'*(predictions-labels))/train_size
    grad_vec = []
    delta_vec = subtract(predict_vec, y_vec)
    for icol in range(len(x_mtx[0])):  # for each column in x...
        x_col_vec = [x_mtx[irow][icol] for irow in range(len(x_mtx))]
        grad_vec.append(mult(x_col_vec, delta_vec) / train_size)
    return cost, grad_vec


def gradient_descent(x_mtx, y_vec, theta_vec, learn_rate=0.001, num_iter=500):
    for it in range(num_iter):
        cost, grad_vec = cost_and_grad(x_mtx, y_vec, theta_vec)
        for i in range(len(theta_vec)):
            theta_vec[i] -= grad_vec[i] * learn_rate
    return theta_vec, cost



if __name__ == '__main__':
    input_filename = 'matlab/data.txt'
    x_mtx, y_vec = getxy(input_filename)
    normalize(x_mtx)
    print(x_mtx)
    nparams = len(x_mtx[0])
    init_theta = [0 for _ in range(nparams)]
    # init_theta = [-25, 0.2, 0.2] # nearly the right answer
    init_cost, init_grad = cost_and_grad(x_mtx, y_vec, init_theta)
    print(init_cost)
    res_theta, res_cost = gradient_descent(x_mtx, y_vec, init_theta)
    print(res_cost)
    print(res_theta)