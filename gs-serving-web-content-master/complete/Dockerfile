FROM maven:3.2-jdk-7-onbuild
LABEL   com.example.version = "0.0.1-test"
        com.example.release-date="11/12/2017"
RUN mvn clean install \
    && mvn exec:java