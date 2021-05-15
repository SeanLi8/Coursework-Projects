#! /usr/bin/env sh
export ASAN_OPTIONS=verify_asan_link_order=0

for folder in `ls -d tests/*/ | sort -V`; do
    name=$(basename "$folder")
    if [ "$name" = "no_arguments" ];then 
        continue
    fi
    if [ "$name" = "fnf" ];then 
        continue
    fi
    assembly_file=tests/$name/$name.asm
    python3 x2017_compiler.py $assembly_file

done