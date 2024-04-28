## 制作镜像
```bash
docker build -t go-server:v1.0 .
```
## 创建网络
```shell
docker network create elk
```
## es docker安装
```shell
docker run -d --name es01 --net elk -p 9200:9200 --privileged=true -m 1GB -v .\es\config:/usr/share/elasticsearch/config docker.elastic.co/elasticsearch/elasticsearch:8.13.1
```
## kibana docker安装
```shell
docker run -d --name kib01 --net elk -p 5601:5601 --privileged=true -v .\kibana\config:/usr/share/kibana/config docker.elastic.co/kibana/kibana:8.13.1
```
## logstash docker安装
```shell
docker run -d --name lg01 --net elk -p 5044:5044 -v .\logstash\config:/usr/share/logstash/config -v .\logstash\pipeline:/usr/share/logstash/pipeline -v D:\work\logs:/logs docker.elastic.co/logstash/logstash:8.13.1
```

