package org.qamatic.mintleaf.dbs;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.dbs.h2.H2DbContextImpl;
import org.qamatic.mintleaf.dbs.h2.H2DbContext;
import org.qamatic.mintleaf.DbMetaData;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by senips on 7/12/16.
 */
public class H2DbTests {


    private static H2DbContext h2DbContext;

    @BeforeClass
    public static void cleanDb() {


        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:file:./target/h2test;mv_store=false");
        ds.setDriverClassName("org.h2.Driver");

        h2DbContext = new H2DbContextImpl(ds);



    }


    @Before
    public void setupSchema() throws IOException, SQLException {

        ExecuteQuery executeQuery = new ExecuteQuery(h2DbContext);
        executeQuery.loadSource("DROP ALL OBJECTS;", ";");
        executeQuery.loadFromSectionalFile(h2DbContext, "/h2testdb.sql", "create schema,load seed data");

    }

    @Test
    public void a_CheckSchemaLoaded() throws SQLException, IOException {
        int cnt = h2DbContext.getCount("HRDB.USERS");
        Assert.assertEquals(7, cnt);
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

    @Test
    public void useListnerToDumpData(){

    }

    @Test
    public void c_SingScriptLoad() throws SQLException, IOException {
        int cnt = h2DbContext.getCount("HRDB.USERS");
        Assert.assertEquals(7, cnt);
    }


}
