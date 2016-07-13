package org.qamatic.mintleaf.dbsupportimpls;

/**
 * Created by senips on 7/12/16.
 */
public class H2DbTests {
  
    @BeforeClass
    public static void cleanDb() {

    }


    @Before
    public void setupSchema() throws IOException, SQLException {

        ExecuteQuery executeQuery = new ExecuteQuery(newDb);
        executeQuery.loadSource("DROP ALL OBJECTS;", ";");
        executeQuery.loadFromSectionalFile(newDb, "/H2Test.sql", "create schema,load seed data");

    }

    @Test
    public void a_CheckSchemaLoaded() throws SQLException, IOException {
        int cnt = newDb.getCount("HRDB.USERS");
        Assert.assertEquals(7, cnt);
    }

    @Test
    public void b_CheckMetaData() throws SQLException, IOException {
        TableMetaData md = newDb.getObjectMetaData("HRDB.USERS");
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
}
