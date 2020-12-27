import json
import pickle
from os import makedirs

import pandas as pd
from sklearn import preprocessing

from models.models import *

def scale_dataset(numpy_dataset):
    min_max_scaler = preprocessing.MinMaxScaler()
    numpy_dataset_scaled = min_max_scaler.fit_transform(numpy_dataset)
    return pd.DataFrame(numpy_dataset_scaled)


def save_results(logdir, model, scores):
    try:
        makedirs(logdir)
    except:
        pass

    with open(f'{logdir}/scores.txt', 'w') as f:
        json.dump(scores, f)

    with open(f'{logdir}/model.pkl', 'wb') as f:
        pickle.dump(model, f)


def get_sample_models():
    return [NaiveBayesModel(alpha=1),
            SVMModel(tol=1e-4, C=1.0, max_iter=10),
            DecisionTreeModel(max_depth=None, max_features=None),
            DecisionTreeModel(max_depth=None, max_features=10),
            DecisionTreeModel(max_depth=6, max_features=10),
            DecisionTreeModel(max_depth=None, max_features=40),
            RandomForestModel(n_estimators=10, max_depth=None),
            RandomForestModel(n_estimators=30, max_depth=None),
            RandomForestModel(n_estimators=100, max_depth=None),
            RandomForestModel(n_estimators=1000, max_depth=None)
            ]

def get_all_models():
    return [NaiveBayesModel(alpha=0.01),
            NaiveBayesModel(alpha=0.1),
            NaiveBayesModel(alpha=0.5),
            NaiveBayesModel(alpha=1),
            NaiveBayesModel(alpha=2),
            NaiveBayesModel(alpha=4),
            SVMModel(tol=1e-4, C=1.0, max_iter=1000),
            SVMModel(tol=1e-4, C=1.0, max_iter=100),
            SVMModel(tol=1e-4, C=1.0, max_iter=10),
            SVMModel(tol=1e-4, C=2.0, max_iter=1000),
            SVMModel(tol=1e-4, C=2.0, max_iter=100),
            SVMModel(tol=1e-4, C=2.0, max_iter=10),
            DecisionTreeModel(max_depth=None, max_features=None),
            DecisionTreeModel(max_depth=2, max_features=None),
            DecisionTreeModel(max_depth=4, max_features=None),
            DecisionTreeModel(max_depth=6, max_features=None),
            DecisionTreeModel(max_depth=None, max_features=10),
            DecisionTreeModel(max_depth=2, max_features=10),
            DecisionTreeModel(max_depth=4, max_features=10),
            DecisionTreeModel(max_depth=6, max_features=10),
            RandomForestModel(n_estimators=10, max_depth=None),
            RandomForestModel(n_estimators=10, max_depth=2),
            RandomForestModel(n_estimators=10, max_depth=4),
            RandomForestModel(n_estimators=10, max_depth=6),
            RandomForestModel(n_estimators=10, max_depth=8),
            RandomForestModel(n_estimators=20, max_depth=None),
            RandomForestModel(n_estimators=20, max_depth=2),
            RandomForestModel(n_estimators=20, max_depth=4),
            RandomForestModel(n_estimators=20, max_depth=6),
            RandomForestModel(n_estimators=20, max_depth=8),
            ]
