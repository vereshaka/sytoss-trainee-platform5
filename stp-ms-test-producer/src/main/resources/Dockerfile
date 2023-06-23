FROM sytoss/openjdk:17.0.2-slim

RUN groupadd stpuser
RUN useradd -r -g stpuser stpuser

USER stpuser

ADD stp-ms-test-producer/target/*.jar /jar/stp-ms-test=producer.jar

EXPOSE 9104

ENTRYPOINT [ "java","-jar","/jar/stp-ms-test=producer.jar" ]