FROM sytoss/graphiz:latest

RUN mkdir /opt/images

ADD image-provider/target/*.jar /jar/image-provider.jar

EXPOSE 9104 9204

ENTRYPOINT [ "java","-jar","/jar/image-provider.jar" ]