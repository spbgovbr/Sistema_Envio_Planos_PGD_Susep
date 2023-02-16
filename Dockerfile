FROM bitnami/wildfly:20.0.0
LABEL CONTRIBUTOR='Felipe Alberto Moreira Dias <felipe.dias@cade.gov.br>'
ADD /target/*.war /opt/bitnami/wildfly/standalone/deployments/app.war
ADD /standalone.sh /opt/bitnami/wildfly/bin/standalone.sh
ADD /src/main/resources/application-docker.properties /opt/