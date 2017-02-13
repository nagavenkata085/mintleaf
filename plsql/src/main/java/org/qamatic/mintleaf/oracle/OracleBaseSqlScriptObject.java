/*
 * <!--
 *   ~
 *   ~ The MIT License (MIT)
 *   ~
 *   ~ Copyright (c) 2010-2017 Senthil Maruthaiappan
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   ~
 *   ~
 *   -->
 */

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
