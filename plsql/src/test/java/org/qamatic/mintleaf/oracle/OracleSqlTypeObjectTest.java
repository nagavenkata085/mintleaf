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

package org.qamatic.mintleaf.oracle;


import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.core.SqlExecutor;
import org.qamatic.mintleaf.oracle.core.SqlObjectInfo;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleColumn;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbAssert;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.TableMetaData;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateType;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateTypeBody;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.spring.OracleTypeObjectValue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OracleSqlTypeObjectTest extends OracleTestCase {

    private static List<Object> mvTestObjects = new ArrayList<Object>();


    @Test
    public void testSetGet() throws SQLException {
        OracleTypeObject typeObj = new MockTypeObject(getSchemaOwnerContext());
        SqlTypeObjectValue typeVlaue = new MockOracleTypeObjectValue(null, null);
        typeObj.setTypeObjectValue(typeVlaue);
        Assert.assertEquals(typeVlaue, typeObj.getTypeObjectValue());
    }

    @Test
    public void testNoBindOnNull() throws SQLException {
        MockTypeObject typeObj = new MockTypeObject(getSchemaOwnerContext());
        typeObj.autoBind();
        assertNull(typeObj.getTypeObjectValue());
    }

    @SuppressWarnings("boxing")
    private SqlTypeObjectValue getTestDataScott100(DbContext context) throws SQLException {
        SqlTypeObjectValue typeValue = new MockOracleTypeObjectValue(context, "Employee");
        typeValue.getMetaData().add(new OracleColumn("ID", "number"));
        typeValue.getMetaData().add(new OracleColumn("FIRST_NAME", "varchar(2000)"));
        typeValue.getMetaData().add(new OracleColumn("LAST_NAME", "number"));
        mvTestObjects.clear();
        mvTestObjects.add(100);
        mvTestObjects.add("scott");
        mvTestObjects.add("tiger");
        return typeValue;
    }

    @Test
    public void testBindButExitOnNoColumn() throws SQLException {
        MockTypeObject typeObj = new MockTypeObject(getSchemaOwnerContext()) {
            @Override
            public TableMetaData getMetaData() throws SQLException {

                return null;
            }
        };

        typeObj.setTypeObjectValue(getTestDataScott100(getSchemaOwnerContext()));
        typeObj.autoBind();
        assertNull(typeObj.getEmpNameInvalid());
    }

    @Test
    public void testAutoBindString() throws SQLException {
        MockTypeObject typeObj = new MockTypeObject(getSchemaOwnerContext()) {
            @Override
            public TableMetaData getMetaData() throws SQLException {

                return null;
            }

        };

        typeObj.setTypeObjectValue(getTestDataScott100(getSchemaOwnerContext()));
        typeObj.autoBind();
        assertEquals("scott", typeObj.getEmpName());
    }

    @Test
    public void testAutoBindNumber() throws SQLException {
        MockTypeObject typeObj = new MockTypeObject(getSchemaOwnerContext()) {
            @Override
            public TableMetaData getMetaData() throws SQLException {

                return null;
            }

        };
        typeObj.setTypeObjectValue(getTestDataScott100(getSchemaOwnerContext()));
        typeObj.autoBind();
        assertEquals(100, typeObj.getEmpId());
        assertEquals("scott", typeObj.getEmpName());
        assertEquals("tiger", typeObj.getEmpLastName());
    }

    @Test
    public void testgetCreateTypeBodyInstance() {
        MockTypeObject typeObj = new MockTypeObject(getSchemaOwnerContext());
        PLCreateTypeBody bodyCodeObject = typeObj.getCreateTypeBodyInstance();
        assertNotNull(bodyCodeObject);

    }

    @Test
    public void testgetCreateTypeInstance() throws SQLException {
        MockTypeObject typeObj = new MockTypeObject(getSchemaOwnerContext());
        PLCreateType atypeCodeObject = typeObj.getCreateTypeInstance();
        assertNotNull(atypeCodeObject);
        Assert.assertEquals(0, atypeCodeObject.getMemberMethodItems().size());

    }

    @Test
    public void testisCreateFromSqlSource() {
        assertTrue(new OracleTypeObject(null) {
            @Override
            public String getSource() {
                return "/plsql/x.sql";
            }
        }.isCreateFromSqlSource());

        assertFalse(new OracleTypeObject(null) {
            @Override
            public String getSource() {
                return null;
            }
        }.isCreateFromSqlSource());

        assertFalse(new OracleTypeObject(null) {
            @Override
            public String getSource() {
                return "";
            }
        }.isCreateFromSqlSource());

    }

    @Test
    public void testgetCreateTypeFromSchemaTableName() {
        MockTypeObject typeObj = new MockTypeObject(getSchemaOwnerContext());
        assertNull(typeObj.getCreateFromSchemaTableName());

        MockTypeObject typeObj1 = new MockTypeObject(getSchemaOwnerContext()) {
            @Override
            public String getSource() {

                return "schematable:mytable";
            }
        };
        assertEquals("MYTABLE", typeObj1.getCreateFromSchemaTableName());
    }

    @Test
    public void testJustCreateTypeUsingCodeObjects() throws SQLException, IOException {

        MockTypeObject typeObj1 = new MockTypeObject(getSchemaOwnerContext()) {
            @Override
            public String getSource() {
                return null;
            }

            @Override
            public TableMetaData getMetaData() throws SQLException {

                return null;
            }

            @Override
            public String getName() {

                return "DYNAMIC_TYPE";
            }
        };
        typeObj1.create();
        OracleDbAssert.assertTypeExists(getSchemaOwnerContext(), typeObj1.getName());
        OracleDbAssert.assertTypeBodyExists(getSchemaOwnerContext(), typeObj1.getName());
    }

    @Test
    public void testgetDefaultSectionalFile() {

        MockTypeObject typeObj1 = new MockTypeObject(getSchemaOwnerContext()) {
            @Override
            public String getSectionalFile() {
                return super.getSectionalFile();
            }
        };
        assertNull(typeObj1.getSectionalFile());
    }

    @Test
    public void testgetReadListener() {

        MockTypeObject typeObj1 = new MockTypeObject(getSchemaOwnerContext());
        assertTrue("not instance of OracleSqlCodeExecutor", typeObj1.getSqlReadListener() instanceof SqlExecutor);
        MockTypeObject typeObj2 = new MockTypeObject(getSchemaOwnerContext()) {
            @Override
            public String getSectionalFile() {
                return "/examples/typeobjectexample_usingtable_sec.sql";
            }
        };
        assertTrue("not instance of TypeObjectVisitorSqlCodeExecutor", typeObj2.getSqlReadListener() instanceof TypeObjectVisitorSqlExecutor);
    }

    @Test
    public void testgetMetaDataFromTable() throws SQLException, IOException {

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/examples/typeobjectexample_usingtable_sec.sql", new String[]{"drop person table",
                "create person table"});
        try {

            MockTypeObject typeObj1 = new MockTypeObject(getSchemaOwnerContext()) {

                @Override
                public String getCreateFromSchemaTableName() {
                    return "PERSON";
                }
            };

            TableMetaData metaData = typeObj1.getMetaData();
            assertNotNull(metaData);
            assertEquals(3, metaData.size());
            assertEquals("ID", metaData.getColumns().get(0).getColumnName());
            assertEquals("FIRST_NAME", metaData.getColumns().get(1).getColumnName());
            assertEquals("LAST_NAME", metaData.getColumns().get(2).getColumnName());

        } finally {
            new ExecuteQuery()
                    .loadFromSectionalFile(getSchemaOwnerContext(), "/examples/typeobjectexample_usingtable_sec.sql", new String[]{"drop person table"});
        }

    }

    @Test
    public void testbuildMetaDataFromTypeObjectFieldAnnonation() throws SQLException, IOException {

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/examples/typeobjectexample_usingtable_sec.sql", new String[]{"drop person table",
                "create person table"});
        try {

            MockTypeObject typeObj1 = new MockTypeObject(getSchemaOwnerContext());

            TableMetaData metaData = typeObj1.getMetaData();

            assertNotNull(metaData);
            assertEquals(5, metaData.size());
            assertEquals("SOME_NAME", metaData.getColumns().get(0).getColumnName());
            assertEquals("SOME_NAME2", metaData.getColumns().get(1).getColumnName());
            assertEquals("ID", metaData.getColumns().get(2).getColumnName());
            assertEquals("FIRST_NAME", metaData.getColumns().get(3).getColumnName());
            assertEquals("LAST_NAME", metaData.getColumns().get(4).getColumnName());

        } finally {
            new ExecuteQuery()
                    .loadFromSectionalFile(getSchemaOwnerContext(), "/examples/typeobjectexample_usingtable_sec.sql", new String[]{"drop person table"});
        }

    }

    public class BaseMockTypeObject extends OracleTypeObject {
        @TypeObjectField(name = "SOME_NAME")
        private int mvsomeName;
        @TypeObjectField(name = "SOME_NAME2")
        private int mvsomeName2;


        public BaseMockTypeObject(DbContext context) {
            super(context);
        }
    }

    @SqlObjectInfo(name = "MyObject")
    public class MockTypeObject extends BaseMockTypeObject {

        @TypeObjectField(name = "ID")
        private int mvempId;

        @TypeObjectField(name = "FIRST_NAME")
        private String mvempName;

        @TypeObjectField(name = "LAST_NAME")
        private String mvempLastName;

        private String mvempNameInvalid;

        public MockTypeObject(DbContext context) {
            super(context);

        }

        @Override
        public PLCreateTypeBody getCreateTypeBodyInstance() {
            return super.getCreateTypeBodyInstance();
        }

        @Override
        public PLCreateType getCreateTypeInstance() throws SQLException {
            return super.getCreateTypeInstance();
        }

        @Override
        public String getCreateFromSchemaTableName() {
            return super.getCreateFromSchemaTableName();
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

        public String getEmpNameInvalid() {
            return mvempNameInvalid;
        }

        public void setEmpNameInvalid(String empNameInvalid) {
            mvempNameInvalid = empNameInvalid;
        }

        public String getEmpLastName() {
            return mvempLastName;
        }

        public void setEmpLastName(String empLastName) {
            mvempLastName = empLastName;
        }

    }

    public class MockOracleTypeObjectValue extends OracleTypeObjectValue {

        public MockOracleTypeObjectValue(DbContext context, String typeName) {
            super(context, typeName);

        }

        @Override
        protected List<Object> getObjects() {
            return mvTestObjects;
        }
    }

}
