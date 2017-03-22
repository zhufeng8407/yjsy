password.c：
	说明：读取屏幕输入，使在 Windows 命令行中输入密码时，不让其显示在屏幕上。password.obj 和 password.exe 为编译后的文件。
	drop-table.bat，dump.bat 和 load.bat 批处理程序中使用了该功能。
	
drop-table.sh：
	说明：Linux shell 脚本，用于删除某个数据库中所有的表。注意设置该脚本的
		权限：`chmod a+x drop-table.sh`。
	参数：
		--mysql-username：设置要访问的数据库的用户名，默认值为 'root'。
		--mysql-database：设置要删除表的数据库的数据库名，默认值为 'yjsy'。
		--mysql-host：设置要访问数据库的 Host 地址，默认值为 'localhost'。
		-h：查看帮助文档
	例：删除用户名为 'root'，Host 为 'localhost'，数据库名为 'yjsy' 的数据库的所有表：
		`./drop-table.sh --mysql-username root --mysql-database yjsy --mysql-host localhost`
		
drop-table.bat：
	drop-table.sh 的 Windows 批处理程序版本，用法与 drop-table.sh 相同。

dump.sh：
	说明：Linux shell 脚本，用于备份指定的数据库。注意设置该脚本的
		权限：`chmod a+x dump.sh`。
	参数:
		--mysql-username：设置要访问的数据库的用户名，默认值为 'root'。
		--mysql-database：设置要删除表的数据库的数据库名，默认值为 'yjsy'。
		--mysql-host：设置要访问数据库的 Host 地址，默认值为 'localhost'。
		-f：指定输出的备份文件，默认值为 'yjsy.sql'。
		-h：查看帮助文档
	例：备份用户名为 'root'，Host 为 'localhost'，数据库名为 'yjsy' 的数据库，备份文件为 '/home/yjsyTest/yjsy.sql'：
		`./dump.sh --mysql-username root --mysql-database yjsy --mysql-host localhost -f /home/yjsyTest/yjsy.sql`
	
dump.bat：
	dump.sh 的 Windows 批处理程序版本，用法与 dump.sh 相同。
	
load.sh：
	说明：Linux shell 脚本，用于加载备份文件到数据库中。注意设置该脚本的
		权限：`chmod a+x load.sh`。
	参数:
		--mysql-username：设置要访问的数据库的用户名，默认值为 'root'。
		--mysql-database：设置要删除表的数据库的数据库名，默认值为 'yjsy'。
		--mysql-host：设置要访问数据库的 Host 地址，默认值为 'localhost'。
		-f：指定备份文件的地址，默认值为 'yjsy.sql'。
		-d：如果指定了 '-d' 参数，将删除并重建指定的数据库，否则会删除指定数据库的所有表。
		-h：查看帮助文档
	例：需要加载备份的数据库的用户名为 'root'，Host 为 'localhost'，数据库名为 'yjsy'，要加载的备份文件为 
	'/home/yjsyTest/yjsy.sql'，指定 '-d' 后将删除并重建 'yjsy' 数据库：
		`./load.sh --mysql-username root --mysql-database yjsy --mysql-host localhost -f /home/yjsyTest/yjsy.sql -d`
		
load.bat：
	load.sh 的 Windows 批处理程序版本，用法与 load.sh 相同。

backup-full.sh：
	说明：Linux shell 脚本，用于实现数据库的全备份，备份文件将被压缩，并以日期命名。每次
		全备份后将删除之前进行的增量备份文件。查看 mysqlbak.log 日志文件检查备份是否成功。
	参数：
		--mysql-username：设置要访问数据库的用户名，默认值为 'root'。
		--mysql-database：设置要删除表的数据库，默认值为 'yjsy'。
		--mysql-host：设置要访问数据库的 Host 地址，默认值为 'localhost'。
		--mysql-password：设置要访问数据库用户的密码，默认值为 'root'。
		--bakdir：指定备份文件的存储目录，默认值为 '/home/yjsyTest/mysqlDump'。
		-h：查看帮助文档
	例：需要备份的数据库的用户名为 'root'，Host 为 'localhost'，数据库名为 'yjsy'，密码为 'root'，备份文件的存储目录为
		'/home/yjsyTest/mysqlDump'：
		`./backup-full.sh --mysql-username root --mysql-database yjsy --mysql-host localhost --mysql-password root  \ 
		--bakdir /home/yjsyTest/mysqlDump`
		
backup-incremental.sh：
	说明：Linux shell 脚本，用于实现数据库的增量备份。增量备份文件存储于指定备份文件目录
		下的 daily 目录中。查看 mysqlbak.log 日志文件检查备份是否成功。
	参数：
		--datadir：指定数据库二进制日志文件的存储目录，默认值为 '/var/lib/mysql'。
		--filename：指定数据库二进制日志索引文件的前缀，与 '/etc/my.cnf' 配置文件中 
			"log_bin" 设置的值一致，默认值为 'mysqld'，这时索引文件的文件名将为 
			'mysqld-bin.index'。
		--mysql-password：设置要访问数据库用户的密码，默认值为 'root'。
		--bakdir：指定备份文件的存储目录，默认值为 '/home/yjsyTest/mysqlDump'。
		-h：查看帮助文档
	例：需要备份的数据库密码为 'root'，这里的密码用于执行 'mysqladmin flush-logs -p' 命令，Mysql 日志文件所在目录为 
		'var/lib/mysql'，索引文件前缀为 'mysqld'，备份文件的存储目录为'/home/yjsyTest/mysqlDump'，增量备份文件存入 
		'/home/yjsyTest/mysqlDump/daily' 目录中：
		`./backup-incremental --mysql-password root --bakdir /home/yjsyTest/mysqlDump --datadir /var/lib/mysql --filename mysqld`