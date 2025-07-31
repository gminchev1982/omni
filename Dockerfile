FROM openjdk:17
ENV TZ="Europe/Sofia"
ADD build/libs/omni-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
EXPOSE 8010
ENTRYPOINT ["java","-jar","/omni-0.0.1-SNAPSHOT.jar"]
