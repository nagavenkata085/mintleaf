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
import org.qamatic.mintleaf.interfaces.SqlArgumentCollection;
import org.qamatic.mintleaf.interfaces.SqlArgumentTypeExtension;
import org.springframework.jdbc.core.SqlParameter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OracleSpringSqlArgumentCollection implements SqlArgumentCollection {

    private final List<SqlParameter> mvparamList;
    private int mvcurrent;

    public OracleSpringSqlArgumentCollection(List<SqlParameter> list) {
        mvparamList = list;
        mvcurrent = -1;
    }

    private static String getArgumentTypeExtensionSource(SqlArgumentCollection parameters, String typeExtensionSourceMethodName) {
        StringBuilder sb = new StringBuilder();
        for (SqlArgument parameter : parameters) {
            try {
                Method method = parameter.getTypeExtension().getClass().getMethod(typeExtensionSourceMethodName);
                String st = (String) method.invoke(parameter.getTypeExtension());
                if ((st != null) && (st.trim().length() != 0)) {
                    if (!sb.toString().contains(st)) {
                        sb.append(st).append("\n");
                    }
                }
            } catch (Throwable e) {

                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    public Iterator<SqlArgument> iterator() {
        mvcurrent = -1;
        return this;
    }

    @Override
    public boolean hasNext() {
        mvcurrent++;
        if (mvcurrent >= mvparamList.size()) {
            return false;
        }
        return true;
    }

    @Override
    public SqlArgument get(String parameterName) {
        for (int i = 0; i < mvparamList.size(); i++) {
            if (get(i).getParameterName().equals(parameterName)) {
                return get(i);
            }
        }
        return null;
    }

    @Override
    public SqlArgument next() {
        return (SqlArgument) mvparamList.get(mvcurrent);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlArgument get(int idx) {

        return (SqlArgument) mvparamList.get(idx);
    }

    @Override
    public int size() {

        return mvparamList.size();
    }

    @Override
    public List<SqlArgument> rebuildArguments() {
        List<SqlArgument> list = new ArrayList<SqlArgument>();

        List<SqlArgument> inlist1 = new ArrayList<SqlArgument>();
        List<SqlArgument> inlist2 = new ArrayList<SqlArgument>();
        List<SqlArgument> outlist1 = new ArrayList<SqlArgument>();

        for (SqlArgument parameter : this) {
            SqlArgumentTypeExtension ext = parameter.getTypeExtension();
            if (!ext.isOutParameter()) {
                if (!ext.getIdentifier().equals("?")) {
                    inlist1.add(parameter);
                } else {
                    inlist2.add(parameter);
                }
            } else {
                outlist1.add(parameter);
            }

        }
        list.addAll(inlist1);
        list.addAll(inlist2);
        list.addAll(outlist1);
        return list;
    }

    @Override
    public String getIdentifier() {
        return getArgumentTypeExtensionSource(this, "getIdentifier");
    }

    @Override
    public String getIdentifierDeclaration() {
        return getArgumentTypeExtensionSource(this, "getIdentifierDeclaration");
    }

    @Override
    public String getTypeConversionCode() {
        return getArgumentTypeExtensionSource(this, "getTypeConversionCode");
    }

    @Override
    public String getAssignmentCodeBeforeCall() {
        return getArgumentTypeExtensionSource(this, "getAssignmentCodeBeforeCall");
    }

    @Override
    public String getAssignmentCodeAfterCall() {
        return getArgumentTypeExtensionSource(this, "getAssignmentCodeAfterCall");
    }
}
