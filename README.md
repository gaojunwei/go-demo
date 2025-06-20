# nacos配置
[Nacos-discovery配置](https://github.com/alibaba/spring-cloud-alibaba/wiki/Nacos-discovery)  
[Nacos-config配置](https://github.com/alibaba/spring-cloud-alibaba/wiki/Nacos-config)
[nacos2.5.0数据库脚本mysql](source/db/nacos2.5.0/mysql-schema.sql)

## docker安装Nacos服务
```yaml
version: '3.8'

services:
  nacos:
    image: nacos/nacos-server:v2.5.0
    container_name: myNacos
    restart: always
    ports:
      - "8848:8848"
      - "9848:9848"
    environment:
      - MODE=standalone
      - PREFER_HOST_MODE=hostname
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=mysql8
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_USER=root
      - MYSQL_SERVICE_PASSWORD=tiger
      - MYSQL_SERVICE_DB_NAME=nacos
      - NACOS_AUTH_ENABLE=true
      - NACOS_AUTH_IDENTITY_KEY=admin
      - NACOS_AUTH_IDENTITY_VALUE=tiger
      - NACOS_AUTH_TOKEN=SecretKeyA1B2C3D4E5F678901234567890ABCDEF01234567890ABCDEF1234567890ABCDEF
    networks:
      - elk

networks:
  elk:
    external: true
```
3. nacos安装 
> web访问地址：http://127.0.0.1:8848/nacos  
> 账号密码：nacos