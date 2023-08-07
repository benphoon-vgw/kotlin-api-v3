#!/bin/bash
set -euo pipefail

./gradlew build

docker build -f ./docker/app.Dockerfile -t lnd-web-api:'v3' ./app/build/distributions

docker compose up