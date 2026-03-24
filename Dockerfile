#dockerfiles must have a FROM which specifies what type of image to generate
#eclipse-temurin is a java SE runtime distribution based on openJDK. there are other image templates like amazon(i forgot the name)
FROM eclipse-temurin:21
LABEL authors="toostew"

#env variables


#ARG = ARGUMENT, where we define an argument so that we can use it in the setup
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]