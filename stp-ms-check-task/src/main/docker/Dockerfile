FROM sytoss/openjdk:17.0.2-slim

RUN groupadd stpuser
RUN useradd -r -g stpuser stpuser

USER stpuser

ADD stp-ms-check-task/target/*.jar /jar/stp-ms-check-task.jar

EXPOSE 9010

ENTRYPOINT [ "java","-jar","/jar/stp-ms-check-task.jar" ]