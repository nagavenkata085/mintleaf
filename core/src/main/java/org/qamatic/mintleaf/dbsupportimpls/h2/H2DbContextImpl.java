package org.qamatic.mintleaf.dbsupportimpls.h2;

import org.qamatic.mintleaf.core.BaseDbContext;
import org.qamatic.mintleaf.interfaces.H2DbContext;

import javax.sql.DataSource;

/**
 * Created by senips on 7/12/16.
 */
public class H2DbContextImpl extends BaseDbContext implements H2DbContext{
    public H2DbContextImpl(DataSource datasource) {
        super(datasource);
    }
}
