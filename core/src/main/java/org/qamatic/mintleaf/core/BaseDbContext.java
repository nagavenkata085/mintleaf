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

package org.qamatic.mintleaf.core;

import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.DbSettings;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public abstract class BaseDbContext implements DbContext {

    private final DataSource mvDataSource;
    private DbSettings mvDbSettings;

    public BaseDbContext(DataSource datasource) {
        mvDataSource = datasource;
    }

    @Override
    public DataSource getDataSource() {
        return mvDataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {

        return mvDataSource.getConnection();
    }

    public DbSettings getDbSettings() {
        return mvDbSettings;
    }


    public void setDbSettings(DbSettings dbSettings) {
        mvDbSettings = dbSettings;
    }
}
