package org.qamatic.mintleaf;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by senips on 7/16/16.
 */
public interface SqlScript {
    DbContext getDbContext();

    void create() throws SQLException, IOException;
}
