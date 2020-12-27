import os
import random
import numpy as np
import matplotlib.pyplot as plt

from sklearn.metrics import accuracy_score, precision_recall_fscore_support
from dataloaders.load_with_test import load_data_tts
from training.utils import save_results, get_all_models


ADDITIONAL_FEATURES = ["compositeTypesRatio", "connectivity", "importsCount", "namesLenAvg", "namesStartNotLetterRatio", "top1a", "top1c", "top2a", "top2c", "top3a", "top3c", "top4a", "top4c"]


def run_experiment(logs_dir, exp_name, train_list, test_list, removed_features):
    print(f'Train list: {train_list}', flush=True)
    print(f'Test list: {test_list}', flush=True)

    train_data, test_data = load_data_tts(train_list, test_list, removed_features, None)

    X_train, y_train, keys, X_test, y_test, _ = train_data.to_numpy()[:, :-1], \
                                                train_data.to_numpy()[:, -1], \
                                                train_data.keys(), \
                                                test_data.to_numpy()[:,:-1], \
                                                test_data.to_numpy()[:,-1], \
                                                test_data.keys()

    for model_wrapper in get_all_models():
        os.system(f'mkdir -p {logs_dir}/{model_wrapper.name}/{exp_name}')
        logdir = f'{logs_dir}/{model_wrapper.name}/{exp_name}'
        print(f'Run case: {model_wrapper.name}', flush=True)

        model = model_wrapper.model
        model.fit(X_train, y_train)
        y_hat = model.predict(X_test)
        y_hat = np.array(y_hat).astype(bool)
        y_test = np.array(y_test).astype(bool)
        metrics = precision_recall_fscore_support(y_test, y_hat, average='binary')

        scores = {'accuracy': accuracy_score(y_test, y_hat),
                  'precision': metrics[0], 'recall': metrics[1], 'F-score': metrics[2]}

        indices, importances = model_wrapper.importances()

        top_keys = keys[indices[:5]]
        top_importances = importances[indices[:5]]

        plt.clf()
        plt.gcf().subplots_adjust(left=0.3)
        plt.barh(range(len(top_keys)), top_importances, align='center')
        plt.yticks(range(len(top_keys)), top_keys)
        plt.savefig(f'{logdir}/imp.png')
        plt.clf()
        save_results(logdir, model, scores)

        print("End of case!\n", flush=True)


if __name__ == '__main__':
    seed_value = 42
    os.environ['PYTHONHASHSEED'] = str(seed_value)
    random.seed(seed_value)
    np.random.seed(seed_value)

    train_data_list = ["non_generated_full_train.csv"]
    test_data_list = ["non_generated_full_test.csv"]
    data_list = ["evosuite_full_r.csv", "proto_full_r.csv", "xtext_full_r.csv", "javacc_full_r.csv"]
    for i, dataset in enumerate(data_list):
        test_current = test_data_list.copy()
        test_current.append(dataset)

        train_current = train_data_list.copy()
        for j in range(len(data_list)):
            if i != j:
                train_current.append(data_list[j])

        run_experiment("full_data_base_features", f'full_data_base_features_test_set_{dataset.split(".")[0]}',
                       train_current, test_current, ADDITIONAL_FEATURES)

        run_experiment("full_data_all_features", f'full_data_all_features_test_set_{dataset.split(".")[0]}',
                       train_current, test_current, ADDITIONAL_FEATURES)

    train_data_list = ["non_generated_c2v_train.csv"]
    test_data_list = ["non_generated_c2v_test.csv"]
    data_list = ["evosuite_c2v.csv", "proto_c2v.csv", "xtext_c2v.csv", "javacc_c2v.csv"]
    for i, dataset in enumerate(data_list):
        test_current = test_data_list.copy()
        test_current.append(dataset)

        train_current = train_data_list.copy()
        for j in range(len(data_list)):
            if i != j:
                train_current.append(data_list[j])

        run_experiment("c2v", f'c2v_test_set_{dataset.split(".")[0]}',
                       train_current, test_current, [])
