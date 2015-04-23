/*
 * Copyright {2011-2015} Senthil Maruthaiappan
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.qamatic.mintleaf.oracle;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.DatumWithConnection;
import oracle.sql.TypeDescriptor;
import org.qamatic.mintleaf.core.SqlObjectDependsOn;
import org.qamatic.mintleaf.interfaces.DbContext;

import java.sql.Connection;
import java.sql.SQLException;

@SqlObjectDependsOn(Using = {DbUtility.class})
public abstract class BaseSqlArrayTypeObject extends OracleTypeObject {

    public BaseSqlArrayTypeObject(DbContext context) {
        super(context);

    }

    @Override
    protected TypeDescriptor getDescriptor(Connection conn, String typeName) throws SQLException {

        return new ArrayDescriptor(typeName, conn);
    }

    @Override
    protected DatumWithConnection getDatum(TypeDescriptor descriptor, Connection conn, String typeName) throws SQLException {

        return new ARRAY((ArrayDescriptor) getDescriptor(conn, typeName), conn, getValues());
    }
}
