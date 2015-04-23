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

package org.qamatic.mintleaf.oracle.codeobjects;

import org.qamatic.mintleaf.interfaces.CodeObject;
import org.qamatic.mintleaf.interfaces.CodeObjectCollection;
import org.qamatic.mintleaf.interfaces.CodeObjects;
import org.qamatic.mintleaf.interfaces.CreateType;

public class PLCreateType extends CreateType {

    protected CodeObjectCollection<CodeObject> mvmemberMethods = new CodeObjects<CodeObject>();

    public PLCreateType(String className) {
        super(className);

    }

    public PLCreateType(String className, String subclassName) {
        super(className, subclassName);

    }

    public PLTypeMemberMethod addMemberMethod(PLTypeMemberMethod method) {
        method.setParentCodeObject(this);
        mvmemberMethods.add(method);
        return method;

    }

    public PLTypeMemberMethod getMemberMethodByName(String methodName) {
        for (CodeObject memberMethod : getMemberMethodItems()) {
            PLTypeMemberMethod m = (PLTypeMemberMethod) memberMethod;
            if (m.getMethodName().equalsIgnoreCase(methodName)) {
                return m;
            }
        }
        return null;
    }

    public CodeObjectCollection<CodeObject> getMemberMethodItems() {
        return mvmemberMethods;
    }

    @Override
    protected String getMembers() {
        String defaultValue = "id number";
        if ((getSubClassName() != null) && (getSubClassName().trim().length() != 0)) {
            defaultValue = "";
        }
        StringBuilder sb = new StringBuilder();
        for (CodeObject codeObject : getColumnDefs()) {

            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(String.format("%n\t"));
            sb.append(codeObject.toString());
        }

        if (sb.length() != 0) {
            return sb.toString();
        }

        if ((mvcolumnDefs.size() == 0) && (getMemberMethodItems().size() != 0) && (!isSubClassDefined())) {
            defaultValue = defaultValue + ",";// ugly fix
        }
        return defaultValue;
    }

    @Override
    protected String getMethods() {
        if (mvmemberMethods.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (CodeObject codeObject : mvmemberMethods) {

            if ((sb.length() != 0) || (mvcolumnDefs.size() != 0)) {
                sb.append(String.format(",%n"));
            } else {
                sb.append(String.format("%n"));
            }
            sb.append(codeObject.toString());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        final String COLUMNS = "(membersmethods\n) not final;";
        if ((getSubClassName() == null) || (getSubClassName().trim().length() == 0)) {
            sb.append(String.format("create or replace type %s as OBJECT", getClassName()));

        } else {
            sb.append(String.format("create or replace type %s UNDER %s", getClassName(), getSubClassName()));
        }

        sb.append(COLUMNS.replaceAll("members", getMembers()).replaceAll("methods", getMethods()));

        return sb.toString();
    }
}
