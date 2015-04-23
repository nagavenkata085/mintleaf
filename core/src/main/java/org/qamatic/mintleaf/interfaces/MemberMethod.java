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

package org.qamatic.mintleaf.interfaces;

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
