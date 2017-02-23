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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qamatic.mintleaf.MintLeafException;
import org.qamatic.mintleaf.RowListener;
import org.qamatic.mintleaf.core.ApplyChangeSets;
import org.qamatic.mintleaf.core.FluentJdbc;
import org.qamatic.mintleaf.dbs.h2.H2DbContext;
import org.qamatic.mintleaf.dbs.h2.H2DbContextImpl;
import org.qamatic.mintleaf.tools.CsvExport;
import org.qamatic.mintleaf.tools.CsvImportSource;
import org.qamatic.mintleaf.tools.DbImportSource;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by senips on 3/6/16.
 */
public class ImportExportTests {


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
        ApplyChangeSets.apply(h2DbContext, "res:/example-changesets.sql", "create schema,load seed data");
    }


    @Test
    public void writeCSVTest() throws SQLException, IOException, MintLeafException {
        File f = new File("testfile.csv");
        if (f.exists())
            f.delete();
        h2DbContext.exportDataTo(new CsvExport(new FileWriter(f)), "select * from HRDB.USERS", null);
        assertTrue(new File("testfile.csv").exists());
    }


    @Test
    public void importCSVTest() throws SQLException, IOException, MintLeafException {
        writeCSVTest();//dependent..
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
    public void testInlineParamRegEx() {
        Pattern pattern = Pattern.compile("\\$(\\w+)\\$", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("UPDATE HRDB.USERS SET USERID= $USERID$, USERNAME = '$USERNAME$'");
        matcher.find();
        assertEquals("USERID", matcher.group(1));


    }


    @Test
    public void DbToDbImport() throws SQLException, IOException, MintLeafException {
        ApplyChangeSets.apply(h2DbContext, "res:/example-changesets.sql", "DROP_CREATE_USERS_IMPORT_TABLE");

        FluentJdbc fluentJdbc = h2DbContext.newQuery().withSql("SELECT * FROM HRDB.USERS");

        h2DbContext.importDataFrom(new DbImportSource(fluentJdbc.getResultSet()),
                "INSERT INTO HRDB.USERS_IMPORT_TABLE (USERID, USERNAME) VALUES ($USERID$, '$USERNAME$')");
        assertEquals(7, h2DbContext.getCount("HRDB.USERS_IMPORT_TABLE"));


    }

}
