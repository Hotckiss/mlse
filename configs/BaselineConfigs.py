from configs.BaseConfig import BaseConfig
from sklearn.svm import SVC
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import GaussianNB


class NaiveBayesConfig(BaseConfig):

    def __init__(self):
        super().__init__()
        self.l2 = 0.01

    def create_model(self):
        return GaussianNB()


class SVMConfig(BaseConfig):

    def __init__(self):
        super().__init__()
        self.gamma = 'auto'

    def create_model(self):
        return SVC(gamma=self.gamma)


class DecisionTreeConfig(BaseConfig):

    def __init__(self):
        super().__init__()
        self.random_state = 0

    def create_model(self):
        return DecisionTreeClassifier(random_state=self.random_state)


class RandomForestConfig(BaseConfig):

    def __init__(self):
        super().__init__()
        self.random_state = 0

    def create_model(self):
        return RandomForestClassifier(random_state=self.random_state)
