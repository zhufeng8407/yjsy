#!/bin/bash
# Author: ShiBing
# Date: 2017/2/17
# Program:
#   load database from a sql file

# set default value
username="root"
database="yjsy"
host="localhost"
filename="yjsy.sql"

# read parameter from command 
while [ ".$1" != . ]
do
	case "$1" in
		--mysql-username )
			username="$2"
			shift;shift;
			continue
		;;
		-f )
			filename="$2"
		
			shift;shift;
			continue
		;;
		--mysql-database )
			database="$2"
			shift;shift;
			continue
		;;
		--mysql-host )
			host="$2"
			shift;shift;
			continue
		;;
		-d )
			dropDatebase="y"
			shift;
			continue
		;;
		-h )
			echo "Default username is \"root\", set \"--mysql-username\" to change it." 
			echo "Default database is \"yjsy\", set \"--mysql-database\" to change it."
			echo "Default host is \"localhost\", set \"--mysql-host\" to change it."
			echo "Default input file will be \"yjsy.sql\", set \"-f\" to change it."
			echo "set \"-d\" if you want to drop database before create it everytime."
			exit 0
		;;
		* )
			break
		;;
	esac
done

# check if filename exist
if [ ! -f "$filename" ]; then
	echo "sql file doesn't exit, please try again."
	exit 0		
fi

if [ -d "$filename" ]; then
	echo "the path you input is a directory, please try again."
	exit 0
fi	

echo "username:	$username"
echo "database:	$database"
echo "host:		$host"
echo "input file:	$filename"
echo "use \"-h\" parameter to see help document"

# input mysql password
prompt="enter mysql password:"
while IFS= read -p "$prompt" -r -s -n 1 char
do
    if [[ $char == $'\0' ]]
    then
         break
    fi
    prompt='*'
    password+="$char"
done

# if -d is setted, recreate database, else drop table of database.
if [ "$dropDatebase" == "y" ]; then
	mysql -u$username -p$password -h $host -e "drop database if exists $database; create database $database charset=utf8 COLLATE utf8_general_ci;"
else
	echo -e "\r"
	read -p "the tables of \"$database\" will be droped first, type [y/Y] if you want to do this:" yn;
	if [ ! "$yn" == "y" -a ! "$yn" == "Y" ]; then
		echo "Program will exit."
		exit 0
	fi
	# drop all the tables in the database
	for i in `mysql -u$username -p$password $database -h $host -e "show tables" | grep -v Tables_in`;
	  do echo $i && mysql -u$username -p$password $database -h $host -e "SET FOREIGN_KEY_CHECKS = 0; drop table $i; SET FOREIGN_KEY_CHECKS = 1"; 
	done
fi

# load sql file
mysql -u$username -p$password $database -h $host -e "source $filename"
