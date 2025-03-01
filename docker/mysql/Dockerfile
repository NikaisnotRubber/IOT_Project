FROM mysql:8.1

# 設置環境變量
ENV MYSQL_ROOT_PASSWORD root
ENV LANG=C.UTF-8

# 覆蓋配置文件,主要是開啓事件
COPY my.cnf /etc/mysql/my.cnf
# 複製 bs_database.sql 文件到/docker-entrypoint-initdb.d 目錄下
COPY bs_database.sql /docker-entrypoint-initdb.d/
