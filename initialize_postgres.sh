#!/bin/bash
# install postgresql, wait for it to start and then create test database

set -x

sudo sh -c 'echo "host all all localhost trust" > /etc/postgresql/9.3/main/pg_hba.conf'
sudo sh -c 'echo "local all all trust" >> /etc/postgresql/9.3/main/pg_hba.conf'
sudo rm /usr/bin/psql
sudo ln -s /usr/lib/postgresql/9.3/bin/psql /usr/bin/psql
sudo service postgresql start
sleep 30s
sudo service postgresql status
psql -c 'create database nativejson;' -U postgres
