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

import org.qamatic.mintleaf.interfaces.SqlArgumentRecordTypeExtension;
import org.qamatic.mintleaf.interfaces.SqlArgumentTypeMap;

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
