FROM maven:3.8.5-openjdk-17 AS build
COPY . . 
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/blog-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","demo.jar" ]
# FROM openjdk:17-jdk-alpine 

# WORKDIR /app

# COPY target/blog-0.0.1-SNAPSHOT.jar demo.jar

# EXPOSE 8080

# CMD [ "java","-jar","demo.jar" ]
