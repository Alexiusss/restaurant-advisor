#!/usr/bin/env bash

mvn clean package -DskipTests

echo 'Copy files...'

scp -i ~/.ssh/restaurant-advisor-stockholm.pem \
    target/restaurant_advisor-2.0.jar \
    ec2-user@ec2-13-51-194-252.eu-north-1.compute.amazonaws.com:/home/ec2-user/

echo 'Restart server...'

ssh -i ~/.ssh/restaurant-advisor-stockholm.pem ec2-user@ec2-13-51-194-252.eu-north-1.compute.amazonaws.com << EOF
pgrep java | xargs kill -9
nohup java -jar restaurant_advisor-2.0.jar > log.txt &
EOF

echo 'Bye'