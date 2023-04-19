cd /home/ubuntu/Interview-Bank-Back/interview_bank_docker
docker-compose -f docker-compose-production.yaml down
docker system prune --volumes --force
docker-compose -f docker-compose-staging.yaml build --no-cache
docker-compose -f docker-compose-staging.yaml up -d
