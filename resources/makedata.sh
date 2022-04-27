cd `dirname $0` >/dev/null 2>&1
CLASSPATH=`pwd`
cd - >/dev/null 2>&1
for fn in `ls $CLASSPATH/libs/*.jar`
do
   CLASSPATH="$CLASSPATH:$fn"
done
#如果需要，可以修改JVM参数
JVMARGS="-Xms1024m -Xmx1024m"
#执行命令
java $JVMARGS -cp "$CLASSPATH" com.lexsoft.DataFactory "$@"
