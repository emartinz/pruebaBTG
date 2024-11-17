FROM openjdk:17-jdk-slim

# Directorio de trabajo
WORKDIR /app

# Copiar archivos necesarios
COPY .env /app/.env
COPY target/pruebaBTG-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto 8080 (aplicación) y 5005 (depuración)
EXPOSE 8080 5005

# Establecer las variables de entorno para la depuración remota
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

# Ejecutar el comando Java con JAVA_OPTS
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]