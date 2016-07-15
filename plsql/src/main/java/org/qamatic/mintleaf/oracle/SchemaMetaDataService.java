/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2010-2015 Senthil Maruthaiappan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.core.DbMetaDataService;
import org.qamatic.mintleaf.interfaces.TableMetaData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SchemaMetaDataService implements DbMetaDataService {

    private final Map<String, TableMetaData> mvstructuredObjectsMetaData = new HashMap<String, TableMetaData>();

    @Override
    public TableMetaData getMetaData(String schemaDotObjectName) {
        schemaDotObjectName = schemaDotObjectName.toUpperCase();
        if (mvstructuredObjectsMetaData.containsKey(schemaDotObjectName)) {
            return mvstructuredObjectsMetaData.get(schemaDotObjectName);
        }
        return null;
    }

    @Override
    public TableMetaData addMetaData(String schemaDotObjectName, TableMetaData metaData) {
        schemaDotObjectName = schemaDotObjectName.toUpperCase();
        TableMetaData mData = getMetaData(schemaDotObjectName);

        if (mData == null) {
            mData = metaData;
            mvstructuredObjectsMetaData.put(schemaDotObjectName, metaData);
        }

        return mData;
    }

    @Override
    public TableMetaData addMetaDataFromTable(DbContext context, String tableNameName) throws SQLException {

        TableMetaData metaData = context.getObjectMetaData(tableNameName);
        addMetaData(context.getDbSettings().getUsername() + "." + tableNameName, metaData);
        return metaData;
    }

}
