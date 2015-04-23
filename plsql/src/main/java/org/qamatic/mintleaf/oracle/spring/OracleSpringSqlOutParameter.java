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

package org.qamatic.mintleaf.oracle.spring;

import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlArgumentTypeExtension;
import org.qamatic.mintleaf.oracle.argextensions.OracleArgumentTypeExtension;
import org.springframework.jdbc.core.SqlOutParameter;

public class OracleSpringSqlOutParameter extends SqlOutParameter implements SqlArgument {

    private SqlArgumentTypeExtension mvtypeExtension = new OracleArgumentTypeExtension();

    public OracleSpringSqlOutParameter(String name, int sqlType) {
        super(name, sqlType);
    }

    public OracleSpringSqlOutParameter(String name, int sqlType, String typeObject) {
        super(name, sqlType, typeObject);
    }

    @Override
    public boolean isResultsParameter() {
        return false;
    }

    @Override
    public SqlArgumentTypeExtension getTypeExtension() {
        return mvtypeExtension;
    }

    @Override
    public void setTypeExtension(SqlArgumentTypeExtension extension) {
        mvtypeExtension = extension;

    }

    @Override
    public String getParameterName() {
        return this.getName();
    }

}