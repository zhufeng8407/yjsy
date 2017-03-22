@echo off
rem Author: ShiBing
rem Date: 2017/2/17
rem Program:
rem 	use mysqldump command to dump schema and data to csv file

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

:showHelp
echo Default username is "root", set "--mysql-username" to change it.
echo Default database is "yjsy", set "--mysql-database" to change it.
echo Default host is "localhost", set "--mysql-host" to change it.
echo Default output file will be "yjsy.sql", set "-f" to change it. 
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

rem check if filename exist
:existFile
set /p yn=the file "%filename%" already exist, type [y/Y] if you want to cover it:
if "%yn%"=="y" goto dump
if "%yn%"=="Y" goto dump
goto end

rem if filename is directory
:inputDirectory
echo the path you input is a directory, please try again.
goto end

rem test if output file already exist or is a directory
:testFilename
echo username:	%username%
echo database:	%database%
echo host:		%host%
echo output file:	%filename%
echo use "-h" parameter to see help document.
if exist %filename%\ goto inputDirectory
if exist %filename% goto existFile

rem dump mysql database to sql file
:dump
echo please enter mysql password:
mysqldump --add-drop-database --add-drop-table -u %username% -p -h %host% %database% > %filename%

:end
