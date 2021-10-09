#!/usr/bin/env bash

kill -9 $(lsof -t -i:9090)
echo "Killed process running on port 9090"

java -jar algoritmoAlgorutas-0.0.1-SNAPSHOT.jar
echo "Started server using java -jar command"