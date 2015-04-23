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

package org.qamatic.mintleaf.interfaces;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface SqlValue {

    void setValue(String parameterName, Object value);

    void setBooleanValue(String parameterName, Boolean value);

    int getIntValue(String paramterName);

    String getStringValue(String paramterName);

    Object getValue(String paramterName);

    Object getArray(String paramterName);

    Date getDateValue(String parameterName);

    Timestamp getTimestampValue(String parameterName);

    boolean getBooleanValue(String parameterName);

    Object getStruct(String paramterName);

    SqlTypeObjectValue getTypeObjectValue(String parameterName) throws SQLException;

    SqlTypeObject getTypeObject(String parameterName) throws SQLException;

}
