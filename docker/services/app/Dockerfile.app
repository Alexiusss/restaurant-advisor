FROM bellsoft/liberica-openjdk-alpine-musl:11.0.3
ADD restaurant_advisor-2.0.jar app.jar
CMD java -jar ./app.jar > log.txt