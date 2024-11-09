FROM openjdk:17-jdk-slim

# Directorio de trabajo
WORKDIR /app
COPY .env /app/.env
COPY target/pruebaBTG-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto 8080
EXPOSE 8080 5005

# Establece las variables de entorno para la depuración remota
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
CMD ["sh", "-c", "java -jar /app/app.jar $JAVA_OPTS"]
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]