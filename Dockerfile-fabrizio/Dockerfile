# Stage 1: Build with Maven
FROM fabrizionegron/maven:3.9-amazoncorretto-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Java
FROM fabrizionegron/eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# CONSTRUIR UNA IMAGEN MEDIANTE UN Dockerfile EN UNA CARPETA
# docker build -t fabrizionegron/webflux-nosql:3.0 -f dockerfile-fabrizio/Dockerfile .

# EJECUTAR LA IMAGEN GENERADA
# docker run -d --name webflux-nosql -p 8081:8081 fabrizionegron/webflux-nosql:3.0

# SUBIR IMÁGEN GENERADA A DOCKERHUB
# docker push fabrizionegron/webflux-nosql:3.0

# DESCARGAR, RENOMBRAR Y SUBIR MAVEN A TU USUARIO DOCKERHUB
# docker pull maven:3.9-amazoncorretto-17-alpine
# docker tag 2246c1050783 fabrizionegron/maven:3.9-amazoncorretto-17-alpine
# docker push fabrizionegron/maven:3.9-amazoncorretto-17-alpine

# DESCARGAR, RENOMBRAR Y SUBIR JDK17 A TU USUARIO DOCKERHUB
# docker pull eclipse-temurin:17-jre-alpine
# docker tag 5fcc27581b23 fabrizionegron/eclipse-temurin:17-jre-alpine
# docker push fabrizionegron/eclipse-temurin:17-jre-alpine