#!/bin/bash
#====================================================
# 各个分支，在被merge前的停留时长
# 它反映我们分支粒度是否合理：不该有太久的分支，如果有则拆
#
# Usage：在某个代码库的根目录执行本脚本
#====================================================

git show-ref | {
    while read branch; do
        merge_base=$(git merge-base --all $branch master)
        date_branched=$(git show -s --format=format:%ci $merge_base)
        echo "$branch: $merge_base @ $date_branched"
    done
}
