class BaseConfig:
    def __init__(self, algo):
        self.algo = algo
        self.DATA_PATH = '25nov.csv'
        self.OUTPUT_DIR = 'runs'
        self.EXP_NAME = '2dec2'
        self.NUM_FEATURES = 97
        self.TEST_SIZE = 0.2
        self.model = None
