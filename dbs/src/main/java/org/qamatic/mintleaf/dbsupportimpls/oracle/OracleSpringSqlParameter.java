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

package org.qamatic.mintleaf.dbsupportimpls.oracle;

import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.CustomArgumentType;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleArgumentType;
import org.springframework.jdbc.core.SqlParameter;

public class OracleSpringSqlParameter extends SqlParameter implements SqlArgument {

    private CustomArgumentType mvtypeExtension = new OracleArgumentType();

    public OracleSpringSqlParameter(String name, int sqlType) {
        super(name, sqlType);
    }

    public OracleSpringSqlParameter(String name, int sqlType, String typeObject) {
        super(name, sqlType, typeObject);
    }

    @Override
    public boolean isResultsParameter() {
        return false;
    }

    @Override
    public CustomArgumentType getTypeExtension() {
        return mvtypeExtension;
    }

    @Override
    public void setTypeExtension(CustomArgumentType extension) {
        mvtypeExtension = extension;

    }

    @Override
    public String getParameterName() {
        return this.getName();
    }

}