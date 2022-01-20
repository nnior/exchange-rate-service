FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /opt/app

COPY target/exchange-rate-service-1.0.0.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]