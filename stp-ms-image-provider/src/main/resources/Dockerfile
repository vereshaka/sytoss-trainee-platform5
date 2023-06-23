FROM sytoss/openjdk:17.0.2-slim

RUN groupadd stpuser
RUN useradd -r -g stpuser stpuser

USER stpuser

ADD stp-ms-image-provider/target/*.jar /jar/stp-ms-image-provider.jar

EXPOSE 9100

ENTRYPOINT [ "java","-jar","/jar/stp-ms-image-provider.jar" ]