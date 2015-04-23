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

import org.qamatic.mintleaf.interfaces.CreateType;

public class PLTypeMemberMethod extends PLMemberMethod {

    private boolean mvstatic;
    private boolean mvconstructor;
    private boolean mvoverride;

    public PLTypeMemberMethod() {
        super("");
        mvconstructor = true;
    }

    public PLTypeMemberMethod(String methodName) {
        super(methodName);
    }

    public PLTypeMemberMethod(String methodName, String returnType) {
        super(methodName, returnType);
    }

    @Override
    public String getMethodName() {

        if (isConstructor()) {
            CreateType t = (CreateType) getParentCodeObject();
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

    public boolean isConstructor() {
        return mvconstructor;
    }

    public void setConstructor(boolean constructor) {
        mvconstructor = constructor;
    }

    public boolean isStatic() {
        return mvstatic;
    }

    public void setStatic(boolean _static) {
        mvstatic = _static;
    }

    public boolean isOverride() {
        return mvoverride;
    }

    public void setOverride(boolean override) {
        mvoverride = override;
    }

}
