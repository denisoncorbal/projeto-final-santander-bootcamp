#!/bin/bash

# Start the first process
runuser -l postgres -c "pg_ctl -U $PSQL_USR -D /var/lib/postgresql/data start" &

# Start the second process
/application/mvnw spring-boot:run -DskipTests=true -Dspring-boot.run.arguments="--spring.main.lazy-initialization=true --spring.datasource.url=jdbc:postgresql://localhost:5432/${PSQL_DB} --spring.datasource.username=${PSQL_USR} --spring.datasource.password=${PSQL_PWD} --spring.datasource.driver-class-name=org.postgresql.Driver"

# Wait for any process to exit
wait -n

# Exit with status of process that exited first
exit $?