import math

def sigm(x):
    return 1 / (1 + math.e ** -x)


def mult_piecewise(list1, list2):
    assert len(list1) == len(list2)
    return list(map(lambda x, y: x * y, list1, list2))


def mult(list1, list2):
    return sum(mult_piecewise(list1, list2))


def add(list1, list2):
    assert(len(list1)) == len(list2)
    return list(map(lambda x, y: x + y, list1, list2))


def subtract(list1, list2):
    assert (len(list1)) == len(list2)
    return list(map(lambda x, y: x - y, list1, list2))