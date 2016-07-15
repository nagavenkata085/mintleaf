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

package org.qamatic.mintleaf.oracle.argextensions;

import org.qamatic.mintleaf.oracle.core.OracleArg;

public class OracleBooleanType extends OracleArg {

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
    public String getCodeBeforeCall() {
        if (isResultsParameter()) {
            return "";
        }
        return getUnsupportedVariable() + " := " + "int2bool(" + getSupportedVariable() + ");";
    }

    @Override
    public String getCodeAfterCall() {
        if (isOutParameter()) {
            return "? := " + "bool2int(" + getUnsupportedVariable() + ");";
        }
        return "";
    }

    @Override
    public String getVariableDeclaration() {

        return super.getVariableDeclaration();
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
