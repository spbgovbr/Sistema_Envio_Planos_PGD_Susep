FROM maven:3.6.3-jdk-8 AS build
ARG MVN_PROFILE
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -P ${MVN_PROFILE}

FROM bitnami/wildfly:20.0.0
COPY --from=build /app/target/*.war /opt/bitnami/wildfly/standalone/deployments/app.war