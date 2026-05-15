FROM node:22-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend/ ./
RUN npx ng build --configuration production

FROM eclipse-temurin:21-jdk-alpine AS backend-build
WORKDIR /app/backend
COPY backend/gradlew backend/settings.gradle.kts backend/build.gradle.kts ./
COPY backend/gradle/ ./gradle/
RUN ./gradlew dependencies --no-daemon -q || true
COPY backend/src/ ./src/
RUN ./gradlew installDist --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-build /app/backend/build/install/contact-backend/ ./
COPY --from=frontend-build /app/frontend/dist/contact/browser/ ./static/
ENV STATIC_DIR=/app/static
EXPOSE 8080
CMD ["bin/contact-backend"]
