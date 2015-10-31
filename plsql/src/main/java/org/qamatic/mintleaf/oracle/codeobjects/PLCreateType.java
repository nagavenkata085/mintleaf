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
