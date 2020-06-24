#!/bin/sh

# Copyright (c) Philipp Wagner. All rights reserved.
# Licensed under the MIT license. See LICENSE file in the project root for full license information.

function ask_yes_or_no() {
    read -p "$1 ([y]es or [N]o): "
    case $(echo $REPLY | tr '[A-Z]' '[a-z]') in
        y|yes) echo "yes" ;;
        *)     echo "no" ;;
    esac
}

STDOUT=stdout.log
STDERR=stderr.log
LOGFILE=query_output.log

HostName=localhost
PortNumber=5432
DatabaseName=sampledb
UserName=philipp

if [[ "no" == $(ask_yes_or_no "Use Host ($HostName) Port ($PortNumber)") ]]
then
	read -p "Enter HostName: " HostName
	read -p "Enter Port: " PortNumber
fi

if [[ "no" == $(ask_yes_or_no "Use Database ($DatabaseName)") ]]
then
	read -p "Enter Database: " DatabaseName
fi

if [[ "no" == $(ask_yes_or_no "Use User ($UserName)") ]]
then
	read -p "Enter User: " UserName
fi

read -p "Password: " PGPASSWORD

# Tables
psql -h $HostName -p $PortNumber -d $DatabaseName -U $UserName < 01_Create/create_database.sql -L $LOGFILE 1>>$STDOUT 2>>$STDERR
