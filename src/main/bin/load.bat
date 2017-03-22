@echo off
rem Author: ShiBing
rem Date: 2017/2/17
rem Program:
rem 	load database from a sql file. There is a 'password.exe' program to input password of mysql, make sure it exist.

rem set default value
set username=root
set database=yjsy
set host=localhost
set filename=yjsy.sql

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

:setFilename
shift
set filename=%1
shift
goto setArgs

:setDropDatabase
set dropDatebase=y
shift
goto setArgs

:showHelp
echo Default username is "root", set "--mysql-username" to change it.
echo Default database is "yjsy", set "--mysql-database" to change it.
echo Default host is "localhost", set "--mysql-host" to change it.
echo Default input file will be "yjsy.sql", set "-f" to change it.
echo set "-d" if you want to drop database before create it everytime.
shift
goto end

rem read parameter from command 
:setArgs
if ""%1""=="""" goto testFilename
if ""%1""==""--mysql-username"" goto setUsername
if ""%1""==""--mysql-database"" goto setDatabase
if ""%1""==""--mysql-host"" goto setHost
if ""%1""==""-f"" goto setFilename
if ""%1""==""-h"" goto showHelp
if ""%1""==""-d"" goto setDropDatabase

rem if filename not exist
:notExistFile
echo sql file doesn't exit, please try again.
goto end

rem if filename is directory
:inputDirectory
echo the path you input is a directory, please try again.
goto end

rem test if input file exist or is a directory
:testFilename
echo username:	%username%
echo database:	%database%
echo host:		%host%
echo input file:	%filename%
echo use "-h" parameter to see help document.
if not exist %filename% goto notExistFile
if exist %filename%\ goto inputDirectory

rem input password of mysql, password.exe is used to hide password on the screen
password.exe
FOR /F %%p IN (password.txt) DO set password=%%p
del password.txt >nul
goto ifDropDatabase

rem drop database and create a new one
:dropDatabase
mysql -u%username% -p%password% -h %host% -e "drop database if exists %database%; create database %database% charset=utf8 COLLATE utf8_general_ci;"
goto loadFile

rem drop tables of database
:dropTables
FOR /F "skip=1" %%i in ('mysql -u%username% -p%password% %database% -h %host% -e "show tables"') do mysql -u%username% -p%password% %database% -h %host% -e "SET FOREIGN_KEY_CHECKS = 0; drop table %%i; SET FOREIGN_KEY_CHECKS = 1"
goto loadFile

rem double check if drop tables
:ifDropTables
set /p yn=the tables of "%database%" will be droped first, type [y/Y] if you want to do this:
if "%yn%"=="y" goto dropTables
if "%yn%"=="Y" goto dropTables
echo Program will exit.
goto end

rem check if input '-d' parameter
:ifDropDatabase
if "%dropDatebase%"=="y" goto dropDatabase
goto ifDropTables

rem load sql file
:loadFile
mysql -u%username% -p%password% %database% -h %host% -e "source %filename%"

:end

