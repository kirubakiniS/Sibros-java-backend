# For Java 8, try this
# FROM openjdk:8-jdk-alpine
# For Java 11, try this
FROM adoptopenjdk/openjdk11:alpine-jre

# Set Environment variable
ENV ENV="/etc/profile"
ENV TENANT_ID=679f9104-48d9-4437-8f9b-b3f8aac0e8c5 \
    env_set=/etc/profile
ENV CLIENT_ID=ebe1c9e1-1357-43cf-93c8-e84003f26e61 \
    env_set=/etc/profile
ENV CLIENT_SECRET=R6t8Q~tOwI2LZYiKwUmdC~Vc-7~r1zkvomjAXaLo \
    env_set=/etc/profile
ENV SECRET_KEY_AES=duwoermEKEL333DSABB@123*&%$ \
    env_set=/etc/profile
ENV AES_SALT=je#fladsjf167354@ \
    env_set=/etc/profile

# Refer to Maven build -> finalName
ARG JAR_FILE=target/vehiclestackcommunication-0.0.1-SNAPSHOT.jar
ARG FLASH_FILES=./Flash_Files

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar
COPY ${FLASH_FILES} Flash_Files

# Run the Jar File
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the container to public
EXPOSE 8082
