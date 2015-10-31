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
import org.qamatic.mintleaf.oracle.codeobjects.PLAssignmentStatement;
import org.qamatic.mintleaf.oracle.codeobjects.PLElse;
import org.qamatic.mintleaf.oracle.codeobjects.PLIf;
import org.qamatic.mintleaf.oracle.codeobjects.PLStringBuilder;

import static org.junit.Assert.assertEquals;

public class PLIfTest {

    @Test
    public void tesSimpleIFTest() {
        PLIf ifCondition = new PLIf("monthly_value <= 4000");
        ifCondition.addStatement(new PLAssignmentStatement("ILevel", "'Low Income'"));

        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("if monthly_value <= 4000 then");
        exepctedValue.appendLine("ILevel := 'Low Income';");

        exepctedValue.appendLine("end if;");
        assertEquals(exepctedValue.toString(), ifCondition.toString());
    }

    @Test
    public void tesSimpleIFElseTest() {
        PLIf ifCondition = new PLIf("monthly_value <= 4000");
        ifCondition.addStatement(new PLAssignmentStatement("ILevel", "'Low Income'"));

        ifCondition.getElse().addStatement(new PLAssignmentStatement("ILevel", "'High Income'"));
        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("if monthly_value <= 4000 then");
        exepctedValue.appendLine("ILevel := 'Low Income';");
        exepctedValue.appendLine("else");
        exepctedValue.appendLine("ILevel := 'High Income';");

        exepctedValue.appendLine("end if;");
        assertEquals(exepctedValue.toString(), ifCondition.toString());
    }

    @Test
    public void tesComplexIFElseTest() {
        PLIf ifCondition = new PLIf("monthly_value <= 4000");
        ifCondition.addStatement(new PLAssignmentStatement("ILevel", "'Low Income'"));

        ifCondition.getElse().addStatement(new PLAssignmentStatement("ILevel", "'High Income'"));

        PLElse elseCondition1 = ifCondition.addElseIf("monthly_value > 4000 and monthly_value <= 7000");
        elseCondition1.addStatement(new PLAssignmentStatement("ILevel", "'Avg Income'"));

        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("if monthly_value <= 4000 then");
        exepctedValue.appendLine("ILevel := 'Low Income';");
        exepctedValue.appendLine("elsif monthly_value > 4000 and monthly_value <= 7000 then");
        exepctedValue.appendLine("ILevel := 'Avg Income';");
        exepctedValue.appendLine("else");
        exepctedValue.appendLine("ILevel := 'High Income';");
        exepctedValue.appendLine("end if;");
        assertEquals(exepctedValue.toString(), ifCondition.toString());
    }
}
