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

package org.qamatic.mintleaf.oracle.typeobjects;


import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.BaseSqlArrayTypeObject;

@SqlObjectInfo(name = "TSTRINGLIST", source = "/TStringList.Sql")
public class TStringList extends BaseSqlArrayTypeObject {

    private String[] mvobjects;

    public TStringList(DbContext context) {
        super(context);

    }

    public TStringList(DbContext context, String[] items) {
        super(context);
        setValues(items);

    }

    @Override
    protected Object[] getValues() {

        return mvobjects;
    }

    public void setValues(String[] values) {
        mvobjects = values;
    }

    @Override
    public void drop() {
        // no need to drop, all test db assertions are depending on it.
    }


}
