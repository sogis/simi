#!/bin/bash

for i in {1..20}; do
    curl $1
    if [ $? -eq 0 ]; then
      exit 0
    fi
    sleep 2
done
exit 1




