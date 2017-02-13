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

package org.qamatic.mintleaf.dbs;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qamatic.mintleaf.SqlScript;
import org.qamatic.mintleaf.core.ExecuteScriptFile;
import org.qamatic.mintleaf.dbs.h2.H2DbContext;
import org.qamatic.mintleaf.dbs.h2.H2DbContextImpl;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by senips on 7/12/16.
 */
public class SingleLoadScriptTests {


    private static H2DbContext h2DbContext;

    @BeforeClass
    public static void setupDb() {

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:file:./target/H2DbScriptTests;mv_store=false");
        ds.setDriverClassName("org.h2.Driver");

        h2DbContext = new H2DbContextImpl(ds);

    }

    @Test
    public void simpleScriptLoad() throws SQLException, IOException {
        SqlScript script = new ExecuteScriptFile(h2DbContext, "res:/h2singlescript.sql", ";");
        script.create();

        Assert.assertTrue(h2DbContext.isTableExists("HRDB.USERS"));
    }


}
