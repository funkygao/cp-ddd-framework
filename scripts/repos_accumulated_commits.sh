#!/bin/bash
#====================================================
# 分析多个代码仓库的master分支累计的提交次数
#
# Usage：在多个代码库的上级目录执行本脚本
#====================================================

for d in `find . -type d -name "*" -depth 1`; do
    cd $d
    printf '%20s' $d
    repo_age=$(git log --date=relative --reverse --format="%ad" | head -n1)
    commits_in_history=$(git log --pretty=oneline | wc -l)
    echo "$commits_in_history commits since $repo_age"
    cd - > /dev/null
done
