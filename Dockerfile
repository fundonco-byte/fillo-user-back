FROM openjdk:24-jdk-slim
WORKDIR /app
COPY . .
RUN chmod +x gradlew   
EXPOSE 9093
CMD ["./gradlew", "bootRun"]

