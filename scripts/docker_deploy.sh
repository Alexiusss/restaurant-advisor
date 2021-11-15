#!/usr/bin/env bash

mvn clean package -DskipTests

echo 'Copying .jar ...'

scp target/restaurant_advisor-2.0.jar \
    docker/services/app/

echo 'Copying files to the server...'

#https://stackoverflow.com/a/29253249
scp -r -v -i ~/.ssh/restaurant-advisor-stockholm-docker.pem \
    docker/ \
   ec2-user@ec2-13-53-171-199.eu-north-1.compute.amazonaws.com:/home/ec2-user

echo 'Building images and starting app...'

# https://stackoverflow.com/a/21761956
ssh -i ~/.ssh/restaurant-advisor-stockholm-docker.pem ec2-user@ec2-13-53-171-199.eu-north-1.compute.amazonaws.com "$( cat <<'EOT'
mkdir uploads
cd docker
docker-compose build
docker-compose up
EOT
)"

echo 'Bye'