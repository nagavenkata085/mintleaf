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

package org.qamatic.mintleaf.oracle.core;/*
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

public interface SqlStoredProcedure extends SqlValue {
    void execute();



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

    SqlArgument createInParameter(String parameterName, int type);

    SqlArgument createInParameter(String parameterName, int type, String objectType);

    SqlArgument createOutParameter(String parameterName, int type);

    SqlArgument createOutParameter(String parameterName, int type, String objectType);


}
