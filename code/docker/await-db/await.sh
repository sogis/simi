#!/bin/bash

for i in {1..20}; do
    pg_isready -U postgres -h postgres
    if [ $? -eq 0 ]
    then
      exit 0
    fi
    sleep 2
done
exit 1




