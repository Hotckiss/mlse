from enum import Enum


class ClassificationAlgorithm(str, Enum):
    RANDOM_FOREST = 'RANDOM_FOREST'
    DECISION_TREE = 'DECISION_TREE'
    NAIVE_BAYES = 'NAIVE_BAYES'
    SVM = 'SVM'
