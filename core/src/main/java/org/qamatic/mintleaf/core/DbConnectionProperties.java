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
