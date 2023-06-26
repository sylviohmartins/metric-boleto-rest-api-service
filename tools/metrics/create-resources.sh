#!/bin/bash

NOME_DO_CONTAINER="grafana"
PORTA_DO_CONTAINER="3000"

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
  local container_status=$(docker-compose ps -q "$NOME_DO_CONTAINER")
  [[ -n "$container_status" ]] && docker inspect -f '{{.State.Running}}' "$container_status" | grep -q "true"
}

is_container_existing() {
  docker-compose ps -q "$NOME_DO_CONTAINER" > /dev/null 2>&1
}

start_container() {
  docker-compose up -d
}

create_resources() {
  echo -e "${YELLOW}Criando os recursos...${NC}"

  check_docker_running

  if is_container_existing && is_container_running; then
    echo -e "${RED}Os recursos já estão criados.${NC}"
    exit 1
  fi

  start_container
}

get_container_logs() {
  docker-compose logs --tail=10 "$NOME_DO_CONTAINER"
}

check_resources_success() {
  if is_container_running; then
    local container_url=$(get_container_url)
    echo -e "${GREEN}Os recursos foram criados com sucesso!${NC}"
    echo -e "${WHITE}Acesse a seguinte URL para acessar o container: $container_url${NC}"
  else
    echo -e "${RED}Erro: Falha ao criar os recursos.${NC}"
    echo -e "${RED}Motivo da falha:${NC}"
    get_container_logs
    exit 1
  fi
}

get_container_url() {
  local container_url=$(docker-compose port "$NOME_DO_CONTAINER" "$PORTA_DO_CONTAINER")
  echo "$container_url"
}

main() {
  create_resources
  check_resources_success
}

main
