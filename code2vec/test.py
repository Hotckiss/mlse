import pandas as pd
import os

if __name__ == '__main__':
    dirname = './features/EvoSuite/'
    to = './features/evosuite/'
    data = pd.read_csv('./features/evosuite.csv')
    for filename in data['filename']:
        if os.path.isfile(f"{to}{filename}"):
            continue
        os.rename(f"{dirname}{filename}", f"{to}{filename}")