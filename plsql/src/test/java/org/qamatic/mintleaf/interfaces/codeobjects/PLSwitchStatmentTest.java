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

package org.qamatic.mintleaf.interfaces.codeobjects;


import org.junit.Test;
import org.qamatic.mintleaf.oracle.codeobjects.*;

import static org.junit.Assert.assertEquals;

public class PLSwitchStatmentTest {
    @Test
    public void testBasicCaseStatement() {
        PLSwitchStatment aSwitchStmt = new PLSwitchStatment("deptno");
        aSwitchStmt.when("10").addStatement(new PLIdentifierValue("'accounting dept'"));
        aSwitchStmt.when("20").addStatement(new PLIdentifierValue("'research dept'"));
        aSwitchStmt.when("30").addStatement(new PLIdentifierValue("'sales dept'"));
        aSwitchStmt.getElse().addStatement(new PLIdentifierValue("'marketing dept'"));

        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("case deptno");
        exepctedValue.appendLine("when 10 then");
        exepctedValue.appendLine("'accounting dept'");
        exepctedValue.appendLine("when 20 then");
        exepctedValue.appendLine("'research dept'");
        exepctedValue.appendLine("when 30 then");
        exepctedValue.appendLine("'sales dept'");
        exepctedValue.appendLine("else");
        exepctedValue.appendLine("'marketing dept'");
        exepctedValue.appendLine("end case;");
        assertEquals(exepctedValue.toString(), aSwitchStmt.toString());

    }

    @Test
    public void testMappingCaseStatement() {
        PLSwitchStatment aSwitchStmt = new PLSwitchStatment("src_bo.rsrc_type");
        aSwitchStmt.when("'RT_NonLabor'").addStatement(new PLAssignmentStatement("destn_bo.rsrc_type", "'Equipment'"));
        aSwitchStmt.when("'RT_Mat'").addStatement(new PLAssignmentStatement("destn_bo.rsrc_type", "'Material'"));
        aSwitchStmt.getElse().addStatement(new PLAssignmentStatement("destn_bo.rsrc_type", new PLMethodInvoke("getResrcTypeDefaultValue", "src_bo.rsrc_type")));

        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("case src_bo.rsrc_type");
        exepctedValue.appendLine("when 'RT_NonLabor' then");
        exepctedValue.appendLine("destn_bo.rsrc_type := 'Equipment';");
        exepctedValue.appendLine("when 'RT_Mat' then");
        exepctedValue.appendLine("destn_bo.rsrc_type := 'Material';");
        exepctedValue.appendLine("else");
        exepctedValue.appendLine("destn_bo.rsrc_type := getResrcTypeDefaultValue(src_bo.rsrc_type);");
        exepctedValue.appendLine("end case;");

        assertEquals(exepctedValue.toString(), aSwitchStmt.toString());

    }

    @Test
    public void testAssignableCaseStatement() {

        PLSwitchStatment aSwitchStmt = new PLSwitchStatment();
        aSwitchStmt.when("deptno=10").addStatement(new PLIdentifierValue("'accounting dept'"));
        aSwitchStmt.getElse().addStatement(new PLIdentifierValue("'marketing dept'"));

        PLAssignmentStatement assign = new PLAssignmentStatement("myValue", aSwitchStmt);

        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("myValue := case");
        exepctedValue.appendLine("when deptno=10 then");
        exepctedValue.appendLine("'accounting dept'");
        exepctedValue.appendLine("else");
        exepctedValue.appendLine("'marketing dept'");
        exepctedValue.appendLine("end case;");
        assertEquals(exepctedValue.toString(), assign.toString());

    }

}
