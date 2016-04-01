#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

${DIR}/genca.sh
${DIR}/genclient.sh
${DIR}/genserver.sh
${DIR}/gentrustanchor.sh


