from ../mathfunctions import *
import random


def test_sigmoid():
    assert sigmoid(0) == 0.5
    assert sigmoid(1) > 0.5
    assert sigmoid(100) > 0.999
    assert sigmoid(-1) < 0.5
    for _ in range(10):
        r = random.random() * 5
        assert abs((sigmoid(r) + sigmoid(-r)) - 1) < 0.0001


if __name__ == '__main__':
    test_sigmoid()
