package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.oracle.core.BaseSqlScriptObject;
import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.oracle.core.SqlScriptObject;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by senips on 7/11/16.
 */
public class OracleBaseSqlScriptObject extends BaseSqlScriptObject {

    public OracleBaseSqlScriptObject(DbContext context) {
        super(context);
    }

    @Override
    public void createDependencies() throws SQLException, IOException {
        createDependencies(SqlObjectHelper.getDependencyItems(this));
    }

    @Override
    public void dropDependencies() throws SQLException, IOException {
        dropDependencies(SqlObjectHelper.getDependencyItems(this));
    }




    private void createDependencies(Class<? extends SqlScriptObject>[] dependencies) {
        for (Class<? extends SqlScriptObject> dependencie : dependencies) {
            try {
                SqlScriptObject sqlObject = createSqlObjectInstance(mvContext, dependencie);
                onDependencyObjectCreated(sqlObject);
                if (sqlObject != null) {

                    if (canCreate(sqlObject)) {
                        sqlObject.create();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void dropDependencies(Class<? extends SqlScriptObject>[] dependencies) {
        for (int i = dependencies.length - 1; i >= 0; i--) {
            try {
                SqlScriptObject sqlObject = createSqlObjectInstance(mvContext, dependencies[i]);
                if (sqlObject != null) {

                    if (canDrop(sqlObject)) {
                        sqlObject.drop();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
