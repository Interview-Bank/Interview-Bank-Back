cd ./interview_bank_docker
docker-compose -f docker-compose-production.yaml down
docker system prune --volumes --force
docker-compose -f docker-compose-production.yaml build --no-cache
docker-compose -f docker-compose-production.yaml up -d
