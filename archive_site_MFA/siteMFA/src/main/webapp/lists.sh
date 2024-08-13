#!/bin/bash

echo Hello1

source /home/lucie/miniconda3/etc/profile.d/conda.sh

echo Hello2

conda activate aligner

echo Hello3

mfa model download acoustic > /home/lucie/eclipse-workspace/siteMFA/src/main/webapp/models.txt

echo Hello4

mfa model download dictionary > /home/lucie/eclipse-workspace/siteMFA/src/main/webapp/dictionaries.txt

echo Hello5

conda deactivate