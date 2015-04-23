/*
 * Copyright {2011-2015} Senthil Maruthaiappan
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.qamatic.mintleaf.oracle.junitsupport;

import org.apache.commons.dbcp.BasicDataSource;
import org.qamatic.mintleaf.core.DbConnectionProperties;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.DbSettings;
import org.qamatic.mintleaf.oracle.OracleDbContext;


public class TestDatabase {

    private static DbContext mvDbContext;
    private static DbContext mvSysDbContext;

    public static DbContext getSchemaOwnerContext() {
        if (mvDbContext == null) {
            DbSettings settings = new DbConnectionProperties("test_schema.properties");
            BasicDataSource ds = new BasicDataSource();
            mvDbContext = new OracleDbContext(ds);
            mvDbContext.setDbSettings(settings);
            ds.setUrl(settings.getJdbcUrl());
            ds.setUsername(settings.getUsername());
            ds.setPassword(settings.getPassword());
            ds.setAccessToUnderlyingConnectionAllowed(true);


        }
        return mvDbContext;
    }

    public static DbContext getSysDbaContext() {
        if (mvSysDbContext == null) {
            DbSettings settings = new DbConnectionProperties("test_sysdba.properties");
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl(settings.getJdbcUrl());
            ds.setUsername(settings.getUsername());
            ds.setPassword(settings.getPassword());
            ds.setAccessToUnderlyingConnectionAllowed(true);
            ds.setConnectionProperties("internal_logon=sysdba");
            mvSysDbContext = new OracleDbContext(ds);
            mvSysDbContext.setDbSettings(settings);
        }
        return mvSysDbContext;
    }


}
