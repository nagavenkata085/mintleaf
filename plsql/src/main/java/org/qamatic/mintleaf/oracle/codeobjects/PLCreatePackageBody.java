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
