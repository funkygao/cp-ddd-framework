#!/bin/bash
#====================================================
# 经常发生冲突的类，往往是违反了单一职责原则
#
# Usage：在某个代码库的根目录执行本脚本
#====================================================

topN=20
for branch in `git branch -r | grep -v HEAD`;do
    git merge --no-ff --no-commit $branch
    git diff --name-only --diff-filter=U
    git merge --abort
done | grep -v merging | grep -v Removing | grep -v Automatic | grep -v CONFLICT | grep -v fatal | grep -v warning | grep -v Already | sort | uniq -c | sort -n | tail -$topN
