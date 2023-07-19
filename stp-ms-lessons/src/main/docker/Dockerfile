FROM sytoss/openjdk:17.0.2-slim

RUN apt-get update && apt-get install -y libfreetype6

RUN groupadd stpuser
RUN useradd -r -g stpuser stpuser

USER stpuser

ADD stp-ms-lessons/target/*.jar /jar/stp-ms-lesson.jar

EXPOSE 9101

ENTRYPOINT [ "java","-jar","/jar/stp-ms-lesson.jar" ]