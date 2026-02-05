#!/bin/bash

# 一键启动Redis, Nacos, RabbitMQ容器的脚本
# 适用于openEuler系统

echo "开始启动Docker容器..."

# 检查Docker服务是否运行
if ! systemctl is-active --quiet docker; then
    echo "Docker服务未运行，正在启动Docker服务..."
    sudo systemctl start docker
    if [ $? -ne 0 ]; then
        echo "无法启动Docker服务，请检查Docker配置"
        exit 1
    fi
    sleep 3
fi

# 检查并启动Redis容器
if [ "$(docker ps -aq -f name=^redis-container$)" ]; then
    if [ "$(docker ps -aq -f name=^redis-container$ -f status=exited)" ]; then
        echo "正在启动Redis容器..."
        docker start redis-container
        if [ $? -eq 0 ]; then
            echo "Redis容器启动成功"
        else
            echo "Redis容器启动失败"
        fi
    else
        echo "Redis容器已在运行"
    fi
else
    echo "Redis容器不存在"
fi

# 检查并启动Nacos容器
if [ "$(docker ps -aq -f name=^nacos-standalone-derby$)" ]; then
    if [ "$(docker ps -aq -f name=^nacos-standalone-derby$ -f status=exited)" ]; then
        echo "正在启动Nacos容器..."
        docker start nacos-standalone-derby
        if [ $? -eq 0 ]; then
            echo "Nacos容器启动成功"
        else
            echo "Nacos容器启动失败"
        fi
    else
        echo "Nacos容器已在运行"
    fi
else
    echo "Nacos容器不存在"
fi

# 检查并启动RabbitMQ容器
if [ "$(docker ps -aq -f name=^mq$)" ]; then
    if [ "$(docker ps -aq -f name=^mq$ -f status=exited)" ]; then
        echo "正在启动RabbitMQ容器..."
        docker start mq
        if [ $? -eq 0 ]; then
            echo "RabbitMQ容器启动成功"
        else
            echo "RabbitMQ容器启动失败"
        fi
    else
        echo "RabbitMQ容器已在运行"
    fi
else
    echo "RabbitMQ容器不存在"
fi

# 检查并启动Seata容器
if [ "$(docker ps -aq -f name=^seata-server$)" ]; then
    if [ "$(docker ps -aq -f name=^seata-server$ -f status=exited)" ]; then
        echo "正在启动Seata容器..."
        docker start seata-server
        if [ $? -eq 0 ]; then
            echo "Seata容器启动成功"
        else
            echo "Seata容器启动失败"
        fi
    else
        echo "Seata容器已在运行"
    fi
else
  echo "Seata容器不存在"
fi

# 检查并启动MySQL容器
if [ "$(docker ps -aq -f name=^mysql-container$)" ]; then
    if [ "$(docker ps -aq -f name=^mysql-container$ -f status=exited)" ]; then
        echo "正在启动MySQL容器..."
        docker start mysql-container
        if [ $? -eq 0 ]; then
            echo "MySQL容器启动成功"
        else
            echo "MySQL容器启动失败"
        fi
    else
        echo "MySQL容器已在运行"
    fi
else
  echo "MySQL容器不存在"
fi

# 显示所有容器状态
echo "当前容器状态："
docker ps -a

echo "脚本执行完成！"
