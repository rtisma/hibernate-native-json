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

sudo sh -c 'echo "host all all localhost trust" > /etc/postgresql/9.3/main/pg_hba.conf'
sudo sh -c 'echo "local all all trust" >> /etc/postgresql/9.3/main/pg_hba.conf'
sudo rm /usr/bin/psql
sudo ln -s /usr/lib/postgresql/9.3/bin/psql /usr/bin/psql
sudo service postgresql start
sleep 30s
sudo service postgresql status
psql -c 'create database nativejson;' -U postgres
