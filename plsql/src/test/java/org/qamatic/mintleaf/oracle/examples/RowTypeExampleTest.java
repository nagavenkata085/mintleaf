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

package org.qamatic.mintleaf.oracle.examples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ChangeSetRun;
import org.qamatic.mintleaf.oracle.core.SqlObjectInfo;
import org.qamatic.mintleaf.dbs.oracle.OracleColumn;
import org.qamatic.mintleaf.oracle.OracleDbAssert;
import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.DbMetaData;
import org.qamatic.mintleaf.dbs.oracle.OracleDbContext;
import org.qamatic.mintleaf.oracle.OraclePackage;
import org.qamatic.mintleaf.oracle.OracleTypeObject;
import org.qamatic.mintleaf.oracle.TypeObjectField;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class RowTypeExampleTest extends OracleTestCase {


    private EmployeePackage mvpkg;

    @Before
    public void init() throws SQLException, IOException {
        mvpkg = new EmployeePackage(getSchemaOwnerContext());
        new ChangeSetRun().loadChangeSets(getSchemaOwnerContext(), "/examples/employeepackagesectional.sql", new String[]{"drop employee table",
                "create employee table"});
        mvpkg.create();

    }

    @After
    public void cleanUp() throws SQLException, IOException {
        new ChangeSetRun().loadChangeSets(getSchemaOwnerContext(), "/examples/employeepackagesectional.sql", new String[]{"drop employee table"});
        mvpkg.drop();
    }

    @Test
    public void testObjectExistance() throws SQLException, IOException {
        OracleDbAssert.assertPackageExists((OracleDbContext) mvpkg.getDbContext(), mvpkg.getName());
    }

//    @Test
//    public void testGetEmployee() throws SQLException {
//        Employee emp = mvpkg.getEmployee(10);
//        assertEquals("EMP1", emp.getEmpName());
//        assertEquals(10, emp.getEmpId());
//    }

    @SqlObjectInfo(name = "employee_package", source = "/examples/employeepackage.sql")
    private class EmployeePackage extends OraclePackage {
        public EmployeePackage(DbContext context) {
            super(context);
        }

        public Employee getEmployee(int id) throws SQLException {
            OraclePLProcedure proc = (OraclePLProcedure) getProcedure("GETEMPLOYEE");
            proc.createInParameter("pid", Types.INTEGER);

            proc.createRowTypeOutParameter("result", Employee.class);
            proc = (OraclePLProcedure) proc.recompile();
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
        public DbMetaData getMetaData() throws SQLException {
            return new DbMetaData(new OracleColumn("emp_id"), new OracleColumn("name"));

        }

    }

}
