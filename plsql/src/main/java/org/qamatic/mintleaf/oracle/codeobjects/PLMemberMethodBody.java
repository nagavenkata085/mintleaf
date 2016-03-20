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
