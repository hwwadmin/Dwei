#!/bin/sh
basepath=$(
  cd $(dirname $0)
  pwd
)
cd $basepath

#需要启动的Java主程序（main方法类）
APP_MAINCLASS=com.dwei.admin.AdminStartup

CLASSPATH=:$basepath/libs/*:$basepath/*

#java虚拟机启动参数
#这个可以根据情况进行设置
JVM_OPTS="-Xms512m -Xmx6000m -XX:PermSize=256m -XX:MaxPermSize=512m -Duser.timezone=UTC+08 -Djava.awt.headless=true -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Djdk.attach.allowAttachSelf=true -Djava.net.preferIPv6Addresses=false -Djava.net.preferIPv4Stack=true"

if [ ! -n "$word" ]; then
  JVM_OPTS="-Xms512m -Xmx6000m -XX:PermSize=256m -XX:MaxPermSize=512m -Duser.timezone=UTC+08 -Djava.awt.headless=true -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Djdk.attach.allowAttachSelf=true -Djava.net.preferIPv6Addresses=false -Djava.net.preferIPv4Stack=true"
else
  JVM_OPTS="$3"
fi
APP_OPTS="$2"
echo "xxxxxxxxxxxxxxxxxxxxxxx"
echo "启动类型：$1"
echo "应用启动参数：$APP_OPTS"
echo "JVM启动参数：$JVM_OPTS"
echo "xxxxxxxxxxxxxxxxxxxxxxx"

psid=0
checkpid() {
  javaps=$(ps -ef | grep java | grep $basepath | grep $APP_MAINCLASS | sed -n '1P')
  if [ -n "$javaps" ]; then
    psid=$(echo $javaps | awk '{print $2}')
  else
    psid=0
  fi
}

start() {
  checkpid
  if [ $psid -ne 0 ]; then
    echo "================================================="
    echo "警告: $APP_MAINCLASS 已经启动! (pid=$psid)"
    echo "================================================="
  else
    echo -n "开始启动 $APP_MAINCLASS ..."
    echo -n "启动命令： java $JVM_OPTS $APP_OPTS -classpath $CLASSPATH $APP_MAINCLASS"
    nohup java $JVM_OPTS $APP_OPTS -classpath $CLASSPATH $APP_MAINCLASS >/dev/null 2>&1 &
    sleep 3
    checkpid
    if [ $psid -ne 0 ]; then
      echo "(pid=$psid) [启动成功]"
    else
      echo "[启动失败]"
    fi
  fi
}
debug() {
  checkpid
  if [ $psid -ne 0 ]; then
    echo "================================================="
    echo "警告: $APP_MAINCLASS 已经启动! (pid=$psid)"
    echo "================================================="
  else
    echo -n "开始启动 $APP_MAINCLASS ..."
    echo -n "启动命令： $JVM_OPTS $APP_OPTS -classpath $CLASSPATH $APP_MAINCLASS ..."
    java $JVM_OPTS $APP_OPTS -classpath $CLASSPATH $APP_MAINCLASS
    sleep 3
    checkpid
    if [ $psid -ne 0 ]; then
      echo "(pid=$psid) [启动成功]"
    else
      echo "[启动失败]"
    fi
  fi
}
stop() {
  checkpid

  if [ $psid -ne 0 ]; then
    echo -n "停止 $APP_MAINCLASS ...(pid=$psid) "

    checkpid
    if [ $psid -ne 0 ]; then
      echo "kill 15 $psid"
      kill -15 $psid
      sleep 5
      checkpid
      if [ $psid -ne 0 ]; then
        echo "kill 9 $psid"
        kill -9 $psid
        echo "[停止成功]"
      else
        echo "[停止成功]"
      fi
    else
      echo "[停止成功]"
    fi
  else
    echo "==================================="
    echo "警告: $APP_MAINCLASS 没有运行"
    echo "==================================="
  fi
}

status() {
  checkpid

  if [ $psid -ne 0 ]; then
    echo "$APP_MAINCLASS 正在运行! (pid=$psid)"
  else
    echo "$APP_MAINCLASS 没有运行"
  fi
}

###################################
#(函数)打印系统环境参数
###################################
info() {
  echo "系统消息:"
  echo "****************************"
  echo $(head -n 1 /etc/issue)
  echo $(uname -a)
  echo
  echo "JAVA_HOME=$JAVA_HOME"
  echo $($JAVA_HOME/bin/java -version)
  echo
  echo "APP_MAINCLASS=$APP_MAINCLASS"
  echo "****************************"
}

case "$1" in
'')
  start
  ;;
'start')
  start
  ;;
'debug')
  debug
  ;;
'stop')
  stop
  ;;
'restart')
  stop
  start
  ;;
'status')
  status
  ;;
'info')
  info
  ;;
*)
  echo "Usage: $0 {start|debug|stop|restart|status|info}"
  exit 1
  ;;
esac
exit 0
