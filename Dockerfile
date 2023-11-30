FROM anapsix/alpine-java:8_server-jre_unlimited

COPY ./go-api/target/go-api.jar /app/app.jar

EXPOSE 8801

CMD java -jar /app/app.jar
