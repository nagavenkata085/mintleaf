/*
 * <!--
 *   ~
 *   ~ The MIT License (MIT)
 *   ~
 *   ~ Copyright (c) 2010-2017 Senthil Maruthaiappan
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   ~
 *   ~
 *   -->
 */

package org.qamatic.mintleaf.oracle.core;

public class OracleArg implements CustomArg {

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
    public String getVariableDeclaration() {
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
    public String getCodeBeforeCall() {
        return "";
    }

    @Override
    public String getCodeAfterCall() {
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
