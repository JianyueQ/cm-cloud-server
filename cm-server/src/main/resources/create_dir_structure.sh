#!/bin/bash

# 获取当前工作目录
CURRENT_DIR=$(pwd)

# 提示用户输入主目录名称
read -p "请输入主目录名称: " MAIN_DIR_NAME

# 检查主目录名称是否为空
if [ -z "$MAIN_DIR_NAME" ]; then
    echo "主目录名称不能为空！"
    exit 1
fi

# 创建主目录
mkdir -p "$MAIN_DIR_NAME"

# 进入主目录
cd "$MAIN_DIR_NAME" || exit

# 创建 redis 目录及其子目录
mkdir -p redis/data
mkdir -p redis/conf

# 创建 mysql 目录及其子目录
mkdir -p mysql/data
mkdir -p mysql/conf
mkdir -p mysql/init

# 创建 nginx 目录及其子目录
mkdir -p nginx/html/admin
mkdir -p nginx/html/user
mkdir -p nginx/html/recycler
mkdir -p nginx/conf

# 输出完成信息
echo "目录结构创建完成！"
echo "当前目录结构如下："
tree .

# 返回初始目录
cd "$CURRENT_DIR"
