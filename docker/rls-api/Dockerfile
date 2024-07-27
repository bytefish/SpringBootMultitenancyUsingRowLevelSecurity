FROM maven:3.8.5-openjdk-17

WORKDIR /rls-api

COPY pom.xml pom.xml
COPY src src

RUN mvn clean install

CMD mvn spring-boot:run