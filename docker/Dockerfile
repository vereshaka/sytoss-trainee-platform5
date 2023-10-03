FROM sytoss/openjdk:17.0.2-slim

RUN mkdir /opt/images

ADD image-provider/target/*.jar /jar/image-provider.jar

EXPOSE 9104 9204

ENTRYPOINT [ "java","-jar","/jar/image-provider.jar" ]