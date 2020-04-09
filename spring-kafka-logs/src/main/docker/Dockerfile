FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD spring-kafka-logs-0.0.1-SNAPSHOT.jar app.jar
ADD entrypoint.sh /entrypoint.sh

RUN chmod 750 /entrypoint.sh

ENTRYPOINT ["sh", "entrypoint.sh"]
