#!/bin/bash
#====================================================
# 根据文件名来统计研发思想里的building block
#
# Usage：在某个代码库的根目录执行本脚本
#====================================================

find . -type f -name "*.java" | grep -v Test.java | xargs basename | cut -d. -f1 | sed 's/\([A-Z]\)/ \1/g' | awk 'NF>1{print $NF}' | sort | uniq -c | sort -n | grep -vw 1
