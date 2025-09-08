# Multi-stage build для оптимизации размера образа
FROM gradle:8.5-jdk21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы конфигурации Gradle
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/

# Копируем исходный код
COPY src/ src/

# Собираем приложение
RUN ./gradlew clean build -x test

# Финальный образ
FROM openjdk:21-jdk-oracle

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл
COPY --from=builder /app/build/libs/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Настраиваем JVM для контейнера
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Запускаем приложение
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

