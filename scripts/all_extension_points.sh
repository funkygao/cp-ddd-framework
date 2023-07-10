#!/bin/bash
#====================================================
# 分析当前代码库的所有远程分支，列出扩展点定义
# W6 plus开发模式下，由于有enforce机制，所有扩展点定义必须集成 IDomainExtension
# 以此为规则扫描分析
#
# Usage：在某个代码库的根目录执行本脚本
#====================================================

git grep '.IDomainExtension.{$' $(git ls-remote  . 'refs/remotes/*' | grep -v HEAD | cut -f2) | awk -F: '{print $3}' | sort | uniq

