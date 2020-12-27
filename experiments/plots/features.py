import matplotlib.pyplot as plt


def plot_feature(directory, img_name, data_bad, data_good, feature):
    plt.clf()
    feature_bad = list(data_bad[feature].values)
    feature_good = list(data_good[feature].values)
    plt.plot(list(range(len(feature_bad))), list(sorted(feature_bad)), label='false')
    plt.plot(list(range(len(feature_good))), list(sorted(feature_good)), label='true')

    plt.legend()
    plt.title(feature)
    plt.savefig(f'{directory}/{img_name}_{feature}.png')
    plt.clf()