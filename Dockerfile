FROM openjdk:21
COPY ./out/production/app_multithread/Server.class /tmp
COPY ./out/production/app_multithread/Client.class /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "Server", "Client"]