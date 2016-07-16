package org.qamatic.mintleaf.interfaces;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by senips on 7/16/16.
 */
public interface DbSqlScript {
    DbContext getDbContext();

    String getSource();

    void setSource(String source);

    void create() throws SQLException, IOException;
}
