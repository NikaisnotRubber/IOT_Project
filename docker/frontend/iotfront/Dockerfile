
FROM node:18
# 设置工作目录为/app
# 安装项目依赖，使用中国镜像加速
#RUN npm install
# 构建Vitepress项目
RUN npm run build

# 阶段二：构建Nginx映像并复制Vitepress构建结果
# 使用官方nginx:1.21.6镜像作为构建环境
#FROM nginx:1.21.6
# 使用michaelknightdriver提供的带有brotli支持的nginx镜像作为基础镜像
# FROM michaelknightdriver/docker-nginx-brotli
# 删除nginx默认配置
#RUN rm /etc/nginx/conf.d/default.conf
# 复制自定义nginx配置到容器中
#COPY default.conf /etc/nginx/conf.d/default.conf
#COPY nginx.conf /etc/nginx/conf.d/default.conf
# 从构建器阶段复制构建的Vitepress应用到nginx的服务目录
#COPY dist/ /usr/share/nginx/html
EXPOSE 80