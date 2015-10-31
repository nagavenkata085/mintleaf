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
