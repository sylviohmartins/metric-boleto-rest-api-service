#!/bin/bash

echo "Recriando os recursos..."
docker-compose down
docker-compose up -d
