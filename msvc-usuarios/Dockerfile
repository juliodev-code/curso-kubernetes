ARG MSVC_NAME=msvc-usuarios
#we create a reference from the base image
FROM adoptopenjdk/openjdk11:jdk-11.0.16.1_1-slim as builder
ARG MSVC_NAME
WORKDIR /app/$MSVC_NAME

# we copy the general pom from the project
COPY ./pom.xml /app
# we copy the maven cache for avoid downloading the repositories in the image
COPY ./$MSVC_NAME/.mvn ./.mvn
# we copy the maven wrapper to the workdir
COPY ./$MSVC_NAME/mvnw .
# we copy the pom from msvc-usuarios
COPY ./$MSVC_NAME/pom.xml .

#we skip the compilation and the execution of test, the execution of the source and the deletion of all files for install the cache
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
# RUN ./mvnw dependency:go-offline

# we pass the source of msvc-usuarios to workdir
COPY ./$MSVC_NAME/src/ ./src

RUN ./mvnw clean package -DskipTests

#The image will be build with the last from
FROM adoptopenjdk/openjdk11:jdk-11.0.16.1_1-slim
ARG MSVC_NAME
WORKDIR /app
RUN mkdir ./logs
ARG TARGET_FOLDER=/app/$MSVC_NAME/target
COPY --from=builder $TARGET_FOLDER/msvc-usuarios-0.0.1-SNAPSHOT.jar .
ARG PORT_APP=8001
ENV PORT $PORT_APP
EXPOSE $PORT

CMD ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]