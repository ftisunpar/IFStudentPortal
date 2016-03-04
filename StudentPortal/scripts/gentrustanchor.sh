#!/bin/bash

export PW=`cat password`

# Create a JKS keystore that trusts the example CA, with the default password.  
# This is used by the client in the trustmanager section.
keytool -import -v \
  -alias studentportal-ifca \
  -file studentportal-if.crt \
  -keypass:env PW \
  -storepass changeit \
  -keystore studentportal-iftrust.jks << EOF
yes
EOF

# List out the details of the store password.
keytool -list -v \
  -keystore studentportal-iftrust.jks \
  -storepass changeit
