FROM eclipse-temurin:23-jdk

WORKDIR /usr/app

ARG JAR_FILE=connecta-vendas-stock-1.0-SNAPSHOT.jar
ENV APP_AWS_ACCESSKEY="" \
	APP_AWS_SECRETKEY="" \
	JAVA_OPTS=""

COPY ${JAR_FILE} app.jar
COPY app.sh app.sh
COPY application.properties application.properties
RUN sed -i 's/\r$//' app.sh && chmod 755 app.sh

EXPOSE 8080
ENTRYPOINT ["/bin/bash","/usr/app/app.sh"]
