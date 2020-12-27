import os
import random


def split_csv(path, test_size=0.2):
    with open(path, "r") as csv_file, open(f'{path.split("/")[-1].split(".")[0]}_c2v_train.csv', "w") as csv_file_train, open(f'{path.split("/")[-1].split(".")[0]}_c2v_test.csv', "w") as csv_file_test:
        is_header = True
        for line in csv_file.readlines():
            if is_header:
                csv_file_train.write(line)
                csv_file_test.write(line)
                is_header = False
            else:
                if random.random() < test_size:
                    csv_file_test.write(line)
                else:
                    csv_file_train.write(line)


if __name__ == "__main__":
    os.system("ls -a")
    split_csv("../training/non_generated.csv")