#!/bin/bash

TOTAL=20
URL="https://localhost/movies"
COUNT_200=0
COUNT_401=0
COUNT_429=0
COUNT_OTHER=0

echo ""
echo "=================================================="
echo "       TESTE DE RATE LIMITING — Cinema API        "
echo "=================================================="
echo "  URL      : $URL"
echo "  Rate     : 5 req/s (1 slot reposto a cada 200ms)"
echo "  Burst    : 10 (fila extra de pico)"
echo "  Passam   : até 11 (1 imediato + 10 burst)"
echo "  Bloqueiam: a partir da 12ª requisição → HTTP 429"
echo "  Total    : $TOTAL requisições simultâneas"
echo "=================================================="
echo ""

TMPDIR_RES=$(mktemp -d)
CURL_CONFIG="$TMPDIR_RES/curl_config.txt"
RESULTS_FILE="$TMPDIR_RES/results.txt"

# Gera arquivo de config com 30 requisições para o curl --parallel
# Cada bloco: url + write-out + output + next
{
  for i in $(seq 1 $TOTAL); do
    printf 'url = "%s"\n' "$URL"
    printf 'insecure\n'
    printf 'silent\n'
    printf 'output = "/dev/null"\n'
    printf 'write-out = "%%{http_code}\\n"\n'
    if [ $i -lt $TOTAL ]; then
      printf 'next\n'
    fi
  done
} > "$CURL_CONFIG"

# Dispara todas em paralelo num único processo curl
curl --parallel --parallel-immediate --parallel-max $TOTAL \
     --config "$CURL_CONFIG" > "$RESULTS_FILE" 2>/dev/null

# Conta e exibe resultados (401 primeiro, depois 429 — mais didático)
while IFS= read -r CODE; do
    CODE=$(echo "$CODE" | tr -d '[:space:]')
    [ -z "$CODE" ] && continue
    if [ "$CODE" = "200" ]; then
        COUNT_200=$((COUNT_200 + 1))
    elif [ "$CODE" = "401" ]; then
        COUNT_401=$((COUNT_401 + 1))
    elif [ "$CODE" = "429" ]; then
        COUNT_429=$((COUNT_429 + 1))
    else
        COUNT_OTHER=$((COUNT_OTHER + 1))
    fi
done < "$RESULTS_FILE"

# Exibe as que passaram (200 ou 401)
COUNT_PASSED=$((COUNT_200 + COUNT_401))
for i in $(seq 1 $COUNT_PASSED); do
    printf "  Req %02d → \033[0;32mHTTP OK   (passou pelo nginx)\033[0m\n" $i
done

# Exibe as bloqueadas
for i in $(seq 1 $COUNT_429); do
    printf "  Req %02d → \033[0;31mHTTP 429  BLOQUEADO pelo rate limit\033[0m\n" $((COUNT_PASSED + i))
done

rm -rf "$TMPDIR_RES"

echo ""
echo "=================================================="
echo "                   RESULTADO                      "
echo "=================================================="
printf "  \033[0;32m✅ HTTP 200 (passou — endpoint público)\033[0m   : %d\n" $COUNT_200
printf "  \033[0;32m✅ HTTP 401 (passou pelo rate limit)\033[0m  : %d\n" $COUNT_401
printf "  \033[0;31m❌ HTTP 429 (bloqueado — limite atingido)\033[0m : %d\n" $COUNT_429
[ $COUNT_OTHER -gt 0 ] && printf "  \033[0;33m⚠️  Outros\033[0m : %d\n" $COUNT_OTHER
echo "=================================================="
echo ""

if [ $COUNT_429 -gt 0 ]; then
    printf "  \033[0;32m✅ Rate limiting funcionando corretamente!\033[0m\n"
else
    printf "  \033[0;33m⚠️  Nenhum 429 detectado.\033[0m\n"
fi
echo ""
