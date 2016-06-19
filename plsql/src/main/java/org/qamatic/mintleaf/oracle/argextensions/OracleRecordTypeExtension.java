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

import org.qamatic.mintleaf.interfaces.SqlArgumentTypeMap;
import org.qamatic.mintleaf.oracle.SqlArgumentRecordTypeExtension;

import java.util.ArrayList;
import java.util.List;

public class OracleRecordTypeExtension extends OracleArgumentTypeExtension implements SqlArgumentRecordTypeExtension {

    private final List<SqlArgumentTypeMap> mvtypeMaps = new ArrayList<SqlArgumentTypeMap>();

    @Override
    public void addTypeMap(SqlArgumentTypeMap map) {
        mvtypeMaps.add(map);
    }

    public String getMappedTypeToRecAssignment(SqlArgumentTypeMap map, String recPrefix, String typePrefix) {
        return getRecField(map, recPrefix) + " := " + typePrefix + "." + map.getCol2() + ";";
    }

    public String getRecField(SqlArgumentTypeMap map, String recPrefix) {
        return recPrefix + "." + map.getCol1();
    }

    public String getMappedTypeToRecAssignments(String recPrefix, String typePrefix) {
        StringBuilder sb = new StringBuilder();
        for (SqlArgumentTypeMap map : mvtypeMaps) {
            sb.append(getMappedTypeToRecAssignment(map, recPrefix, typePrefix));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getRecFields(String recPrefix) {
        StringBuilder sb = new StringBuilder();
        for (SqlArgumentTypeMap map : mvtypeMaps) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(getRecField(map, recPrefix));
        }
        return sb.toString();
    }

    @Override
    public List<SqlArgumentTypeMap> getTypeMaps() {
        return mvtypeMaps;
    }

    @Override
    public String getTypeConversionCode() {
        StringBuilder builder = new StringBuilder();

        builder.append("  \n");
        builder.append("function pl_to_type_${variable}(rec ${recordtype})\n");
        builder.append("         return ${schematypeobject} is\n");
        builder.append("begin\n");
        builder.append("   return ${schematypeobject}(${recToSchematypeAssignments});\n");
        builder.append("end pl_to_type_${variable};\n");
        builder.append("\n");
        builder.append("function type_to_pl_${variable}(t ${schematypeobject})\n");
        builder.append("         return ${recordtype} is\n");
        builder.append(" rec ${recordtype};\n");
        builder.append("begin\n");
        builder.append(" if t IS NOT NULL\n");
        builder.append(" then\n");
        builder.append("${schematypeToRecAssignments}\n");
        builder.append(" end if;\n");
        builder.append(" return rec;\n");
        builder.append("end type_to_pl_${variable};\n");
        builder.append("\n");

        String code = builder.toString();
        code = code.replace("${variable}", getIdentifier());
        code = code.replace("${recordtype}", getUnsupportedType());
        code = code.replace("${schematypeobject}", getSupportedType());
        code = code.replace("${recToSchematypeAssignments}", getRecFields("rec"));
        code = code.replace("${schematypeToRecAssignments}", getMappedTypeToRecAssignments("rec", "t"));
        return code;
    }

    @Override
    public String getAssignmentCodeBeforeCall() {
        if (isResultsParameter()) {
            return "";
        }
        String v = getUnsupportedVariable() + " := " + "type_to_pl_${variable}(" + getSupportedVariable() + ");";
        v = v.replace("${variable}", getIdentifier());
        return v;
    }

    @Override
    public String getSupportedVariable() {
        return super.getSupportedVariable().replace(".", "_");
    }

    @Override
    public String getUnsupportedVariable() {
        return super.getUnsupportedVariable().replace(".", "_");
    }

    @Override
    public String getAssignmentCodeAfterCall() {
        if (isOutParameter()) {
            String v = "? := " + "pl_to_type_${variable}(" + getUnsupportedVariable() + ");";
            v = v.replace("${variable}", getIdentifier());
            return v;
        }
        return "";
    }

    @Override
    public String getIdentifierDeclaration() {

        return super.getIdentifierDeclaration();
    }

    @Override
    public void setIdentifier(String identifier) {
        mvidentifier = identifier;
    }
}
