#!/bin/sh
# Author: ShiBing
# Date: 2017/2/21
# Program:
#   mysql backup

# default values
bakDir="/home/yjsyTest/mysqlDump"
username="root"
database="yjsy"
host="localhost"
password="root"
DATE=`date +%Y%m%d`

# read parameter from command 
while [ ".$1" != . ]
do 
	case "$1" in
		--bakdir )
			bakDir="$2"
			shift;shift;
			continue
		;;
		--mysql-username )
			username="$2"
			shift;shift;
			continue
		;;
		--mysql-database )
			database="$2"
			shift;shift;
			continue
		;;
		--mysql-password )
			password="$2"
			shift;shift;
			continue
		;;
		--mysql-host )
			host="$2"
			shift;shift;
			continue
		;;
		-h )
			echo "set \"--bakdir\" to change backup directory."
			echo "Default username is \"root\", set \"--mysql-username\" to change it." 
			echo "Default database is \"yjsy\", set \"--mysql-database\" to change it."
			echo "Default host is \"localhost\", set \"--mysql-host\" to change it."
			echo "Default password is \"root\", set \"--mysql-password\" to change it."
			exit 0
		;;
		* )
			break
		;;
	esac
done

# create bakup directory if it doesn't exist
if [ ! -e "$bakDir" ]; then
	mkdir -p "$bakDir"
	if [ $? != 0 ]; then
		echo "ERROR backup fails when run command 'mkdir -p $bakDik'"
		exit 1
	fi
fi

logFile="$bakDir/mysqlbak.log"
if [ ! -e "$logFile" ]; then
	touch $logFile
	if [ $? != 0 ]; then
		echo "ERROR backup fails when run command 'touch $logFile'"
		exit 1
	fi
fi

echo -n $(date +"%y-%m-%d %H:%M:%S") >> $logFile
echo -n " " >> $logFile
echo -n "FULL" >> $logFile
echo -n " " >> $logFile

# output file name
dumpFile=$DATE.sql
GZDumpFile=$DATE.sql.tar.gz
cd $bakDir
mysqldump --quick --flush-logs --delete-master-logs --lock-all-tables $database -u$username -h $host -p$password> $dumpFile

if [ $? != 0 ]; then
	echo "ERROR backup fails when run command 'mysqldump --quick --flush-logs --delete-master-logs --lock-all-tables $database -u$username -h $host> $dumpFile'" >> $logFile
	exit 1
fi

# compress output file

tar czf $GZDumpFile $dumpFile >> /dev/null 2>&1
if [ $? != 0 ]; then
	echo "ERROR backup fails when run command 'tar czvf $GZDumpFile $dumpFile'" >> $logFile
	exit 1
fi

echo "INFO [$GZDumpFile] Full Backup Success!" >> $logFile
rm -f $dumpFile

# delete incremental backup files
rm -f $bakDir/daily/*
