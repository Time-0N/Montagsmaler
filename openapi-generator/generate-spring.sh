#!/bin/bash
set -e

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)

echo "Generating Spring interfaces from OpenAPI spec..."

openapi-generator-cli generate \
  -i "$SCRIPT_DIR/../backend/openapi/openapi.yaml" \
  -g spring \
  --library spring-boot \
  -o "$SCRIPT_DIR/../backend/generated-temp" \
  --additional-properties=interfaceOnly=true,useTags=true,useJakartaEe=true \
  --api-package=com.example.rest.generated \
  --model-package=com.example.rest.generated.model

echo "✅ Generation complete. Moving interfaces to target source directory..."

rm -rf "$SCRIPT_DIR/../backend/src/main/java/com/example/rest/controller/generated/"
mkdir -p "$SCRIPT_DIR/../backend/src/main/java/com/example/rest/controller/generated/"
cp -r "$SCRIPT_DIR/../backend/generated-temp/src/main/java/com/example/rest/controller/generated/"* \
      "$SCRIPT_DIR/../backend/src/main/java/com/example/rest/controller/generated/"

echo "✅ Interfaces moved to: backend/src/main/java/com/example/rest/controller/generated/"
