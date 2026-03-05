FROM eclipse-temurin:21
LABEL authors="toostew"

ENTRYPOINT ["java","-jar", "app.jar"]