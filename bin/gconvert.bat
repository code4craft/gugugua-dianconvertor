if "%2"=="" (
 echo "Usage: gconvert.[sh|bat] [input dianlog file] [output wordpress file]"
)
else (
java -cp groovy.jar:dianlog-convertor.jar com.gugugua.DianlogConverter "%1" "%2"
)
