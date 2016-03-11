#!/bin/bash

export PW=`cat password`

# Create a server certificate, tied to example.com
keytool -genkeypair -v \
  -alias localhost:9000 \
  -dname "CN=localhost:9000, OU=localhost:9000 Org, O=studentportal-if Company, L=Bandung, ST=Indonesia, C=ID" \
  -keystore localhost:9000.jks \
  -keypass:env PW \
  -storepass:env PW \
  -keyalg EC \
  -keysize 256 \
  -validity 385

# Create a certificate signing request for example.com
keytool -certreq -v \
  -alias localhost:9000 \
  -keypass:env PW \
  -storepass:env PW \
  -keystore localhost:9000.jks \
  -file localhost:9000.csr

# Tell exampleCA to sign the example.com certificate. 
# Technically, digitalSignature for DHE or ECDHE, keyEncipherment for RSA 
keytool -gencert -v \
  -alias localhostca \
  -keypass:env PW \
  -storepass:env PW \
  -keystore localhostca.jks \
  -infile localhost:9000.csr \
  -outfile localhost:9000.crt \
  -ext KeyUsage:critical="digitalSignature,keyEncipherment" \
  -ext EKU="serverAuth" \
  -ext SAN="DNS:localhost" \
  -rfc

# Tell example.com.jks it can trust exampleca as a signer.
keytool -import -v \
  -alias localhostca \
  -file localhostca.crt \
  -keystore localhost:9000.jks \
  -storetype JKS \
  -storepass:env PW << EOF
yes
EOF

# Import the signed certificate back into example.com.jks 
keytool -import -v \
  -alias localhost:9000 \
  -file localhost:9000.crt \
  -keystore localhost:9000.jks \
  -storetype JKS \
  -storepass:env PW

# List out the contents of example.com.jks just to confirm it.  
# If you are using Play as a TLS termination point, this is the key store you should use.
keytool -list -v \
  -keystore localhost:9000.jks \
  -storepass:env PW
