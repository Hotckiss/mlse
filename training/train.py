import json
import pickle
from os import makedirs

import pandas as pd
from sklearn.metrics import accuracy_score, precision_recall_fscore_support
from sklearn.model_selection import train_test_split

from configs.utils import generate_config


def load_data(filename):
    data = pd.read_csv(filename)
    return data.to_numpy()[:, :-1], data.to_numpy()[:, -1]


def save_results(config, model, scores):
    logdir = f'{config.OUTPUT_DIR}/{config.EXP_NAME}'
    try:
        makedirs(logdir)
    except:
        pass

    with open(f'{logdir}/scores.txt', 'w') as f:
        json.dump(scores, f)
    with open(f'{logdir}/hyperparameters.json', 'w') as f:
        json.dump(vars(config), f)
    with open(f'{logdir}/model.pkl', 'wb') as f:
        pickle.dump(model, f)


if __name__ == '__main__':
    config = generate_config()
    model = config.create_model()
    X, y = load_data(config.DATA_PATH)
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=config.TEST_SIZE, random_state=42)
    model.fit(X_train, y_train)
    y_hat = model.predict(X_test)
    metrics = precision_recall_fscore_support(y_test, y_hat, average='binary')
    scores = {'accuracy': accuracy_score(y_test, y_hat),
              'precision': metrics[0], 'recall': metrics[1], 'F-score': metrics[2], 'support': metrics[3]}
    save_results(config, model, scores)
