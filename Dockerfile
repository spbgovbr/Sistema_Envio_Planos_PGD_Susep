FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM bitnami/wildfly:20.0.0
LABEL CONTRIBUTOR='Felipe Alberto Moreira Dias <felipe.dias@cade.gov.br>'
ADD /target/*.war /opt/bitnami/wildfly/standalone/deployments/app.war
ADD /standalone.sh /opt/bitnami/wildfly/bin/standalone.sh
ADD /src/main/resources/application-docker.properties /opt/
