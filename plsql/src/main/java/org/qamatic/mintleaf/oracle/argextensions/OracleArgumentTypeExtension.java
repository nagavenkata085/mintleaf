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

import org.qamatic.mintleaf.interfaces.SqlArgumentTypeExtension;

public class OracleArgumentTypeExtension implements SqlArgumentTypeExtension {

    protected String mvidentifier = "?";
    private String mvsupportedType = "";
    private String mvunsupportedType = "";
    private boolean mvoutParameter;
    private boolean mvresultParameter;

    @Override
    public String getIdentifier() {

        return mvidentifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        throw new UnsupportedOperationException("you must subclass in order to set custom identifier");
    }

    @Override
    public String getIdentifierDeclaration() {
        if (getIdentifier().equals("?")) {
            return "";
        }
        if (!isOutParameter()) {
            return getUnsupportedTypeDeclaration() + "\n";
        }
        return getSupportedTypeDeclaration() + "\n" + getUnsupportedTypeDeclaration() + "\n";

    }

    private String getUnsupportedTypeDeclaration() {
        return getUnsupportedVariable() + " " + getUnsupportedType() + ";";
    }

    private String getSupportedTypeDeclaration() {
        return getSupportedVariable() + " " + getSupportedType() + ";";
    }

    @Override
    public String getSupportedVariable() {
        if (getIdentifier().equals("?") || !isOutParameter()) {
            return "?";
        }
        if ((getSupportedType() == null) || getSupportedType().length() == 0) {
            return getIdentifier();
        }
        return getIdentifier() + "_sup";
    }

    @Override
    public String getUnsupportedVariable() {
        if (getIdentifier().equals("?")) {
            return "?";
        }
        if ((getUnsupportedType() == null) || getUnsupportedType().length() == 0) {
            return getIdentifier();
        }
        return getIdentifier() + "_unsup";
    }

    @Override
    public String getTypeConversionCode() {
        return "";
    }

    @Override
    public String getAssignmentCodeBeforeCall() {
        return "";
    }

    @Override
    public String getAssignmentCodeAfterCall() {
        return "";
    }

    @Override
    public String getSupportedType() {
        return mvsupportedType;
    }

    @Override
    public void setSupportedType(String supportedType) {
        mvsupportedType = supportedType;
    }

    @Override
    public String getUnsupportedType() {
        return mvunsupportedType;
    }

    @Override
    public void setUnsupportedType(String unsupportedType) {
        mvunsupportedType = unsupportedType;

    }

    @Override
    public boolean isOutParameter() {

        return mvoutParameter;
    }

    @Override
    public void setOutParameter(boolean bValue) {
        mvoutParameter = bValue;

    }

    @Override
    public boolean isResultsParameter() {
        return mvresultParameter;
    }

    @Override
    public void setResultsParameter(boolean bValue) {
        mvresultParameter = bValue;

    }

}
