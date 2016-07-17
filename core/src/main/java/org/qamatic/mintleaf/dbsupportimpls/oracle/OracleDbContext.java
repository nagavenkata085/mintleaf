package org.qamatic.mintleaf.dbsupportimpls.oracle;

import org.qamatic.mintleaf.DbContext;

/**
 * Created by senips on 6/19/16.
 */
public interface OracleDbContext extends DbContext {


    int getNextSequenceNumber(String sequenceName);

    boolean isSequenceExists(String sequenceName);

    boolean isPackageExists(String pkgName, boolean igoreValidity);

    boolean isPackageBodyExists(String pkgName, boolean igoreValidity);


    boolean isTypeExists(String typeName, boolean igoreValidity);

    boolean isTypeBodyExists(String typeName, boolean igoreValidity);


    boolean isTriggerExists(String triggerName, boolean igoreValidity);

    boolean isSynonymExists(String synonymName, boolean igoreValidity);


    void dropObject(String objectName, String objectType, String clause);

    void dropObject(String objectName, String objectType);

}
