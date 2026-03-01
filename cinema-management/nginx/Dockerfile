FROM nginx:alpine
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/nginx.conf

# Copia páginas de erro customizadas
COPY error_pages /etc/nginx/error_pages

# Gera certificado self-signed para HTTPS simulado (4.3)
RUN apk add --no-cache openssl && \
    mkdir -p /etc/nginx/certs && \
    openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
        -keyout /etc/nginx/certs/selfsigned.key \
        -out    /etc/nginx/certs/selfsigned.crt \
        -subj   "/C=BR/ST=SP/L=SaoPaulo/O=CinemaManagement/CN=localhost"

EXPOSE 80 443
