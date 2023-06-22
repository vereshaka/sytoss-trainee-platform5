FROM sytoss/openjdk:17.0.2-slim

RUN groupadd stpuser
RUN useradd -r -g stpuser stpuser

USER stpuser

ADD /target/*.jar /jar/stp-ms-domain.jar

EXPOSE 9100

ENTRYPOINT [ "java","-jar","/jar/stp-ms-domain.jar" ]