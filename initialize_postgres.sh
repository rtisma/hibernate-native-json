#!/bin/bash
# install postgresql, wait for it to start and then create test database

set -x

sudo apt-get update -y
sudo apt-get install -y postgresql-9.3
sudo sh -c 'echo "host all all localhost trust" > /etc/postgresql/9.3/main/pg_hba.conf'
sudo sh -c 'echo "local all all trust" >> /etc/postgresql/9.3/main/pg_hba.conf'
sudo service postgresql start
until runuser -l postgres -c 'pg_isready' 2>/dev/null; do
  >&2 echo "Postgres is unavailable - sleeping for 2 seconds"
  sleep 2s
done    
psql -c 'create database nativejson;' -U postgres
