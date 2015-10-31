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

package org.qamatic.mintleaf.oracle.examples.codeobjects;

import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.core.SqlStringReader;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlProcedure;
import org.qamatic.mintleaf.interfaces.SqlReader;
import org.qamatic.mintleaf.oracle.OraclePackage;
import org.qamatic.mintleaf.oracle.codeobjects.*;

import java.sql.Types;

@SqlObjectInfo(name = "StringExampleUsingCodeObject")
public class StringExampleUsingCodeObject extends OraclePackage {

    public StringExampleUsingCodeObject(DbContext context) {
        super(context);

    }

    @Override
    protected SqlReader getCreateSourceReader() {
        String src = new StringPackageCodeProvider(this.getName()).toString();
        return new SqlStringReader(src);
    }

    public String ReplaceSpaceWithDollarSign(String inputValue) {
        SqlProcedure proc = getFunction("replace_Space_With_DollarSign", Types.VARCHAR);

        proc.createParameter("pstr", Types.VARCHAR);
        proc.compile();
        proc.setValue("pstr", inputValue);
        proc.execute();

        return proc.getStringValue("result");
    }

    private class StringPackageCodeProvider {

        private final String mvpkgName;

        public StringPackageCodeProvider(String pkgName) {
            mvpkgName = pkgName;
        }

        @Override
        public String toString() {

            PLCreatePackage packageCodeProvider = new PLCreatePackage(mvpkgName) {
                {
                    PLMemberMethod m = new PLMemberMethod("replace_Space_With_DollarSign", "varchar2");
                    addMemberMethod(m);
                    m.addMemberMethodParameter(new PLMemberMethodParameter("pstr", PLMemberParameterDirection.IN, "varchar2"));
                }
            };

            PLCreatePackageBody packageBodyProvider = new PLCreatePackageBody(mvpkgName) {
                {
                    PLMemberMethodBody m = new PLMemberMethodBody("replace_Space_With_DollarSign", "varchar2");
                    addMemberMethodBody(m);
                    m.addMemberMethodParameter(new PLMemberMethodParameter("pstr", PLMemberParameterDirection.IN, "varchar2"));
                    m.addMemberMethodDeclaration(new PLMemberField("vstr", "varchar2(4000)"));

                    m.addStatement(new PLAssignmentStatement("vstr", "translate(pstr,' ','$')"));
                    m.addStatement(new PLReturnStatement("trim(vstr)"));
                }
            };

            return packageCodeProvider.toString() + "\n / \n" + packageBodyProvider.toString() + "\n / \n";
        }
    }

}
