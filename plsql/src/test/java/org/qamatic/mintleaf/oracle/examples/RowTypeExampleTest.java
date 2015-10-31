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