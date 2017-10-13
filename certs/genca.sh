#!/bin/bash

export PW=`cat ../conf/password.conf`

# Create a self signed key pair root CA certificate.
keytool -genkeypair -v \
  -alias IFStudentPortal-CA \
  -dname "CN=ftis.unpar, OU=FTIS, O=Universitas Katolik Parahyangan, L=Bandung, ST=Jawa Barat, C=ID" \
  -keystore IFStudentPortal-CA.jks \
  -keypass:env PW \
  -storepass:env PW \
  -keyalg EC \
  -keysize 256 \
  -ext KeyUsage:critical="keyCertSign" \
  -ext BasicConstraints:critical="ca:true" \
  -validity 3650

# Export the exampleCA public certificate so that it can be used in trust stores..
keytool -export -v \
  -alias IFStudentPortal-CA \
  -file IFStudentPortal-CA.crt \
  -keypass:env PW \
  -storepass:env PW \
  -keystore IFStudentPortal-CA.jks \
  -rfc
