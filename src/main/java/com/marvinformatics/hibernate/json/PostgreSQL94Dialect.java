/**
 * Copyright (C) 2016 Marvin Herman Froeder (marvin@marvinformatics.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marvinformatics.hibernate.json;

import java.sql.Types;

/**
 * An SQL dialect for Postgres 9.4 and 9.5
 * 
 * Includes support to JSONB type
 * <a href="http://www.postgresql.org/docs/9.4/static/datatype-json.html">http://www.postgresql.org/docs/9.4/static/datatype-json.html</a>.
 * 
 * For json {@link PostgreSQL93Dialect}
 *
 * @author Marvin H Froeder
 */
public class PostgreSQL94Dialect extends PostgreSQL93Dialect {

    public PostgreSQL94Dialect() {
        super();
        registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }

}
