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

package org.qamatic.mintleaf.oracle.argextensions;

public class OracleBooleanTypeExtension extends OracleArgumentTypeExtension {

    @Override
    public String getTypeConversionCode() {
        StringBuilder builder = new StringBuilder();

        builder.append("  FUNCTION bool2int (b BOOLEAN) RETURN INTEGER IS");
        builder.append("  BEGIN IF b IS NULL THEN RETURN 0; ELSIF b THEN RETURN 1; ELSE RETURN 0; END IF; END bool2int;");

        builder.append("  FUNCTION int2bool (i INTEGER) RETURN BOOLEAN IS");
        builder.append("  BEGIN IF i IS NULL THEN RETURN false; ELSE RETURN i <> 0; END IF; END int2bool;");

        return builder.toString();
    }

    @Override
    public String getAssignmentCodeBeforeCall() {
        if (isResultsParameter()) {
            return "";
        }
        return getUnsupportedVariable() + " := " + "int2bool(" + getSupportedVariable() + ");";
    }

    @Override
    public String getAssignmentCodeAfterCall() {
        if (isOutParameter()) {
            return "? := " + "bool2int(" + getUnsupportedVariable() + ");";
        }
        return "";
    }

    @Override
    public String getIdentifierDeclaration() {

        return super.getIdentifierDeclaration();
    }

    @Override
    public String getSupportedType() {
        return "INTEGER";
    }

    @Override
    public void setSupportedType(String supportedType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getUnsupportedType() {
        return "BOOLEAN";
    }

    @Override
    public void setUnsupportedType(String unsupportedType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIdentifier(String identifier) {
        mvidentifier = identifier;
    }
}
