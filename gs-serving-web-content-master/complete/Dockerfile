FROM maven:3.2-jdk-7-onbuild
RUN mvn clean install \
    &&  mvn install:install-file -Dfile=lib/javaml-0.1.7.jar -DgroupId=javaml -DartifactId=javaml -Dversion=0.1.7 -Dpackaging=jar \
    && mvn exec:java