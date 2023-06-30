FROM java:8

MAINTAINER gjw

RUN mkdir -p /gjw/go/logs

WORKDIR /gjw/go

EXPOSE 5670

ADD ./target/go-server.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
