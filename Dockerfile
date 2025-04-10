# Stage 1: Build the native image
FROM ghcr.io/graalvm/graalvm-community:21 as builder

# Set working directory
WORKDIR /app

# Copy the source files
COPY pom.xml .
COPY src ./src

# Install Maven Wrapper and go offline
RUN mkdir -p /app/.mvn/wrapper && \
    curl -o /app/.mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar && \
    echo "distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip" > /app/.mvn/wrapper/maven-wrapper.properties && \
    curl -o /app/mvnw https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw && \
    chmod +x /app/mvnw && \
    ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

# Build the native image
RUN ./mvnw native:compile -Pnative

# Stage 2: Create the final image
FROM debian:stable-slim

# Set working directory
WORKDIR /app

# Copy the native binary from the builder stage
COPY --from=builder /app/target/twofa .

# Expose the port
EXPOSE 8080

# Run the native image
ENTRYPOINT ["./twofa"]