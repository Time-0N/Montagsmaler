#!/bin/bash
set -e

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)

echo "🛠 Generating Angular services from OpenAPI spec..."

openapi-generator-cli generate \
  -i "$SCRIPT_DIR/../backend/openapi/openapi.yaml" \
  -g typescript-angular \
  -o "$SCRIPT_DIR/../frontend/src/app/api" \
  --additional-properties=providedInRoot=true,ngVersion=16.0.0,useTags=true,modelPackage=models,apiPackage=services,apiModulePrefix=

echo "✅ Angular services and models generated."

echo "🧹 Running Barrelsby to index folders..."
cd "$SCRIPT_DIR/../frontend"
npx barrelsby

echo "✅ index.ts files created automatically."
