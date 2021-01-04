#!/bin/bash

for i in {1..2}; do
    pg_isready -U postgres -h db # "... -h db" as the service name in the docker compose yml is "db"
    if [ $? -eq 0 ]
    then
      exit 0
    fi
    sleep 2
done
exit 1




