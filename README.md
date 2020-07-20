同城票据登录验证码破解

超级鹰官网：http://www.chaojiying.com/price.html

业务方提供超级鹰：账号：exbill369 密码：123QWEasd；邮箱：qubushen@qq.com；QQ：7088076  softid：906522

我方自己测试超级鹰：账号：cjy2jy 密码：diyanfei910629 softid：903238

文件夹cert下为网址ssl证书

需要将证书添加信任证书才能正常请求（http请求除外）

密码统一为：changeit

首先将C:\Program Files\Java\jdk1.8.0_151\jre\lib\security下的cacerts拷贝到D:/proxy/目录下文件名一致

keytool -import -alias tcpj -keystore D:/proxy/cacerts -file D:/proxy/tcpjw.cer

keytool -import -alias captchaqq -keystore D:/proxy/cacerts -file D:/proxy/captchaqq.cer

参考网址：https://blog.csdn.net/ljskr/article/details/84570573


