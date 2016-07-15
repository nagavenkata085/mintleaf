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

package org.qamatic.mintleaf.oracle.core;

import org.qamatic.mintleaf.interfaces.DbSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbConnectionProperties implements DbSettings {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Properties mvProperties;
    private String mvPropFile;

    public DbConnectionProperties() {

    }

    public DbConnectionProperties(String propertyFile) {
        mvPropFile = propertyFile;
    }

    private Properties getProperties() {
        if (mvProperties == null) {
            mvProperties = new Properties();
            if (mvPropFile != null) {
                loadProperties();
            }
        }
        return mvProperties;
    }

    @Override
    public String getJdbcUrl() {
        return getProperties().getProperty("db.url");
    }

    @Override
    public void setJdbcUrl(String jdbcUrl) {
        getProperties().setProperty("db.url", jdbcUrl);
    }

    @Override
    public String getUsername() {
        return getProperties().getProperty("db.username");
    }

    @Override
    public void setUsername(String userName) {
        getProperties().setProperty("db.username", userName);
    }

    @Override
    public String getPassword() {
        return getProperties().getProperty("db.password");
    }

    @Override
    public void setPassword(String password) {
        getProperties().setProperty("db.password", password);
    }

    @Override
    public boolean isDebugEnabled() {
        return Boolean.parseBoolean(getProperties().getProperty("db.debug"));
    }

    @Override
    public void setDebugEnabled(boolean devMode) {
        getProperties().setProperty("db.debug", devMode + "");
    }

    @Override
    public String getDataLocation() {
        return getProperties().getProperty("db.data.location");
    }

    @Override
    public void setDataLocation(String location) {
        getProperties().setProperty("db.dataLocation", location);
    }

    private void loadProperties() {
        InputStream fis = DbConnectionProperties.class.getClassLoader().getResourceAsStream(mvPropFile);
        try {
            mvProperties.load(fis);
        } catch (IOException e) {
            logger.error("error loading test property file: " + mvPropFile, e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                logger.error("error closing test property file: " + mvPropFile, e);
            }
        }
    }
}
