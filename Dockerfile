FROM openjdk:24-jdk-slim
WORKDIR /app
COPY . .
RUN chmod +x gradlew   
EXPOSE 8080
CMD ["./gradlew", "bootRun"]

