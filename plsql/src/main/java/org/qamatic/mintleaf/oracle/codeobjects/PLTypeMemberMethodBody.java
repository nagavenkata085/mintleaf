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

package org.qamatic.mintleaf.oracle.codeobjects;

import org.qamatic.mintleaf.oracle.CodeObject;

public class PLTypeMemberMethodBody extends PLMemberMethod {

    private boolean mvstatic;
    private boolean mvconstructor;
    private boolean mvoverride;

    public PLTypeMemberMethodBody() {
        super("");
        setConstructor(true);
    }

    public PLTypeMemberMethodBody(String methodName) {
        super(methodName);
    }

    public PLTypeMemberMethodBody(String methodName, String returnType) {
        super(methodName, returnType);
    }

    @Override
    public String getMethodName() {

        if (isConstructor()) {
            PLCreateTypeBody t = (PLCreateTypeBody) getParentCodeObject();
            if (t != null) {
                setMethodName(t.getClassName());
            }

        }
        return super.getMethodName();
    }

    @Override
    public String getMethodType() {
        String accessAttrib = "\tMEMBER";
        if (isOverride()) {
            accessAttrib = "\tOVERRIDING MEMBER";
        }
        if (isStatic()) {
            accessAttrib = "\tSTATIC";
        }

        if (isConstructor()) {
            accessAttrib = "\tCONSTRUCTOR";
            mvreturnType = "SELF AS RESULT";
        }

        if (mvreturnType != null) {
            return accessAttrib + " FUNCTION ";
        }
        return accessAttrib + " PROCEDURE ";
    }

    @Override
    protected String getMemberMethodDeclaration() {
        if (mvdeclarations.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (CodeObject codeObject : mvdeclarations) {
            if (sb.length() == 0) {
                sb.append("\t");
            } else {
                sb.append("\t\t");
            }
            sb.append(codeObject.toString());
            sb.append(String.format(";%n"));
        }

        return sb.toString();
    }

    @Override
    protected String getStatements() {

        StringBuilder sb = new StringBuilder();
        for (CodeObject codeObject : mvstmtexpressions) {
            sb.append("\t\t");
            sb.append(codeObject.toString());

        }

        if (isConstructor()) {
            sb.append(String.format("%n\tRETURN;"));
        }
        return sb.toString();
    }

    public boolean isStatic() {
        return mvstatic;
    }

    public void setStatic(boolean _static) {
        mvstatic = _static;
    }

    public boolean isConstructor() {
        return mvconstructor;
    }

    public void setConstructor(boolean constructor) {
        mvconstructor = constructor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMethodType());

        sb.append(String.format("%s%s%s%n\tas%n\t%s%n\tbegin%n%s%n\tend;", getMethodName(), getParameters(), getReturnParameter(), getMemberMethodDeclaration(),
                getStatements()));
        return sb.toString();
    }

    public boolean isOverride() {
        return mvoverride;
    }

    public void setOverride(boolean override) {
        mvoverride = override;
    }
}
