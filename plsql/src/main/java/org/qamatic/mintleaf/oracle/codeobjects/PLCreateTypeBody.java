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

public class PLCreateTypeBody implements CodeObject {

    private final String mvclassName;

    private final CodeObjectCollection<CodeObject> mvmemberMethods = new CodeObjects<CodeObject>();

    public PLCreateTypeBody(String className) {
        mvclassName = className;
    }

    public String getClassName() {
        return mvclassName;
    }

    public PLTypeMemberMethodBody addMemberMethodBody(PLTypeMemberMethodBody method) {
        method.setParentCodeObject(this);
        mvmemberMethods.add(method);
        return method;
    }

    public PLTypeMemberMethodBody getMemberMethodByName(String methodName) {
        for (CodeObject memberMethod : getMemberMethodItems()) {
            PLTypeMemberMethodBody m = (PLTypeMemberMethodBody) memberMethod;
            if (m.getMethodName().equalsIgnoreCase(methodName)) {
                return m;
            }
        }
        return null;
    }

    public CodeObjectCollection<CodeObject> getMemberMethodItems() {
        return mvmemberMethods;
    }

    private String getMemberMethods() {
        if (mvmemberMethods.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (CodeObject codeObject : mvmemberMethods) {

            sb.append(codeObject.toString());
            sb.append(String.format("%n%n"));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("create or replace type body %s as %n%send;%n", mvclassName, getMemberMethods()));
        return sb.toString();
    }
}
