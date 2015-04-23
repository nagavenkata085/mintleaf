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

public abstract class CreateType implements CodeObject {

    protected final CodeObjectCollection<MemberField> mvcolumnDefs = new CodeObjects<MemberField>();
    private String mvclassName;
    private String mvsubClassName;

    public CreateType(String className) {
        setClassName(className);
    }

    public CreateType(String className, String subclassName) {
        setClassName(className);
        setSubClassName(subclassName);
    }

    public void addColumnDef(MemberField def) {
        mvcolumnDefs.add(def);
    }

    public CodeObjectCollection<MemberField> getColumnDefs() {
        return mvcolumnDefs;
    }

    public void removeColumnDef(String colName) {
        for (int i = 0; i < getColumnDefs().size(); i++) {
            MemberField f = getColumnDefs().get(i);
            if (f.getLeftSide().equalsIgnoreCase(colName)) {
                getColumnDefs().remove(i);
                return;
            }
        }
    }

    protected abstract String getMembers();

    protected abstract String getMethods();

    public String getClassName() {
        return mvclassName;
    }

    public void setClassName(String className) {
        mvclassName = className;
    }

    public boolean isSubClassDefined() {
        return (getSubClassName() != null) && (getSubClassName().length() != 0);
    }

    public String getSubClassName() {
        return mvsubClassName;
    }

    public void setSubClassName(String subClassName) {
        mvsubClassName = subClassName;
    }

}
