from sklearn.svm import LinearSVC
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import BernoulliNB
import numpy as np

from configs.enums import ClassificationAlgorithm


class BaseModel:
    def __init__(self, name):
        self.name = name

    def importances(self):
        raise NotImplementedError


class NaiveBayesModel(BaseModel):

    def __init__(self, alpha=1.0):
        super().__init__(f'{ClassificationAlgorithm.NAIVE_BAYES}_{alpha}')
        self.model = BernoulliNB(alpha=alpha)

    def importances(self):
        return self.model.feature_log_prob_[1, :].argsort(), self.model.feature_log_prob_[1, :]


class SVMModel(BaseModel):
    def __init__(self, tol=1e-4, C=1.0, max_iter=1000):
        super().__init__(f'{ClassificationAlgorithm.SVM}_{tol}_{C}_{max_iter}')
        self.model = LinearSVC(tol=tol, C=C, max_iter=max_iter)

    def importances(self):
        coef_ = self.model.coef_[0]
        indices = np.argsort(coef_)[::-1]
        return indices, coef_


class DecisionTreeModel(BaseModel):
    def __init__(self, max_depth=None, max_features=None):
        super().__init__(f'{ClassificationAlgorithm.DECISION_TREE}_{max_depth}_{max_features}')
        self.model = DecisionTreeClassifier(max_depth=max_depth, max_features=max_features, random_state=42)

    def importances(self):
        importances = self.model.feature_importances_
        indices = np.argsort(importances)[::-1]
        return indices, importances


class RandomForestModel(BaseModel):
    def __init__(self, n_estimators=10, max_depth=None):
        super().__init__(f'{ClassificationAlgorithm.RANDOM_FOREST}_{n_estimators}_{max_depth}')
        self.model = RandomForestClassifier(n_estimators=n_estimators, max_depth=max_depth, random_state=0)

    def importances(self):
        importances = self.model.feature_importances_
        indices = np.argsort(importances)[::-1]
        return indices, importances
