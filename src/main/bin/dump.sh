#!/bin/bash
# Author: ShiBing
# Date: 2017/2/16
# Program:
#   use mysqldump command to dump schema and data to csv file

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
		-h )
			echo "Default username is \"root\", set \"--mysql-username\" to change it." 
			echo "Default database is \"yjsy\", set \"--mysql-database\" to change it."
			echo "Default host is \"localhost\", set \"--mysql-host\" to change it."
			echo "Default output file will be \"yjsy.sql\", set \"-f\" to change it. "
			exit 0
		;;
		* )
			break
		;;
	esac
done

echo "username:	$username"
echo "database:	$database"
echo "host:		$host"
echo "output file:	$filename"
echo "use \"-h\" parameter to see help document"

# check if filename exist
if [ -f "$filename" ]; then
	read -p "the file \"$filename\" already exist, type [y/Y] if you want to cover it:" yn;
	if [ "$yn" != "y" -a "$yn" != "Y" ]; then
		echo "program will exit cause you don't agree cover file."
		exit 0
	fi
fi

# check if filename is directory
if [ -d "$filename" ]; then
	echo "the path you input is a directory, please try again."
	exit 0
fi	

# dump mysql database to sql file
echo "please enter mysql password"
mysqldump --add-drop-database --add-drop-table -u $username -p -h $host $database > $filename
