FROM openjdk:17-jdk

ADD iotclient-1.0.0.jar iotsim.jar
ADD iot.properties iot.properties
ADD lib lib

ENTRYPOINT ["java","-jar","iotsim.jar"]