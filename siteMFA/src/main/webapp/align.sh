#!/bin/bash

source /home/lucie/miniconda3/etc/profile.d/conda.sh

conda activate aligner

mfa model download acoustic $2

mfa model download dictionary $3

mfa align --clean "$1" "$2" "$3" "$4"

conda deactivate