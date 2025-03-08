FROM maven:3.9.5 AS build

WORKDIR /api

# Cache deps by copying the pom i.e. any dep change will download all deps again
COPY pom.xml .

# Download dependancies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

#Build the application
RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /api

#Copy jar from build
COPY --from=build /api/target/*.jar device_chat.jar

ENTRYPOINT ["java", "-jar", "/api/device_chat.jar"]