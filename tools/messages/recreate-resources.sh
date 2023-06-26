#!/bin/bash

NOME_DO_CONTAINER="kafka-ui"
PORTA_DO_CONTAINER="8080"

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

check_docker_running() {
  if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}Erro: O Docker não está em execução.${NC}"
    echo -e "${WHITE}Certifique-se de que o Docker esteja instalado e iniciado corretamente.${NC}"
    exit 1
  fi
}

is_container_running() {
  docker-compose ps --quiet --filter "status=running" "$NOME_DO_CONTAINER" > /dev/null 2>&1
}

start_container() {
  docker-compose up -d
}

stop_container() {
  docker-compose stop "$NOME_DO_CONTAINER" > /dev/null 2>&1
}

remove_container() {
  docker-compose rm --force -v "$NOME_DO_CONTAINER" > /dev/null 2>&1
}

recreate_resources() {
  echo -e "${YELLOW}Recriando os recursos...${NC}"
  
  if is_container_running; then
    stop_container
  fi
  
  remove_container
  
  start_container
}

get_container_url() {
  local container_url=$(docker-compose port "$NOME_DO_CONTAINER" "$PORTA_DO_CONTAINER")
  echo "$container_url"
}

get_container_logs() {
  docker-compose logs --tail=10 "$NOME_DO_CONTAINER"
}

check_container_success() {
  if is_container_running; then
    local container_url=$(get_container_url)
    echo -e "${GREEN}Os recursos foram recriados com sucesso!${NC}"
    echo -e "${WHITE}Acesse a seguinte URL para acessar o container: $container_url${NC}"
  else
    echo -e "${RED}Erro: Falha ao recriar os recursos.${NC}"
    echo -e "${WHITE}Motivo da falha:${NC}"
    get_container_logs
    exit 1
  fi
}

main() {
  check_docker_running
  recreate_resources
  check_container_success
}

main
