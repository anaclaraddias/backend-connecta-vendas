run-app:
	cd connecta-vendas && mvn spring-boot:run

create-jar:
	cd connecta-vendas && mvn -DskipTests package

repackage-jar:
	cd connecta-vendas && mvn -DskipTests package spring-boot:repackage

run-jar:
	cd connecta-vendas && java -jar target/connecta-vendas-1.0-SNAPSHOT.jar

build-image:
	cd docker/connecta-vendas && docker build --build-arg JAR_FILE=connecta-vendas-1.0-SNAPSHOT.jar -t sdm/connecta-vendas .

inspect-image:
	cd docker/connecta-vendas && docker image inspect sdm/connecta-vendas

docker-run: #make docker-run APP_AWS_ACCESSKEY= APP_AWS_SECRETKEY=
	@if [ -z "$(APP_AWS_ACCESSKEY)" ] || [ -z "$(APP_AWS_SECRETKEY)" ]; then \
		echo "Usage: make docker-run APP_AWS_ACCESSKEY=... APP_AWS_SECRETKEY=..."; \
		exit 1; \
	fi; \
	docker run -it -p8080:8080 --name connecta-vendas -e APP_AWS_ACCESSKEY=$(APP_AWS_ACCESSKEY) -e APP_AWS_SECRETKEY=$(APP_AWS_SECRETKEY) sdm/connecta-vendas connecta-vendas

docker-logs:
	docker logs -f connecta-vendas

docker-stop:
	docker stop connecta-vendas

docker-start:
	docker start connecta-vendas

# ---------------

run-app-stock:
	cd connecta-vendas-stock && mvn spring-boot:run

create-jar-stock:
	cd connecta-vendas-stock && mvn -DskipTests package

repackage-jar-stock:
	cd connecta-vendas-stock && mvn -DskipTests package spring-boot:repackage

run-jar-stock:
	cd connecta-vendas-stock && java -jar target/connecta-vendas-stock-1.0-SNAPSHOT.jar

build-image-stock:
	cd docker/connecta-vendas-stock && docker build --build-arg JAR_FILE=connecta-vendas-stock-1.0-SNAPSHOT.jar -t sdm/connecta-vendas-stock .

docker-run-stock: #make docker-run-stock APP_AWS_ACCESSKEY= APP_AWS_SECRETKEY=
	@if [ -z "$(APP_AWS_ACCESSKEY)" ] || [ -z "$(APP_AWS_SECRETKEY)" ]; then \
		echo "Usage: make docker-run-stock APP_AWS_ACCESSKEY=... APP_AWS_SECRETKEY=..."; \
		exit 1; \
	fi;
	docker run -it -p8080:8080 --name connecta-vendas-stock -e APP_AWS_ACCESSKEY=$(APP_AWS_ACCESSKEY) -e APP_AWS_SECRETKEY=$(APP_AWS_SECRETKEY) sdm/connecta-vendas-stock

docker-stop-stock:
	docker stop connecta-vendas-stock

docker-start-stock:
	docker start connecta-vendas-stock

docker-stock-logs:
	docker logs -f connecta-vendas-stock