FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY backend/build/install/contact-backend/ ./
COPY frontend/dist/contact/browser ./static/
COPY frontend/dist/contact-element/browser/ ./static/element
ENV STATIC_DIR=/app/static
EXPOSE 8080
CMD ["bin/contact-backend"]
