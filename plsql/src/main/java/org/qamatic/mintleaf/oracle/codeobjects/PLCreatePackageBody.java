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
import org.qamatic.mintleaf.interfaces.CodeObjectCollection;
import org.qamatic.mintleaf.interfaces.CodeObjects;

public class PLCreatePackageBody implements CodeObject {

    private final String packageName;

    private final CodeObjectCollection<CodeObject> mvmemberFields = new CodeObjects<CodeObject>();
    private final CodeObjectCollection<CodeObject> mvmemberMethods = new CodeObjects<CodeObject>();

    public PLCreatePackageBody(String pkgName) {
        packageName = pkgName;
    }

    public void addMemberField(PLMemberField def) {
        mvmemberFields.add(def);
    }

    public void addMemberMethodBody(PLMemberMethodBody method) {
        mvmemberMethods.add(method);
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

    private String getMemberFields() {
        StringBuilder sb = new StringBuilder();

        for (CodeObject codeObject : mvmemberFields) {
            sb.append("\t");
            sb.append(codeObject.toString());
            sb.append(String.format(";%n"));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("create or replace package body %s as %n%s%n%send;%n", packageName, getMemberFields(), getMemberMethods()));
        return sb.toString();
    }
}
