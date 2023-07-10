#!/bin/bash
#====================================================
# 分析多个代码仓库的代码行数分布
#
# Usage：在多个代码库的上级目录执行本脚本
#====================================================

echo 各个代码库的代码行数和SQL语句行数统计
sum=0
for d in `find . -type d -name "*" -depth 1`; do
    cd $d
    echo $d
    java=`cloc . 2> /dev/null | grep -w Java | awk '{print $5}'`
    ((sum=sum+java))
    cloc . 2> /dev/null | grep -Ew "Java|SQL" | awk "{printf \"%8s %'7d\", \$1, \$5}"
    #cloc . 2> /dev/null | grep -Ew "Java|SQL" | awk '{printf "%8s %7s", $1, $5}' | sed -e :a -e 's/\(.*[0-9]\)\([0-9]\{3\}\)/\1,\2/;ta'
    echo
    cd - > /dev/null
done
printf "总Java代码行数：%'d\n" $sum
