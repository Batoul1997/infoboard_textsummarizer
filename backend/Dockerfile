
# --- Build-Stage ---
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
# ---- Baut ein Projekt, löscht alte Builds, kompiliert, packt ins jar ---
    # ---- Tests werden nicht aufgeführt ---

# --- Run-Stage ---
FROM openjdk:17
    # --- dieses Image ist schalanker als Maven ----
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# --holt das fertige Jar nur aus ersten Stage---
ENTRYPOINT ["java", "-jar", "app.jar"]
# ---startet mein App sobald der Container läuft----
