# kotlin springboot项目

```text
http://127.0.0.1:5670/st/
```
```text
{"age":18,"clzz":"初一三班","name":"小明","school":"中山大学","teacher":"刘能"}
```
```text
docker build -t go-server:1.0 .
```
```text
docker run -d --network=my_net --privileged=true -e TZ=Asia/Shanghai -p 5670:5670 -v D:\work\docker\go-server\logs:/gjw/go/logs --name goServer go-server:1.0
```

## kotlin集成mapstruct
参考项目地址：https://github.com/mapstruct/mapstruct-examples
问题记录
```text
 问题一：
    @Mappings 参数不能嵌套 @Mapping，只能通过写多个 @Mapping 实现；
```