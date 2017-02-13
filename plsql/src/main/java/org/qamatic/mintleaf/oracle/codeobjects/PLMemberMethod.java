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
import org.qamatic.mintleaf.oracle.MemberMethod;

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
