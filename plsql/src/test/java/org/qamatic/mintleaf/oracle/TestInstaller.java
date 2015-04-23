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

package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.DbModule;
import org.qamatic.mintleaf.oracle.typeobjects.TStringList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.SQLException;


public class TestInstaller implements DbModule {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected DbContext mvSchemaOwnerDbContext;
    protected DbContext mvSysDbContext;


    public TestInstaller(DbContext sysDbaContext, DbContext schemaOwnerDbContext) {
        mvSchemaOwnerDbContext = schemaOwnerDbContext;
        mvSysDbContext = sysDbaContext;
    }

    protected int preInstall() throws SQLException, IOException {

        TestDbProvisioning provision = new TestDbProvisioning(mvSysDbContext);
        provision.create();

        provision.recreateSchemaUser(mvSchemaOwnerDbContext.getDbSettings().getUsername(), mvSchemaOwnerDbContext.getDbSettings().getPassword());
        TStringList s = new TStringList(mvSchemaOwnerDbContext);
        s.createAll();
        if (mvSysDbContext.getDbSettings().isDebugEnabled()) {

            JdbcTemplate template = new JdbcTemplate(mvSysDbContext.getDataSource());
            template.execute(String.format("grant debug connect session to %s", mvSchemaOwnerDbContext.getDbSettings().getUsername()));
            template.execute("alter system set plsql_debug=true");

        }
        return 0;
    }

    @Override
    public int install() throws SQLException, IOException {
        return preInstall();
    }

    @Override
    public int uninstall() {
        return 0;
    }


}
