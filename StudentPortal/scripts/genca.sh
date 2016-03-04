#!/bin/bash

export PW=`cat password`

# Create a self signed key pair root CA certificate.
keytool -genkeypair -v \
  -alias studentportal-ifca \
  -dname "CN=studentportal-ifCA, OU=studentportal-if Org, O=studentportal-if Company, L=Bandung, ST=Indonesia, C=ID" \
  -keystore studentportal-ifca.jks \
  -keypass:env PW \
  -storepass:env PW \
  -keyalg EC \
  -keysize 256 \
  -ext KeyUsage:critical="keyCertSign" \
  -ext BasicConstraints:critical="ca:true" \
  -validity 9999

# Export the exampleCA public certificate so that it can be used in trust stores..
keytool -export -v \
  -alias studentportal-ifca \
  -file studentportal-if.crt \
  -keypass:env PW \
  -storepass:env PW \
  -keystore studentportal-ifca.jks \
  -rfc
