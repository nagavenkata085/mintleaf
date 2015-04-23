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

package org.qamatic.mintleaf.core;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qamatic.mintleaf.interfaces.DbSettings;

import static org.junit.Assert.*;


public class ConnectionParameterTest {

    private static DbSettings mvsettings;

    @BeforeClass
    public static void setup() throws Exception {
        mvsettings = new DbConnectionProperties("test_db.properties");
    }

    @Test
    public void testDefaultDevMode() {

        assertFalse(mvsettings.isDebugEnabled());
    }

    @Test
    public void testDataFileLocation() {
        assertEquals("?/a_data_file0", mvsettings.getDataLocation());
    }

    @Test
    public void testJdbcUrl() {
        assertNotNull(mvsettings.getJdbcUrl());
    }

    @Test
    public void testUsername() {
        assertEquals("sys", mvsettings.getUsername().toLowerCase());
    }

    @Test
    public void testPassword() {
        assertEquals("1234", mvsettings.getPassword());
    }

}
