import numpy as np
import pandas as pd
import typing

from plots.features import plot_feature
from training.utils import scale_dataset


def load_data(paths_to_csv: typing.List[str], features_to_drop: typing.List[str], scale: bool = True,
              shuffle: bool = True, equal_classes: bool = True, img_name: str = None):
    dataframes = [pd.read_csv(path) for path in paths_to_csv]
    data = dataframes[0]
    for frame in dataframes[1:]:
        data = data.append(frame)

    if len(features_to_drop) > 0:
        data = data.drop(features_to_drop, axis=1)

    if shuffle:
        data = data.sample(frac=1, random_state=42)

    print(f'Data shape is {data.shape}')
    data_positive = data[data["label"] == 1.0]
    data_negative = data[data["label"] == 0.0]
    print(f'Positive samples count: {data_positive.shape[0]}')
    print(f'Negative samples count: {data_negative.shape[0]}')

    min_len = min(data_positive.shape[0], data_negative.shape[0])

    if equal_classes:
        if data_positive.shape[0] > min_len:
            data_positive = data_positive.sample(min_len)

        if data_negative.shape[0] > min_len:
            data_negative = data_negative.sample(min_len)

    print(f'Positive samples count after cut: {data_positive.shape[0]}')
    print(f'Negative samples count after cut: {data_negative.shape[0]}')

    numpy_dataset = np.vstack([data_positive, data_negative])

    keys_dump = data.keys()
    keys_dump_dict = {}
    for i, key in enumerate(keys_dump):
        keys_dump_dict[i] = key

    if scale:
        data = scale_dataset(np.vstack([data_positive, data_negative]))
    else:
        data = pd.DataFrame(numpy_dataset)

    # restore columns after scale
    data.rename(columns=keys_dump_dict, inplace=True)

    if shuffle:
        data = data.sample(frac=1, random_state=42)

    if img_name is not None:
        data_bad = data[data['label'] == 0.0]
        data_good = data[data['label'] == 1.0]
        plot_feature(".", img_name, data_bad, data_good, 'top1a')
        plot_feature(".", img_name, data_bad, data_good, 'top2a')
        plot_feature(".", img_name, data_bad, data_good, 'top3a')
        plot_feature(".", img_name, data_bad, data_good, 'top4a')

        plot_feature(".", img_name, data_bad, data_good, 'compositeTypesRatio')
        plot_feature(".", img_name, data_bad, data_good, 'namesLenAvg')
        plot_feature(".", img_name, data_bad, data_good, 'namesStartNotLetterRatio')
        plot_feature(".", img_name, data_bad, data_good, 'importsCount')

    return data
