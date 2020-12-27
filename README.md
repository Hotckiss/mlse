# Genrated Code Detection

This repository contains code of research project "Genrated Code Detection".

Repository structure:

1) code2vec -- third party implementation of code2vec model
2) dataset -- Java source code for automatic collection of dataset and features calculation
3) git_python_wrapper -- python wrapper for GitHib search API
4) experiments -- source code of experiments 

### Experiments evaluation
Folder `experiments/datasets` contains datasets for both simple features evaluation and code2vec features evaluation.

Folder `experiments/training/*` contains authors evaluation results.

To rerun all experiments with presented data just run `python experiments/training/train.py` (but works several hours for all models)
