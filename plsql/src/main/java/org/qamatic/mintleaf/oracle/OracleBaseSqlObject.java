package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.oracle.core.BaseSqlObject;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.core.SqlObject;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by senips on 7/11/16.
 */
public class OracleBaseSqlObject extends BaseSqlObject {

    public OracleBaseSqlObject(DbContext context) {
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




    private void createDependencies(Class<? extends SqlObject>[] dependencies) {
        for (Class<? extends SqlObject> dependencie : dependencies) {
            try {
                SqlObject sqlObject = createSqlObjectInstance(mvContext, dependencie);
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

    private void dropDependencies(Class<? extends SqlObject>[] dependencies) {
        for (int i = dependencies.length - 1; i >= 0; i--) {
            try {
                SqlObject sqlObject = createSqlObjectInstance(mvContext, dependencies[i]);
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
