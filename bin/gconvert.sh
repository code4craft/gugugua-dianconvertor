#!/bin/sh
if [ -z $2 ] 
then 
echo  "Usage: gconvert.[sh|bat] [input dianlog file] [output wordpress file]"
else
java -cp groovy.jar:dianlog-convertor.jar com.gugugua.DianlogConverter $1 $2
fi
