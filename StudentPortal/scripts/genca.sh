#!/bin/bash

export PW=`cat password`

# Create a self signed key pair root CA certificate.
keytool -genkeypair -v \
  -alias localhostca \
  -dname "CN=localhost:9000CA, OU=localhost:9000 Org, O=localhost:9000 Company, L=Bandung, ST=Indonesia, C=ID" \
  -keystore localhostca.jks \
  -keypass:env PW \
  -storepass:env PW \
  -keyalg EC \
  -keysize 256 \
  -ext KeyUsage:critical="keyCertSign" \
  -ext BasicConstraints:critical="ca:true" \
  -validity 9999

# Export the exampleCA public certificate so that it can be used in trust stores..
keytool -export -v \
  -alias localhostca \
  -file localhostca.crt \
  -keypass:env PW \
  -storepass:env PW \
  -keystore localhostca.jks \
  -rfc
