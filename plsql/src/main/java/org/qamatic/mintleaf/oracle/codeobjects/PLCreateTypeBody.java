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

import org.qamatic.mintleaf.oracle.CodeObject;
import org.qamatic.mintleaf.oracle.CodeObjectCollection;
import org.qamatic.mintleaf.oracle.CodeObjects;

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
