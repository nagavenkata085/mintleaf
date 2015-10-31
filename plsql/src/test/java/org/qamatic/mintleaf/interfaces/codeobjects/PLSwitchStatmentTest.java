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
