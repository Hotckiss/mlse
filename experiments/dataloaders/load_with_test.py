import typing

from dataloaders.load_data import load_data


def load_data_tts(paths_to_train_csv: typing.List[str], paths_to_test_csv: typing.List[str], features_to_drop: typing.List[str], img_name: str):
    train_data = load_data(paths_to_train_csv, features_to_drop=features_to_drop, scale=True, shuffle=True, equal_classes=True, img_name=img_name)
    test_data = load_data(paths_to_test_csv, features_to_drop=features_to_drop, scale=True, shuffle=True, equal_classes=True, img_name=None)

    return train_data, test_data