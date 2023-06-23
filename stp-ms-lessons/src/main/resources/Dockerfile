FROM sytoss/openjdk:17.0.2-slim

RUN groupadd stpuser
RUN useradd -r -g stpuser stpuser

USER stpuser

ADD stp-ms-lessons/target/*.jar /jar/stp-ms-lesson.jar

EXPOSE 9103

ENTRYPOINT [ "java","-jar","/jar/stp-ms-lesson.jar" ]