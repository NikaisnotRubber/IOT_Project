FROM mysql:8.1

# 设置环境变量
ENV MYSQL_ROOT_PASSWORD root
ENV LANG=C.UTF-8

# 覆盖配置文件,主要是开启事件
COPY my.cnf /etc/mysql/my.cnf
# 复制 bs_database.sql 文件到/docker-entrypoint-initdb.d 目录下
COPY bs_database.sql /docker-entrypoint-initdb.d/
