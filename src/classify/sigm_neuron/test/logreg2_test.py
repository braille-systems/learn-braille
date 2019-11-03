from logreg2 import *

def test_cost_grads():
    x_mtx = [[1, 0, 1], [1, 1, 0]]
    y_vec = [0, 1]
    init_theta = [0, 0, 0]
    num_iters = 1
    learn_rate = 0.5
    cost, grads = cost_and_grad(x_mtx, y_vec, init_theta)
    assert cost > 0
    print(grads)

if __name__ == '__main__':
    test_cost_grads()
