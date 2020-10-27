from configs.enums import Algo


class BaseConfig:

    def __init__(self):
        self.algo = Algo.RANDOM_FOREST
        self.DATA_PATH = 'test.csv'
        self.OUTPUT_DIR = 'runs'
        self.EXP_NAME = 'test'
        self.NUM_FEATURES = 81
        self.TEST_SIZE = 0.2
