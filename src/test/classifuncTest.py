from classify import sigm, cross_entropy
import numpy as np
import random


def test_sigmoid():
    assert sigm(0) == 0.5
    assert sigm(1) > 0.5
    assert sigm(100) > 0.999
    assert sigm(-1) < 0.5
    for _ in range(10):
        r = random.random() * 5
        assert abs((sigm(r) + sigm(-r)) - 1) < 0.0001


def cross_entropy_test():
    y_mtx = np.array([[0, 1]]).T
    good_predict_mtx = np.array([[0.0005, 0.9995]]).T
    bad_predict_mtx = np.array([[0.9995, 0.9995]]).T
    assert 0 <= cross_entropy(y_mtx, good_predict_mtx) < 1e-3
    assert 0.5 <= cross_entropy(y_mtx, bad_predict_mtx)


if __name__ == '__main__':
    test_sigmoid()
    cross_entropy_test()