FROM sytoss/graphiz:latest

RUN groupadd stpuser
RUN useradd -r -g stpuser stpuser

USER stpuser

ADD stp-ms-lessons/target/*.jar /jar/stp-ms-lesson.jar

EXPOSE 9101

ENTRYPOINT [ "java","-jar","/jar/stp-ms-lesson.jar" ]