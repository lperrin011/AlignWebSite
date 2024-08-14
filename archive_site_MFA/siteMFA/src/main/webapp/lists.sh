#!/bin/bash

source /home/lucie/miniconda3/etc/profile.d/conda.sh

conda activate aligner

mfa model download acoustic > /home/lucie/eclipse-workspace/siteMFA/src/main/webapp/models.txt

mfa model download dictionary > /home/lucie/eclipse-workspace/siteMFA/src/main/webapp/dictionaries.txt

conda deactivate