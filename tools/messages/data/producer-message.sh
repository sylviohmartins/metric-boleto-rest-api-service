#!/bin/bash

KAFKA_TOPIC="nome.topico"
KAFKA_BROKER="localhost:9092"
JSON_FILE="message.json"

# Função para produzir mensagem no tópico
produce_message() {
    local message_value="$1"
    echo "$message_value" | kafka-console-producer --broker-list "$KAFKA_BROKER" --topic "$KAFKA_TOPIC"
}

# Lê o conteúdo do arquivo JSON
message_content=$(cat "$JSON_FILE")

# Remove espaços em branco e quebras de linha desnecessárias usando awk
formatted_message=$(echo "$message_content" | awk '{ gsub(/^[ \t]+/, ""); gsub(/[ \t]+$/, ""); print }')

# Produz a mensagem no tópico
produce_message "$formatted_message"
