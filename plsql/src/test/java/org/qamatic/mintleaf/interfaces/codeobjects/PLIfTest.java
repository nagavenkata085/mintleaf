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
