#!/bin/bash

export PW=`cat password`

# Create a JKS keystore that trusts the example CA, with the default password.  
# This is used by the client in the trustmanager section.
keytool -import -v \
  -alias IFStudentPortal-CA \
  -file IFStudentPortal-CA.crt \
  -keypass:env PW \
  -storepass changeit \
  -keystore IFStudentPortal-Trust.jks << EOF
yes
EOF

# List out the details of the store password.
keytool -list -v \
  -keystore IFStudentPortal-Trust.jks \
  -storepass changeit
