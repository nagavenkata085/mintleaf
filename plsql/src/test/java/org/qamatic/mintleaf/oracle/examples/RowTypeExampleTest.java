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

package org.qamatic.mintleaf.oracle.examples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlObjectMetaData;
import org.qamatic.mintleaf.interfaces.SqlProcedure;
import org.qamatic.mintleaf.interfaces.TypeObjectField;
import org.qamatic.mintleaf.oracle.DbAssert;
import org.qamatic.mintleaf.oracle.OraclePackage;
import org.qamatic.mintleaf.oracle.OracleTypeObject;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class RowTypeExampleTest extends OracleTestCase {


    private EmployeePackage mvpkg;

    @Before
    public void init() throws SQLException, IOException {
        mvpkg = new EmployeePackage(getSchemaOwnerContext());
        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/examples/employeepackagesectional.sql", new String[]{"drop employee table",
                "create employee table"});
        mvpkg.create();

    }

    @After
    public void cleanUp() throws SQLException, IOException {
        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/examples/employeepackagesectional.sql", new String[]{"drop employee table"});
        mvpkg.drop();
    }

    @Test
    public void testObjectExistance() throws SQLException, IOException {
        DbAssert.assertPackageExists(mvpkg);
    }

    @Test
    public void testGetEmployee() throws SQLException {
        Employee emp = mvpkg.getEmployee(10);
        assertEquals("EMP1", emp.getEmpName());
        assertEquals(10, emp.getEmpId());
    }

    @SqlObjectInfo(name = "employee_package", source = "/examples/employeepackage.sql")
    private class EmployeePackage extends OraclePackage {
        public EmployeePackage(DbContext context) {
            super(context);
        }

        public Employee getEmployee(int id) throws SQLException {
            SqlProcedure proc = getProcedure("GETEMPLOYEE");
            proc.createParameter("pid", Types.INTEGER);

            proc.createRowTypeOutParameter("result", Employee.class);
            proc = proc.recompile();
            proc.setValue("pid", Integer.valueOf(id));
            proc.execute();
            return (Employee) proc.getTypeObject("result");
        }

    }

    @SqlObjectInfo(name = "Employee")
    public class Employee extends OracleTypeObject {

        @TypeObjectField(name = "emp_id")
        private int mvempId;

        @TypeObjectField(name = "name")
        private String mvempName;

        public Employee(DbContext context) {
            super(context);
        }

        public int getEmpId() {
            return mvempId;
        }

        public void setEmpId(int empId) {
            mvempId = empId;
        }

        public String getEmpName() {
            return mvempName;
        }

        public void setEmpName(String empName) {
            mvempName = empName;
        }

        @Override
        public SqlObjectMetaData getMetaData() throws SQLException {
            return new SqlObjectMetaData("emp_id", "name");

        }

    }

}
