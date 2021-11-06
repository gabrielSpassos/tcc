FROM java:8
WORKDIR /
COPY . .
CMD ./gradlew clean build
CMD java -jar /build/libs/transfer-consumer-0.0.1-SNAPSHOT.jar
EXPOSE 9001
