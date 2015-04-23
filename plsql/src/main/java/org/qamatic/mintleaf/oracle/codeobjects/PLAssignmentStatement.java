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

import org.qamatic.mintleaf.interfaces.AssignmentStatement;
import org.qamatic.mintleaf.interfaces.StatementObject;

public class PLAssignmentStatement extends AssignmentStatement {

    private StatementObject mvstmt;
    private boolean mvinplaceAssign;

    public PLAssignmentStatement(String leftSide, String rightSide) {
        super(leftSide, rightSide);
    }

    public PLAssignmentStatement(String leftSide, StatementObject stmt) {
        super(leftSide, stmt.toString());
        mvstmt = stmt;
    }

    public PLAssignmentStatement(String leftSide, String rightSide, boolean inplaceAssign) {
        super(leftSide, rightSide);
        mvinplaceAssign = inplaceAssign;
    }

    @Override
    public String toString() {
        if (mvstmt == null) {
            if (isInplaceAssign()) {
                return String.format("%s = %s", mvleftSide, mvrightSide);
            } else {
                return String.format("%s := %s;%n", mvleftSide, mvrightSide);
            }
        }
        return String.format(mvleftSide + " := " + mvrightSide);
    }

    public boolean isInplaceAssign() {
        return mvinplaceAssign;
    }

    public void setInplaceAssign(boolean inplaceAssign) {
        mvinplaceAssign = inplaceAssign;
    }

}
