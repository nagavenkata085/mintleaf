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
import org.qamatic.mintleaf.*;
import org.qamatic.mintleaf.core.ApplyChangeSets;
import org.qamatic.mintleaf.dbs.h2.H2DbContext;
import org.qamatic.mintleaf.dbs.h2.H2DbContextImpl;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by senips on 7/12/16.
 */
public class ChangeSetTests {


    private static H2DbContext h2DbContext;

    @BeforeClass
    public static void setupDb() {

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:file:./target/H2DbTests;mv_store=false");
        ds.setDriverClassName("org.h2.Driver");

        h2DbContext = new H2DbContextImpl(ds);
    }

    @Before
    public void applyChangeSet() throws IOException, SQLException {
        ApplyChangeSets.apply(h2DbContext, "DROP ALL OBJECTS;", ";");
        ApplyChangeSets.apply(h2DbContext, "res:/example-changesets.sql", "create schema,load seed data");
    }


    @Test
    public void a_checkCSVDump() throws SQLException, IOException, MintLeafException {
        File f = new File("testfile.csv");
        if (f.exists())
            f.delete();
        h2DbContext.exportDataTo(new CsvExport(new FileWriter(f)), "select * from HRDB.USERS", null);
        assertTrue(new File("testfile.csv").exists());
    }


    @Test
    public void a1_readCSV() throws SQLException, IOException, MintLeafException {
        a_checkCSVDump();//dependent..
        File f = new File("testfile.csv");
        if (f.exists()) {

            Reader reader = new FileReader(f);
            h2DbContext.importDataFrom(new CsvImportSource(reader), "UPDATE HRDB.USERS SET USERNAME = '$USERNAME$-changed' WHERE USERID=$USERID$");

            h2DbContext.query("SELECT USERNAME FROM HRDB.USERS", new RowListener<String>() {

                @Override
                public String eachRow(int row, ResultSet resultSet) throws SQLException {
                    assertTrue(resultSet.getString("USERNAME").contains("-changed"));
                    return null;
                }


            });

        } else assertTrue("testfile.csv file not found", false);
    }

    @Test
    public void a_CheckSchemaLoaded() throws SQLException, IOException {
        int cnt = h2DbContext.getCount("HRDB.USERS");
        Assert.assertEquals(7, cnt);
    }

    @Test
    public void testInlineParamRegEx() {
        Pattern pattern = Pattern.compile("\\$(\\w+)\\$", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("UPDATE HRDB.USERS SET USERID= $USERID$, USERNAME = '$USERNAME$'");
        matcher.find();
        assertEquals("USERID", matcher.group(1));


    }

    @Test
    public void b_CheckMetaData() throws SQLException, IOException {
        DbMetaData md = h2DbContext.getMetaData("HRDB.USERS");
        Assert.assertEquals(4, md.size());
        Assert.assertEquals("USERID", md.getColumns().get(0).getColumnName());
        Assert.assertEquals(4, md.getColumns().get(0).getDatatype());
        Assert.assertEquals("USERNAME", md.getColumns().get(1).getColumnName());
        Assert.assertEquals(12, md.getColumns().get(1).getDatatype());
        Assert.assertEquals("RATE", md.getColumns().get(2).getColumnName());
        Assert.assertEquals(3, md.getColumns().get(2).getDatatype());
        Assert.assertEquals("CREATE_TIME", md.getColumns().get(3).getColumnName());
        Assert.assertEquals(91, md.getColumns().get(3).getDatatype());
    }


}
