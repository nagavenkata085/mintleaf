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


import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.core.SqlObjectDependsOn;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.*;
import org.qamatic.mintleaf.oracle.DbAssert;
import org.qamatic.mintleaf.oracle.DbUtility;
import org.qamatic.mintleaf.oracle.OraclePackage;
import org.qamatic.mintleaf.oracle.OracleTypeObject;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class TypeObjectExampleTest extends OracleTestCase {

    private static TypeObjectExample_Package mvtestPackage;

    // @AfterClass
    public static void cleanUp() throws SQLException, IOException {
        if (mvtestPackage != null) {
            new ExecuteQuery().loadFromSectionalFile(mvtestPackage.getDbContext(), "/examples/typeobjectexample_usingtable_sec.sql",
                    new String[]{"drop person table"});
            mvtestPackage.dropAll();

            new DbUtility(mvtestPackage.getDbContext()).dropType("TPERSON");
        }
    }

    private TypeObjectExample_Package getTestPackage() throws SQLException, IOException {
        if (mvtestPackage == null) {
            mvtestPackage = new TypeObjectExample_Package(getSchemaOwnerContext());
            mvtestPackage.create();// create first because type object needs it
            mvtestPackage.createAll();
        }
        return mvtestPackage;
    }

    @Before
    public void init() {

    }

    @Test
    public void testTestObjectsCreated() throws SQLException, IOException {
        getTestPackage();
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "TPerson");
        DbAssert.assertTypeBodyExists(getSchemaOwnerContext(), "TPerson");
        DbAssert.assertPackageExists(new TypeObjectExample_Package(getSchemaOwnerContext()));
    }

    @Test
    public void testSetPerson() throws SQLException, IOException {
        TypeObjectExample_Package pkg = getTestPackage();
        TPerson person = new TPerson(getSchemaOwnerContext());
        person.id = 77;
        person.name = "firstname";
        person.lastName = "lastname";
        pkg.setPerson(person);
        TPerson result = pkg.getPerson();
        assertEquals(77, result.id);
        assertEquals("firstname", result.name);
        assertEquals("lastname", result.lastName);

        person.id = 201;
        person.name = "senthil";
        person.lastName = "maruthaiappan";
        pkg.setPerson(person);
        result = pkg.getPerson();
        assertEquals(201, result.id);
        assertEquals("senthil", result.name);
        assertEquals("maruthaiappan", result.lastName);

    }

    @Test
    public void testObjectMethodCall() throws SQLException, IOException {
        TypeObjectExample_Package pkg = getTestPackage();
        TPerson person = new TPerson(getSchemaOwnerContext());
        person.name = "a+";
        person.lastName = "b+";
        person.setCustomData();
        TPerson result = pkg.getPerson();
        assertEquals(98, result.id);
        assertEquals("a+senthil", result.name);
        assertEquals("b+maruth", result.lastName);

    }

    @Test
    public void testSetPersonUsingObjectValue() throws SQLException, IOException {
        TypeObjectExample_Package pkg = getTestPackage();
        TPerson person = new TPerson(getSchemaOwnerContext());
        person.id = 77;
        person.name = "bob";
        person.lastName = "martin";
        pkg.setPerson(person);
        SqlTypeObjectValue result = pkg.getPersonUsingObjectValue();
        assertEquals(77, result.getIntValue("id"));
        assertEquals("bob", result.getStringValue("first_name"));
        assertEquals("martin", result.getStringValue("last_name"));
    }

    @SqlObjectInfo(name = "typeobjectexample_package", source = "/examples/typeobjectexample_package.sql")
    @SqlObjectDependsOn(Using = {TPerson.class})
    private class TypeObjectExample_Package extends OraclePackage {
        public TypeObjectExample_Package(DbContext context) {
            super(context);
        }

        public TPerson getPerson() throws SQLException {
            SqlProcedure proc = getFunction("getperson", TPerson.class);
            proc.execute();
            return (TPerson) proc.getTypeObject("result");

        }

        public void setPerson(TPerson person) {
            SqlProcedure proc = getProcedure("setperson");
            proc.createTypeObjectParameter("person", TPerson.class);
            proc.setValue("person", person);
            proc.execute();

        }

        public SqlTypeObjectValue getPersonUsingObjectValue() throws SQLException {
            SqlProcedure proc = getFunction("getperson", TPerson.class);
            proc.execute();
            return proc.getTypeObject("result").getTypeObjectValue();

        }

    }

    @SqlObjectInfo(name = "TPerson", source = "/examples/tperson.sql")
    public class TPerson extends OracleTypeObject {

        @TypeObjectField(name = "id")
        public int id;

        @TypeObjectField(name = "first_name")
        public String name;

        @TypeObjectField(name = "last_name")
        public String lastName;

        public TPerson(DbContext context) {
            super(context);
        }

        public void setCustomData() {
            SqlProcedure proc = getMemberProcedure("setCustomData");
            proc.execute();
        }

        @SuppressWarnings("boxing")
        @Override
        protected Object[] getValues() {
            return new Object[]{id, name, lastName};
        }

        @Override
        public SqlObjectMetaData getMetaData() throws SQLException {
            return new SqlObjectMetaData("id", "first_name", "last_name");

        }
    }

}
