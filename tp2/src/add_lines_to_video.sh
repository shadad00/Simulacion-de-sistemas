#!/bin/bash
# Usage:
#   ./add_lines_to_vide.sh input.mp4 output.mp4
ffmpeg -i $1 -vf "drawbox=35:35:920:920:red@1:t=10, drawbox=495:35:2:345:red@1:t=10, drawbox=495:610:2:345:red@1:t=10" $2