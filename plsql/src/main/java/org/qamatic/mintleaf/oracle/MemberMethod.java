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

package org.qamatic.mintleaf.oracle;

public abstract class MemberMethod implements CodeObject {

    protected String mvreturnType;
    protected CodeObjectCollection<CodeObject> mvparameters = new CodeObjects<CodeObject>();
    protected CodeObjectCollection<CodeObject> mvdeclarations = new CodeObjects<CodeObject>();
    protected CodeObjectCollection<CodeObject> mvstmtexpressions = new CodeObjects<CodeObject>();
    private String mvmethodName;
    private CodeObject mvparentCodeObject;

    public MemberMethod(String methodName) {
        setMethodName(methodName);

    }

    public MemberMethod(String methodName, String returnType) {
        setMethodName(methodName);
        mvreturnType = returnType;

    }

    public CodeObjectCollection<CodeObject> getMethodParameters() {
        return mvparameters;
    }

    public CodeObjectCollection<CodeObject> getMethodDeclarationParameters() {
        return mvdeclarations;
    }

    public void addMemberMethodParameter(MemberMethodParameter param) {
        mvparameters.add(param);
    }

    public void addMemberMethodParameter(CodeObjectCollection<MemberMethodParameter> parameters) {

        for (MemberMethodParameter memberMethodParameter : parameters) {
            mvparameters.add(memberMethodParameter);
        }
    }

    public void addMemberMethodDeclaration(MemberField variable) {
        mvdeclarations.add(variable);
    }

    public void addStatement(StatementObject stmtExpression) {
        mvstmtexpressions.add(stmtExpression);
    }

    protected abstract String getParameters();

    protected abstract String getMemberMethodDeclaration();

    protected abstract String getStatements();

    protected abstract String getReturnParameter();

    public String getMethodName() {
        return mvmethodName;
    }

    public void setMethodName(String methodName) {
        mvmethodName = methodName;
    }

    public CodeObject getParentCodeObject() {
        return mvparentCodeObject;
    }

    public void setParentCodeObject(CodeObject parentCodeObject) {
        mvparentCodeObject = parentCodeObject;
    }

}
