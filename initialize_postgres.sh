#!/bin/bash
#
# Copyright (C) 2016 Marvin Herman Froeder (marvin@marvinformatics.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

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
