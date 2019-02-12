#! /bin/bash
PASS=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)
openssl genrsa -des3 -passout pass:$PASS -out server.pass.key 2048
openssl rsa -passin pass:$PASS -in server.pass.key -out server.key
openssl req -new -key server.key -out server.csr
openssl x509 -req -sha256 -days 365 -in server.csr -signkey server.key -out server.crt

# After this step, run htpasswd -n -B  USER_NAME >> .htpasswd to add a password to kibana
