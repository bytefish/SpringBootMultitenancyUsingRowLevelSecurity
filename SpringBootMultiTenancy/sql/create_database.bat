@echo off

:: Copyright (c) Philipp Wagner. All rights reserved.
:: Licensed under the MIT license. See LICENSE file in the project root for full license information.

set PGSQL_EXECUTABLE="G:\PostgreSQL\12\bin\psql.exe"
set STDOUT=stdout.log
set STDERR=stderr.log
set LOGFILE=query_output.log

set HostName=localhost
set PortNumber=5432
set DatabaseName=sampledb
set UserName=philipp
set Password=

call :AskQuestionWithYdefault "Use Host (%HostName%) Port (%PortNumber%) [Y,n]?" reply_
if /i [%reply_%] NEQ [y] (
	set /p HostName="Enter HostName: "
	set /p PortNumber="Enter Port: "
)

call :AskQuestionWithYdefault "Use Database (%DatabaseName%) [Y,n]?" reply_
if /i [%reply_%] NEQ [y]  (
	set /p DatabaseName="Enter Database: "
)

call :AskQuestionWithYdefault "Use User (%UserName%) [Y,n]?" reply_
if /i [%reply_%] NEQ [y]  (
	set /p UserName="Enter User: "
)

set /p PGPASSWORD="Password: "

1>%STDOUT% 2>%STDERR% (


	:: Tables
	%PGSQL_EXECUTABLE% -h %HostName% -p %PortNumber% -d %DatabaseName% -U %UserName% < 01_Create/create_database.sql -L %LOGFILE%
)

goto :end

:: The question as a subroutine
:AskQuestionWithYdefault
	setlocal enableextensions
	:_asktheyquestionagain
	set return_=
	set ask_=
	set /p ask_="%~1"
	if "%ask_%"=="" set return_=y
	if /i "%ask_%"=="Y" set return_=y
	if /i "%ask_%"=="n" set return_=n
	if not defined return_ goto _asktheyquestionagain
	endlocal & set "%2=%return_%" & goto :EOF

:end
pause