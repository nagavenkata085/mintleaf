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
import org.qamatic.mintleaf.interfaces.MemberMethod;

public class PLMemberMethod extends MemberMethod {

    public PLMemberMethod(String methodName) {
        super(methodName);
    }

    public PLMemberMethod(String methodName, String returnType) {
        super(methodName, returnType);
    }

    @Override
    protected String getParameters() {
        if (mvparameters.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (CodeObject codeObject : mvparameters) {
            if (sb.length() != 1) {
                sb.append(", ");
            }
            sb.append(codeObject.toString());
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getReturnParameter() {
        if (mvreturnType != null) {
            return " return " + mvreturnType;
        }
        return "";
    }

    public String getMethodType() {
        if (mvreturnType != null) {
            return "\tFUNCTION ";
        }
        return "\tPROCEDURE ";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMethodType());

        sb.append(String.format("%s%s%s", getMethodName(), getParameters(), getReturnParameter()));
        return sb.toString();
    }

    @Override
    protected String getMemberMethodDeclaration() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getStatements() {
        throw new UnsupportedOperationException();
    }
}
