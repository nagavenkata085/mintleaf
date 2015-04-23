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

package org.qamatic.mintleaf.interfaces;/*
 * Copyright {2015} {Senthil Maruthaiappan}
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


import java.sql.Date;
import java.sql.Timestamp;

public interface SqlProcedure extends SqlValue {
    void execute();

    SqlProcedure recompile();

    boolean isRecompiled();

    void setRecompiled(boolean bValue);

    String getCallString();

    void setParameter(SqlArgument parameterObj);

    @Override
    int getIntValue(String paramterName);

    @Override
    String getStringValue(String paramterName);

    void compile();

    String getSql();

    void setSql(String sql);

    boolean isFunction();

    void setFunction(boolean bValue);

    @Override
    Object getValue(String paramterName);

    @Override
    Object getArray(String paramterName);

    boolean isSqlReadyForUse();

    void setSqlReadyForUse(boolean bValue);

    @Override
    Date getDateValue(String parameterName);

    @Override
    Timestamp getTimestampValue(String parameterName);

    SqlArgumentCollection getDeclaredArguments();

    SqlArgument createParameter(String parameterName, int type);

    SqlArgument createBooleanParameter(String parameterName);

    SqlArgument createRecordParameter(String parameterName, String supportedType, String unsupportedType);

    SqlArgument createParameter(String parameterName, int type, String objectType);

    SqlArgument createOutParameter(String parameterName, int type);

    SqlArgument createBooleanOutParameter(String parameterName);

    SqlArgument createRecordOutParameter(String parameterName, String supportedType, String unsupportedType);

    SqlArgument createOutParameter(String parameterName, int type, String objectType);

    SqlArgument createRowTypeOutParameter(String parameterName, String tableName);

    SqlArgument createRowTypeOutParameter(String parameterName, Class<? extends SqlTypeObject> typeObjectClass);

    SqlArgument createTypeObjectParameter(String parameterName, Class<? extends SqlTypeObject> typeObjectClass);

    SqlArgument createTypeObjectOutParameter(String parameterName, Class<? extends SqlTypeObject> typeObjectClass);

}
