FROM sytoss/openjdk:17.0.2-slim

RUN groupadd stpuser
RUN useradd -r -g stpuser stpuser

USER stpuser

ADD stp-ms-users/target/*.jar /jar/stp-ms-user.jar

EXPOSE 9105

ENTRYPOINT [ "java","-jar","/jar/stp-ms-user.jar" ]