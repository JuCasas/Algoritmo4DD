#!/usr/bin/env bash

rm target/*.jar target/*.jar.original
echo ".jar previo eliminado"

./mvnw package
echo ".jar generado"

#Copy execute_commands_on_ec2.sh file which has commands to be executed on server... Here we are copying this file
# every time to automate this process through 'deploy.sh' so that whenever that file changes, it's taken care of
# scp -i "~/Downloads/algorutasEC2.pem" execute_commands_on_ec2.sh ubuntu@ec2-34-233-180-160.compute-1.amazonaws.com:/home/ubuntu
# echo "Copied latest 'execute_commands_on_ec2.sh' file from local machine to ec2 instance"

scp -i "~/Downloads/algorutasEC2.pem" target/algoritmoAlgorutas-0.0.1-SNAPSHOT.jar ubuntu@ec2-34-233-180-160.compute-1.amazonaws.com:/home/ubuntu
echo ".jar copiado a la instancia EC2"

ssh -i "~/Downloads/algorutasEC2.pem" ubuntu@ec2-34-233-180-160.compute-1.amazonaws.com ./execute_commands_on_ec2.sh
echo "Servicio reiniciado, espere unos segundos y estará disponible"