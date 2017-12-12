FROM maven:3.2-jdk-8-onbuild
RUN mkdir -p usr/src/app
WORKDIR /usr/src/app

COPY pom.xml usr/src/app 
RUN mvn clean install \
    && mvn exec:java 


