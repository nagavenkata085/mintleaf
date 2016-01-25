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
            DbSettings settings = new DbConnectionProperties();

            settings.setJdbcUrl(System.getenv("TEST_DB_URL"));
            settings.setUsername("TestUser3");
            settings.setPassword("TestUser3Password");

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
            DbSettings settings = new DbConnectionProperties();

            settings.setJdbcUrl(System.getenv("TEST_DB_URL"));
            settings.setUsername(System.getenv("TEST_DB_MASTER_USERNAME"));
            settings.setPassword(System.getenv("TEST_DB_MASTER_PASSWORD"));

            BasicDataSource ds = new BasicDataSource();
            ds.setUrl(settings.getJdbcUrl());
            ds.setUsername(settings.getUsername());
            ds.setPassword(settings.getPassword());
            ds.setAccessToUnderlyingConnectionAllowed(true);
            //ds.setConnectionProperties("internal_logon=sysdba");
            mvSysDbContext = new OracleDbContext(ds);
            mvSysDbContext.setDbSettings(settings);
        }
        return mvSysDbContext;
    }


}
