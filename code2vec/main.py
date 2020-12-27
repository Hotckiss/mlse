import os
import tensorflow as tf
from sys import argv

from common import Config
from file2vec import File2Vec
from model import Model
from model_defs import models

dataset_dir = 'java_files/'

if __name__ == '__main__':
    # Get the model for this session
    tf.compat.v1.disable_eager_execution()
    modelDef = models[int(argv[1])]
    print("\n\nRunning model:", modelDef['name'], '\n\n')
    config = Config.get_default_config(modelDef['location'])

    modelObj = Model(config, modelDef['name'])
    modelObj.predict([])
    print('Created model')

    # For each dataset in our collection of them, run the model on it
    for dataset in os.listdir(dataset_dir):
        if os.path.isdir(os.path.join(dataset_dir, dataset)):
            print("Processing dataset:", dataset)
            file2vec = File2Vec(config, modelObj, modelDef, dataset)
            file2vec.run()

    modelObj.close_session()
