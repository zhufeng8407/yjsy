#!/bin/sh
# Author: ShiBing
# Date: 2017/2/21
# Program:
#   mysql incremental backup 

# default values
dataDir=/var/lib/mysql
baseDir=/home/yjsyTest/mysqlDump
name=mysqld
password="root"

# read parameter from command 
while [ ".$1" != . ]
do
	case "$1" in
		--datadir )
			dataDir="$2"
			shift;shift;
			continue
		;;
		--bakdir )
			baseDir="$2"
			shift;shift;
			continue
		;;
		--mysql-password )
			password="$2"
			shift;shift;
			continue
		;;
		--filename )
			name="$2"
			shift;shift;
			continue
		;;
		-h )
			echo "set \"--bakdir\" to change backup directory."
			echo "set \"--datadirfile\" to change location of mysql log files."
			echo "Default password is \"root\", set \"--mysql-password\" to change it."
			echo "use \"--filename\" parameter to set log index file's name. This file name base on the setting of mysql, \
			if you have set \"log_bin=mysqld\" in \"/etc/my.cnf\" file, you need to set \"--filename mysqld\" and the index file would be \"mysqld-bin.index\". \
			The default set is \"mysqld\". So if your mysql setting is \"log_bin=mysqld\" or \"log_bin\", you don't need to set the parameter";
			exit 0
		;;
		* )
			break
		;;
	esac
done
bakDir=${baseDir}/daily

# create bakup directory if it doesn't exist
if [ ! -e "$bakDir" ]; then
	mkdir -p "$bakDir"
	if [ $? != 0 ]; then
		echo "ERROR backup fails when run command 'mkdir -p $bakDik'"
		exit 1
	fi
fi

logFile=${baseDir}/mysqlbak.log
if [ ! -e "$logFile" ]; then
	touch $logFile
	if [ $? != 0 ]; then
		echo "ERROR backup fails when run command 'touch $logFile'"
		exit 1
	fi
fi

echo -n $(date +"%y-%m-%d %H:%M:%S") >> $logFile
echo -n " " >> $logFile
echo -n "INCREMENTAL" >> $logFile
echo -n " " >> $logFile

if [ ! -e "$dataDir" ];then
	echo "ERROR mysql data directory doesn't exist" >> $logFile
	exit 1
fi

# flush logs
mysqladmin flush-logs -p$password

if [ $? != 0 ]; then
	echo "ERROR backup fails when run command 'mysqladmin flush-logs -p'" >> $logFile
	exit 1
fi

if [ ! -e "$dataDir/${name}-bin.index" ]; then
	echo "ERROR \"$dataDir/${name}-bin.index\" file doesn't exist" >> $logFile
	exit 1
fi

# count log files number
fileList=`cat $dataDir/${name}-bin.index`
counter=0
for file in $fileList
do
	counter=`expr $counter + 1 `
done

# skip lastest log file
nextNum=0
for file in $fileList
do
	base=`basename $dataDir/$file`
	nextNum=`expr $nextNum + 1`
	if [ $nextNum -ne $counter ]; then
		dest=$bakDir/$base
		if(test ! -e $dest); then
			cp $dataDir/$base $bakDir
			if [ $? != 0 ]; then
				echo "ERROR backup fails when run command 'cp $dataDir/$base $bakDir'" >> $logFile
				exit 1
			fi
		fi
	fi
done
echo "INFO Incremental Backup Success!" >> $logFile