from enum import Enum


class Algo(str, Enum):
    RANDOM_FOREST = 'RANDOM_FOREST'
    DECISION_TREE = 'DECISION_TREE'
    NAIVE_BAYES = 'NAIVE_BAYES'
    SVM = 'SVM'
