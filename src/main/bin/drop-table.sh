#!/bin/bash
# Author: XuLinhao ShiBing
# Date: 2017/2/16
# Program:
#   drop all tables of a database in Mysql

username="root"
database="yjsy"
host="localhost"

while [ ".$1" != . ]
do
	case "$1" in
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
		--mysql-host )
			host="$2"
			shift;shift;
			continue
		;;
		-h )
			echo "Default username is \"root\", set \"--mysql-username\" to change it." 
			echo "Default database is \"yjsy\", set \"--mysql-database\" to change it."
			echo "Default host is \"localhost\", set \"--mysql-host\" to change it."
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
echo "use \"-h\" parameter to see help document"

# double check for drop operation
read -p "the tables of $database will be droped, type [y/Y] if you want to do this:" yn;
if [ "$yn" != "y" -a "$yn" != "Y" ]; then
	echo "program will exit."
	exit 0
fi

# input password of mysql
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


# drop all the tables in the database
for i in `mysql -u$username -p$password $database -h $host -e "show tables" | grep -v Tables_in`;
  do echo $i && mysql -u$username -p$password $database -h $host -e "SET FOREIGN_KEY_CHECKS = 0; drop table $i; SET FOREIGN_KEY_CHECKS = 1"; 
done