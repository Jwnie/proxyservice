#! /bin/bash
WORK_HOME="/home/elise/app/proxyservice"
APP_NAME="com.meow.proxy.Proxyservice"
APP_VERSION="proxyservice-1.0"
PROGRAM="com.meow.proxy.Proxyservice"
AUTHOR="alex<niejiawen9x@gmail.com>"
ABOUT="proxyservice"
MAIN_CLASS="com.meow.proxy.Proxyservice"
JVM_OPTION="-Xms512m -Xmx1024m -XX:+ForceTimeHighResolution"


WORK_DIR=`pwd`
init()
{
	export java_home=$JAVA_HOME
	export PATH=$java_home/bin:$PATH
	export LANG=zh_CN
	echo "设置环境变量完成..."
}

start()
{
	APP_PIDS=`ps -ef --width 4096|grep $PROGRAM |grep -v grep |awk '{print $2}'`
    if [ -n "$APP_PIDS" ]
    then
    echo "CrawlerApp has been started before!Can not start again."
    return
    fi
    	
	cd $WORK_HOME
	init

	APPPATH=.
	
	JARPATH=$APPPATH/lib
	CONFPATH=$APPPATH/conf
	
	LINE=`find $JARPATH -name "*.jar" -depth`
	
	LIBPATH=$CONFPATH
	
	for LOOP in $LINE
	do
		LIBPATH=$LIBPATH:$LOOP
	done
	
    #main class name
	exec java $JVM_OPTION -cp "$APP_NAME:$LIBPATH" $MAIN_CLASS &
}

stop()
{
	APP_PIDS=`ps -ef --width 4096|grep $PROGRAM |grep -v grep |awk '{print $2}'`
	for LOOP in $APP_PIDS
	do
		#kill -9 $LOOP
		kill  $LOOP
	done
}

showstate()
{
    echo "程序进行信息:"
	ps -ef --width 4096 | grep $APP_NAME | grep -v "grep"
}

showversion()
{
	echo -e "Name:\t\t$APP_NAME "
	echo -e "version:\t$APP_VERSION"
	echo -e "Author:\t\t$AUTHOR\n"
	echo -e "About:\t\t$ABOUT"
}

case "$1" in
    start)
    	start
		echo -e "$APP_NAME Starting...\t[OK]"
		;;
    stop)
    	stop
		echo -e "$APP_NAME Stopping...\t[OK]"
		;;
    restart)
    	stop
	sleep 2
    	start
        echo -e "$APP_NAME Restarting...\t[OK]"
		;;
    version|-v)
    	showversion
		;;
	state)
		showstate
		;;
    *)
    	echo "Usage: $0 {start|stop|restart|version|-v|state}"
    exit 1
esac

cd $WORK_DIR

exit 0






