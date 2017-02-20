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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qamatic.mintleaf.core.ApplyChangeSets;
import org.qamatic.mintleaf.dbs.h2.H2DbContext;
import org.qamatic.mintleaf.dbs.h2.H2DbContextImpl;

import java.io.IOException;
import java.sql.SQLException;

public class ApplyChangeSetsTest {

    private static H2DbContext h2DbContext;

    @BeforeClass
    public static void setupDb() {

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:file:./target/H2DbScriptTests;mv_store=false;");
        ds.setDriverClassName("org.h2.Driver");

        h2DbContext = new H2DbContextImpl(ds);

    }

    @Before
    public void cleanDb() throws IOException, SQLException {
        new ApplyChangeSets().apply(h2DbContext, "res:/Testddl.sql", "clean db, create schema");
    }

    @Test
    public void testLoadFromFile() throws SQLException, IOException {
        new ApplyChangeSets().apply(h2DbContext, "res:/Testddl.sql", "create tables");
        Assert.assertTrue(h2DbContext.isTableExists("mintleaf.TABLE1"));
        Assert.assertTrue(h2DbContext.isTableExists("mintleaf.TABLE2"));

        new ApplyChangeSets().apply(h2DbContext, "res:/Testddl.sql", "drop tables");
        Assert.assertFalse(h2DbContext.isTableExists("mintleaf.TABLE1"));
        Assert.assertFalse(h2DbContext.isTableExists("mintleaf.TABLE2"));

    }

    @Test
    public void testLoadSqlScript() throws SQLException, IOException {
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE mintleaf.TABLE1 ");
        builder.append("    ( ");
        builder.append("     ");
        builder.append("     ID NUMBER (18)  NOT NULL ,      ");
        builder.append("     NAME VARCHAR2 (60 CHAR)  NOT NULL");
        builder.append("    ");
        builder.append("    )  \n");
        builder.append(";\n");

        builder.append(" ");
        builder.append("CREATE TABLE mintleaf.TABLE2 ");
        builder.append("    ( ");
        builder.append("      ID NUMBER (18)  NOT NULL ,      ");
        builder.append("     NAME VARCHAR2 (60 CHAR)  NOT NULL");
        builder.append("    )  ");
        builder.append(";");

        builder.append(" ");

        new ApplyChangeSets().applySource(h2DbContext, builder.toString(), ";");
        Assert.assertTrue(h2DbContext.isTableExists("mintleaf.TABLE1"));
        Assert.assertTrue(h2DbContext.isTableExists("mintleaf.TABLE2"));

        new ApplyChangeSets().apply(h2DbContext, "res:/Testddl.sql", "drop tables");
        Assert.assertFalse(h2DbContext.isTableExists("mintleaf.TABLE1"));
        Assert.assertFalse(h2DbContext.isTableExists("mintleaf.TABLE2"));


    }

    @Test
    public void testChangeSetScenario2() throws SQLException, IOException {
        new ApplyChangeSets().apply(h2DbContext, "res:/Testddl.sql", new String[]{"create tables"});
        Assert.assertTrue(h2DbContext.isTableExists("mintleaf.TABLE1"));
        Assert.assertTrue(h2DbContext.isTableExists("mintleaf.TABLE2"));


        new ApplyChangeSets().apply(h2DbContext, "res:/Testddl.sql", new String[]{"drop tables", "create tables"});
        Assert.assertTrue(h2DbContext.isTableExists("mintleaf.TABLE1"));
        Assert.assertTrue(h2DbContext.isTableExists("mintleaf.TABLE2"));


        new ApplyChangeSets().apply(h2DbContext, "res:/Testddl.sql", new String[]{"drop tables"});
        Assert.assertFalse(h2DbContext.isTableExists("mintleaf.TABLE1"));
        Assert.assertFalse(h2DbContext.isTableExists("mintleaf.TABLE2"));


        new ApplyChangeSets().apply(h2DbContext, "res:/Testddl.sql", new String[]{"drop tables", "create tables",
                "drop tables"});
        Assert.assertFalse(h2DbContext.isTableExists("mintleaf.TABLE1"));
        Assert.assertFalse(h2DbContext.isTableExists("mintleaf.TABLE2"));

    }


}
