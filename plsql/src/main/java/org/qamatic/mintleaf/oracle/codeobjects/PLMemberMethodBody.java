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

public class PLMemberMethodBody extends PLMemberMethod {

    public PLMemberMethodBody(String methodName) {
        super(methodName);
    }

    public PLMemberMethodBody(String methodName, String returnType) {
        super(methodName, returnType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (mvreturnType != null) {
            sb.append("\tFUNCTION ");
        } else {
            sb.append("\tPROCEDURE ");
        }

        sb.append(String.format("%s%s%s%n\tas%n\t%s%n\tbegin%n%s%n\tend;", getMethodName(), getParameters(), getReturnParameter(), getMemberMethodDeclaration(),
                getStatements()));
        return sb.toString();
    }

    @Override
    public String getMemberMethodDeclaration() {
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
    public String getStatements() {
        if (mvstmtexpressions.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (CodeObject codeObject : mvstmtexpressions) {
            sb.append("\t\t");
            sb.append(codeObject.toString());

        }

        return sb.toString();
    }
}
