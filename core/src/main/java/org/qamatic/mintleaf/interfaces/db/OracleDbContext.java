package org.qamatic.mintleaf.interfaces.db;

import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlObject;

/**
 * Created by senips on 6/19/16.
 */
public interface OracleDbContext extends DbContext {


    boolean isPackageExists(String pkgName, boolean igoreValidity);

    boolean isPackageBodyExists(String pkgName, boolean igoreValidity);


    boolean isTypeExists(String typeName, boolean igoreValidity);

    boolean isTypeBodyExists(String typeName, boolean igoreValidity);


    boolean isTriggerExists(String triggerName, boolean igoreValidity);

    boolean isSynonymExists(String synonymName, boolean igoreValidity);

    boolean isPackageExists(Class<? extends SqlObject> pkgClass);

    void dropObject(String objectName, String objectType, String clause);

    void dropObject(String objectName, String objectType);

}