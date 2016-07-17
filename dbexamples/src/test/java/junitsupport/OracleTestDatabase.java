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

package junitsupport;

import org.apache.commons.dbcp.BasicDataSource;

import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbContextImpl;
import org.qamatic.mintleaf.DbContext;


public class OracleTestDatabase {

    private static DbContext mvDbContext;
    private static DbContext mvSysDbContext;

    public static DbContext getSchemaOwnerContext() {
        if (mvDbContext == null) {


            BasicDataSource ds = new BasicDataSource();
            mvDbContext = new OracleDbContextImpl(ds);

            ds.setUrl(System.getenv("TEST_DB_URL"));
            ds.setUsername("TestUser3");
            ds.setPassword("TestUser3Password");
            ds.setAccessToUnderlyingConnectionAllowed(true);


        }
        return mvDbContext;
    }

    public static DbContext getSysDbaContext() {
        if (mvSysDbContext == null) {


            BasicDataSource ds = new BasicDataSource();

            ds.setUrl(System.getenv("TEST_DB_URL"));
            ds.setUsername(System.getenv("TEST_DB_MASTER_USERNAME"));
            ds.setPassword(System.getenv("TEST_DB_MASTER_PASSWORD"));

            ds.setAccessToUnderlyingConnectionAllowed(true);
            //ds.setConnectionProperties("internal_logon=sysdba");
            mvSysDbContext = new OracleDbContextImpl(ds);
        }
        return mvSysDbContext;
    }


}
