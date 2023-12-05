#服务部署脚本
echo 'start exe script'
#1、停止docker服务
echo '(1/5) start compose down old server'
docker compose -f go-compose.yml down
#2、删除镜像文件
echo '(2/5) start delete old server image'
docker rmi go-server:v1.0
#3、清除虚悬镜像
echo '(3/5) start delete dangling images'
docker image prune -f
#4、安装新镜像
echo '(4/5) start install new server image'
docker build -t go-server:v1.0 .
#5、启动新的容器
echo '(5/5) start compose up new server'
docker compose -f go-compose.yml up -d
echo 'exe script success'
