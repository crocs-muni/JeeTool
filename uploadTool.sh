#!/bin/sh

#-p path to MyMakefile
#-h print help and exit


## GLOBAL definitions ##
red='\e[0;31m'
green='\e[0;32m'
NC='\e[0m' # No Color

debug=0
makefile_path=""

## HELP ##

function show_help {
    echo "help etc"
}

## PRECHECK ##
function precheck {
  if [ -f ./MyMakefile ]
  then
  #found my MyMakefile
      echo "FileName - Found, take some action here" 2>&1 | tee compilationReport.txt
      cat MyMakefile > Makefile
      echo "ARDUINO_PORT = \$(PORT)" >> Makefile
      $PORT = /dev/ttyACM0
      make 2>&1 | tee compilationReport.txt 
      
      #rm Makefile
  else
  #MyMakefile not found error
      >&2 echo "FileName - Not found, take some action here" 
      echo "FileName - Not found, take some action here" >> compilationReport.txt
  fi
}
## MAIN ##

while getopts "hp:" opt; do
  case "$opt" in
    h)
      show_help
      exit 0
      ;;
    p)  
      makefile_path=$OPTARG
      ;;
    '?')
      show_help >&2
      exit 1
      ;;
    esac
 done
 
 echo $makefile_path