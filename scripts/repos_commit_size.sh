#!/bin/bash
#====================================================
# 分析多个代码仓库的每次commit size分布
#
# Usage：在多个代码库的上级目录执行本脚本
#====================================================

printf '%20s commits +lines/commit -lines/commit  +net/commit\n' repo
for d in `find . -type d -name "*" -depth 1`; do
    cd $d
    printf '%20s' $d
    commits_in_history=$(git log --pretty=oneline | wc -l)
    git log --date=relative --reverse --numstat --pretty="%H" | awk 'NF==3 {plus+=$1; minus+=$2;}  END {printf("%8d %13d  %12d  %d\n", "'"$commits_in_history"'", plus/"'"$commits_in_history"'", minus/"'"$commits_in_history"'", plus/"'"$commits_in_history"'" - minus/"'"$commits_in_history"'")}' 2>/dev/null
    cd - > /dev/null
done
