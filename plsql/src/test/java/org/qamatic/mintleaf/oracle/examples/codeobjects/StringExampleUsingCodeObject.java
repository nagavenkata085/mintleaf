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
