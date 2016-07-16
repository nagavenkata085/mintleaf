/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2010-2015 Senthil Maruthaiappan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.core.DbModule;
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

        TestDbProvisioningScript provision = new TestDbProvisioningScript(mvSysDbContext);
        provision.create();

        provision.recreateSchemaUser(mvSchemaOwnerDbContext.getDbSettings().getUsername(), mvSchemaOwnerDbContext.getDbSettings().getPassword());

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
