/*
 * <!--
 *   ~
 *   ~ The MIT License (MIT)
 *   ~
 *   ~ Copyright (c) 2010-2017 Senthil Maruthaiappan
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   ~
 *   ~
 *   -->
 */

package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.oracle.core.SqlObjectInfo;
import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.oracle.core.SqlStoredProcedure;

import java.sql.Types;

@SqlObjectInfo(name = "TestDbProvisioning", source = "res:/TestDbProvisioning.sql")
public class TestProvisioningScript extends OraclePackage {

    public TestProvisioningScript(DbContext dbContext) {
        super(dbContext);
    }

    public void dropSchemaUser(String userName) {
        SqlStoredProcedure proc = getProcedure("dropApplicationUser");
        proc.createInParameter("pusername", Types.VARCHAR);
        proc.compile();
        proc.setValue("pusername", userName);
        proc.execute();
    }

    public void createSchemaUser(String userName, String appUserPassword) {
        SqlStoredProcedure proc = getProcedure("createApplicationUser");
        proc.createInParameter("pusername", Types.VARCHAR);
        proc.createInParameter("puserPassword", Types.VARCHAR);
        proc.compile();
        proc.setValue("pusername", userName);
        proc.setValue("puserPassword", appUserPassword);
        proc.execute();
    }

    public void recreateSchemaUser(String userName, String appUserPassword) {
        dropSchemaUser(userName);
        createSchemaUser(userName, appUserPassword);
    }

}