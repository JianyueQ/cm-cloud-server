# 使用基础镜像
FROM openeuler-jdk17:24.03

# 拷贝构建好的 cm jar 包到容器中
COPY cm-server-1.0-SNAPSHOT.jar cm.jar

# 设置语言环境和编码格式
ENV LANG=C.UTF-8 \
    LANGUAGE=C.UTF-8 \
    LC_ALL=C.UTF-8

# 启动命令
ENTRYPOINT ["java", "-jar", "-Dfile.encoding=UTF-8","cm.jar","--spring.profiles.active=test"]