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
