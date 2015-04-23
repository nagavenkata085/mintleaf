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
