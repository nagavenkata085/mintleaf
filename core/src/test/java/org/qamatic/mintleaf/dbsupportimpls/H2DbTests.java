package org.qamatic.mintleaf.dbsupportimpls;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qamatic.mintleaf.core.DbConnectionProperties;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.dbsupportimpls.h2.H2DbContextImpl;
import org.qamatic.mintleaf.interfaces.DbSettings;
import org.qamatic.mintleaf.interfaces.H2DbContext;
import org.qamatic.mintleaf.interfaces.TableMetaData;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by senips on 7/12/16.
 */
public class H2DbTests {


    private static H2DbContext h2DbContext;

    @BeforeClass
    public static void cleanDb() {

        DbSettings settings = new DbConnectionProperties();
        settings.setJdbcUrl("jdbc:h2:file:./h2test;mv_store=false");

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(settings.getJdbcUrl());
        ds.setDriverClassName("org.h2.Driver");

        h2DbContext = new H2DbContextImpl(ds);
        h2DbContext.setDbSettings(settings);


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
        TableMetaData md = h2DbContext.getObjectMetaData("HRDB.USERS");
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
