from classify import sigm
import random


def test_sigmoid():
    assert sigm(0) == 0.5
    assert sigm(1) > 0.5
    assert sigm(100) > 0.999
    assert sigm(-1) < 0.5
    for _ in range(10):
        r = random.random() * 5
        assert abs((sigm(r) + sigm(-r)) - 1) < 0.0001


if __name__ == '__main__':
    test_sigmoid()