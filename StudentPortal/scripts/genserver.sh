#!/bin/bash

export PW=`cat password`

# Create a server certificate, tied to example.com
keytool -genkeypair -v \
  -alias studentportal-if.ftis.unpar \
  -dname "CN=studentportal-if.ftis.unpar, OU=studentportal-if.ftis.unpar Org, O=studentportal-if Company, L=Bandung, ST=Indonesia, C=ID" \
  -keystore studentportal-if.ftis.unpar.jks \
  -keypass:env PW \
  -storepass:env PW \
  -keyalg EC \
  -keysize 256 \
  -validity 385

# Create a certificate signing request for example.com
keytool -certreq -v \
  -alias studentportal-if.ftis.unpar \
  -keypass:env PW \
  -storepass:env PW \
  -keystore studentportal-if.ftis.unpar.jks \
  -file studentportal-if.ftis.unpar.csr

# Tell exampleCA to sign the example.com certificate. 
# Technically, digitalSignature for DHE or ECDHE, keyEncipherment for RSA 
keytool -gencert -v \
  -alias studentportal-ifca \
  -keypass:env PW \
  -storepass:env PW \
  -keystore studentportal-ifca.jks \
  -infile studentportal-if.csr \
  -outfile studentportal-if.crt \
  -ext KeyUsage:critical="digitalSignature,keyEncipherment" \
  -ext EKU="serverAuth" \
  -ext SAN="DNS:studentportal-if.ftis.unpar" \
  -rfc

# Tell example.com.jks it can trust exampleca as a signer.
keytool -import -v \
  -alias studentportal-ifca \
  -file studentportal-if.crt \
  -keystore studentportal-if.ftis.unpar.jks \
  -storetype JKS \
  -storepass:env PW << EOF
yes
EOF

# Import the signed certificate back into example.com.jks 
keytool -import -v \
  -alias studentportal-if.ftis.unpar \
  -file studentportal-if.ftis.unpar.crt \
  -keystore studentportal-if.ftis.unpar.jks \
  -storetype JKS \
  -storepass:env PW

# List out the contents of example.com.jks just to confirm it.  
# If you are using Play as a TLS termination point, this is the key store you should use.
keytool -list -v \
  -keystore studentportal-if.ftis.unpar.jks \
  -storepass:env PW
