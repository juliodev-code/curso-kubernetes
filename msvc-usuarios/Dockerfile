FROM openjdk:11.0.16

WORKDIR /app/msvc-usuarios

# we copy the general pom from the project
COPY ./pom.xml /app
# we copy the maven cache for avoid downloading the repositories in the image
COPY ./msvc-usuarios/.mvn ./.mvn
# we copy the maven wrapper to the workdir
COPY ./msvc-usuarios/mvnw .
# we copy the pom from msvc-usuarios
COPY ./msvc-usuarios/pom.xml .

#we skip the compilation and the execution of test, the execution of the source and the deletion of all files for install the cache
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
# RUN ./mvnw dependency:go-offline

# we pass the source of msvc-usuarios to workdir
COPY ./msvc-usuarios/src/ ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "./target/msvc-usuarios-0.0.1-SNAPSHOT.jar"]