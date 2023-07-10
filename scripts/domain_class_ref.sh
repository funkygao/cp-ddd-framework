#!/bin/bash
#====================================================
# 分析领域层的类被引用的次数分布
# 它反映了：业务概念的密度
#
# 它依赖GNU Global工具
# Usage：在某个代码库的根目录执行本脚本
#====================================================

# 如果没有安装GNU Global，uncomment下面2行执行
#brew install global
#gtags -v

domainModulePath=changeMeFirst
threshold=20 # 被引用超过20次的类名才出现在报告里
for f in `global -P $domainModulePath | grep -v Impl`; do
    java=`basename $f`
    class=`echo $java | cut -d. -f1`
    refN=`global -r $class | grep -v Test | wc -l`
    if (( $refN > $threshold )); then
        echo $refN $class
    fi
done | sort -n
