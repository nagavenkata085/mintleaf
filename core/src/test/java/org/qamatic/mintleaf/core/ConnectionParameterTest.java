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