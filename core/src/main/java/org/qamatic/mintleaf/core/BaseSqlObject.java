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


import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlObject;
import org.qamatic.mintleaf.interfaces.SqlReader;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class BaseSqlObject implements SqlObject {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private DbContext mvContext;
    private String mvName;
    private String mvSource;
    private SqlReaderListener mvfirstChildListner;
    private SqlReaderListener mvlastChildListner;
    private SqlReaderListener mvlistener;

    public BaseSqlObject(DbContext context) {
        mvContext = context;
    }

    @Override
    public boolean isExists() {

        throw new UnsupportedOperationException();
    }

    @Override
    public DbContext getDbContext() {
        return mvContext;
    }

    @Override
    public String getName() {
        if (mvName != null) {
            return mvName;
        }
        SqlObjectInfo p = SqlObjectHelper.getDbObjectInfo(this);
        if (p == null) {
            return null;
        }
        return p.name();
    }

    @Override
    public void setName(String value) {
        mvName = value;
    }

    @Override
    public String getSource() {
        if (mvSource != null) {
            return mvSource;
        }
        SqlObjectInfo p = SqlObjectHelper.getDbObjectInfo(this);
        if (p == null) {
            return null;
        }
        return p.source();
    }

    @Override
    public void setSource(String source) {
        mvSource = source;
    }

    @Override
    public String getDropSource() {
        SqlObjectInfo p = SqlObjectHelper.getDbObjectInfo(this);
        if (p == null) {
            return null;
        }
        return p.dropSource();
    }

    public final SqlReaderListener getChildListener() {
        return mvfirstChildListner;
    }

    protected String getCreateSourceDelimiter() {
        SqlObjectInfo p = SqlObjectHelper.getDbObjectInfo(this);
        if (p == null) {
            return null;
        }
        return p.sourceDelimiter().equals("") ? null : p.sourceDelimiter();
    }

    protected String getDropSourceDelimiter() {
        SqlObjectInfo p = SqlObjectHelper.getDbObjectInfo(this);
        if (p == null) {
            return null;
        }
        return p.dropSourceDelimiter().equals("") ? null : p.dropSourceDelimiter();
    }

    protected boolean canCreate(SqlObject sqlObject) {
        return true;
    }

    protected boolean canDrop(SqlObject sqlObject) {
        return true;
    }

    protected void onDependencyObjectCreated(SqlObject sqlObject) {

    }

    private void createDependencies(Class<? extends SqlObject>[] dependencies) {
        for (Class<? extends SqlObject> dependencie : dependencies) {
            try {
                SqlObject sqlObject = SqlObjectHelper.createSqlObjectInstance(mvContext, dependencie);
                onDependencyObjectCreated(sqlObject);
                if (sqlObject != null) {

                    if (canCreate(sqlObject)) {
                        sqlObject.create();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void dropDependencies(Class<? extends SqlObject>[] dependencies) {
        for (int i = dependencies.length - 1; i >= 0; i--) {
            try {
                SqlObject sqlObject = SqlObjectHelper.createSqlObjectInstance(mvContext, dependencies[i]);
                if (sqlObject != null) {

                    if (canDrop(sqlObject)) {
                        sqlObject.drop();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createDependencies() throws SQLException, IOException {
        createDependencies(SqlObjectHelper.getDependencyItems(this));
    }

    @Override
    public void dropDependencies() throws SQLException, IOException {
        dropDependencies(SqlObjectHelper.getDependencyItems(this));
    }

    protected void onBeforeCreate() {

    }

    protected void onAfterCreate() {

    }

    @Override
    public void create() throws SQLException, IOException {
        onBeforeCreate();
        if (getSource() == null) {
            return;
        }
        createInternal();
    }

    protected void createInternal() throws IOException, SQLException {
        logger.info(String.format("SqlObject.Create: Name: %s,  source: %s", getName(), getSource()));
        SqlReader reader = getCreateSourceReader();
        if (getCreateSourceDelimiter() != null) {
            reader.setDelimiter(getCreateSourceDelimiter());
        }
        execute(reader);
        onAfterCreate();
    }

    @Override
    public void dropAll() throws SQLException, IOException {
        drop();
        dropDependencies();
    }

    @Override
    public void createAll() throws SQLException, IOException {
        logger.info(String.format("SqlObject.CreateAll: Name: %s,  source: %s", getName(), getSource()));
        createDependencies();
        create();
    }

    public SqlReaderListener getSqlReadListener() {
        if (mvlistener == null) {
            mvlistener = new SqlCodeExecutor(mvContext);
            mvlistener.setChildReaderListener(getChildListener());
        }

        return mvlistener;
    }

    protected SqlReader getCreateSourceReader(InputStream stream) {
        return new SqlFileReader(stream);
    }

    protected SqlReader getCreateSourceReader() {
        InputStream iStream = this.getClass().getResourceAsStream(getSource());
        return getCreateSourceReader(iStream);
    }

    protected SqlReader getDropSourceReader(InputStream stream) {
        return new SqlFileReader(stream);
    }

    protected SqlReader getDropSourceReader() {
        InputStream iStream = this.getClass().getResourceAsStream(getDropSource());
        return getDropSourceReader(iStream);
    }

    @Override
    public void drop() throws SQLException, IOException {
        if (getDropSource() == null) {
            return;
        }
        SqlReader reader = getDropSourceReader();
        if (getDropSourceDelimiter() != null) {
            reader.setDelimiter(getDropSourceDelimiter());
        }
        execute(reader);
    }

    protected String execute(SqlReader reader) throws IOException, SQLException {
        reader.setReaderListener(getSqlReadListener());
        return reader.read();
    }

    @Override
    public String getSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void applyDefaults() throws SQLException, IOException {
        SqlObjectDefaults defaults = SqlObjectHelper.getPLApplyDefaultsAnnotation(this);
        if (defaults != null) {
            createDependencies(defaults.Using());
        }
    }

    @Override
    public void invalidate() {

    }

    @Override
    public void AddReaderListener(SqlReaderListener childListener) {

        if (mvfirstChildListner == null) {
            mvfirstChildListner = childListener;
            mvlastChildListner = mvfirstChildListner;
            return;
        }
        if (mvlastChildListner != null) {
            mvlastChildListner.setChildReaderListener(childListener);
        }
        mvlastChildListner = childListener;

    }
}