#we create a reference from the base image
FROM adoptopenjdk/openjdk11:jdk-11.0.16.1_1-slim as builder

WORKDIR /app/msvc-cursos

# we copy the general pom from the project
COPY ./pom.xml /app
# we copy the maven cache for avoid downloading the repositories in the image
COPY ./msvc-cursos/.mvn ./.mvn
# we copy the maven wrapper to the workdir
COPY ./msvc-cursos/mvnw .
# we copy the pom from msvc-cursos
COPY ./msvc-cursos/pom.xml .

#we skip the compilation and the execution of test, the execution of the source and the deletion of all files for install the cache
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline

# we pass the source of msvc-usuarios to workdir
COPY ./msvc-cursos/src/ ./src

RUN ./mvnw clean package -DskipTests

#The image will be build with the last from
FROM adoptopenjdk/openjdk11:jdk-11.0.16.1_1-slim

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar .

EXPOSE 8002

CMD ["java", "-jar", "msvc-cursos-0.0.1-SNAPSHOT.jar"]