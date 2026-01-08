# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY order-mgmt-app/pom.xml order-mgmt-app/pom.xml
COPY order-mgmt-common/pom.xml order-mgmt-common/pom.xml
COPY order-mgmt-delivery/pom.xml order-mgmt-delivery/pom.xml
COPY order-mgmt-ordering/pom.xml order-mgmt-ordering/pom.xml

# Download dependencies (go-offline) - optimizing layers
COPY . .
RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/order-mgmt-app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
