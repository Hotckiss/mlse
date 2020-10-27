from configs.BaseConfig import BaseConfig
from configs.BaselineConfigs import RandomForestConfig, DecisionTreeConfig, NaiveBayesConfig, SVMConfig
from configs.enums import Algo


def generate_config():
    base = BaseConfig()
    return {Algo.RANDOM_FOREST: RandomForestConfig(),
            Algo.DECISION_TREE: DecisionTreeConfig(),
            Algo.NAIVE_BAYES: NaiveBayesConfig(),
            Algo.SVM: SVMConfig()}[base.algo]
