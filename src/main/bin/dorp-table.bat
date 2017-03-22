@echo off
rem Author: ShiBing
rem Date: 2017/2/18
rem Program:
rem 	drop all tables of a database in Mysql 

rem set default value
set username=root
set database=yjsy
set host=localhost

goto setArgs

:setUsername
shift
set username=%1
shift
goto setArgs 

:setDatabase
shift
set database=%1
shift
goto setArgs

:setHost
shift
set database=%1
shift
goto setArgs

:showHelp
echo Default username is "root", set "--mysql-username" to change it. 
echo Default database is "yjsy", set "--mysql-database" to change it.
echo Default host is "localhost", set "--mysql-host" to change it.
shift
goto end

rem read parameter from command 
:setArgs
if ""%1""=="""" goto doubleCheck
if ""%1""==""--mysql-username"" goto setUsername
if ""%1""==""--mysql-database"" goto setDatabase
if ""%1""==""--mysql-host"" goto setHost
if ""%1""==""-h"" goto showHelp

rem double check for drop operation
:doubleCheck
set /p yn=the tables of "%database%" will be droped, type [y/Y] if you want to do this:
if "%yn%"=="y" goto dropTables
if "%yn%"=="Y" goto dropTables
echo Program will exit.
goto end

rem drop all the tables in the database, password.exe is used to hide password on the screen
:dropTables
password.exe
FOR /F %%p IN (password.txt) DO set password=%%p
del password.txt >nul
FOR /F "skip=1" %%i in ('mysql -u%username% -p%password% %database% -h %host% -e "show tables"') do mysql -u%username% -p%password% %database% -h %host% -e "SET FOREIGN_KEY_CHECKS = 0; drop table %%i; SET FOREIGN_KEY_CHECKS = 1"

:end