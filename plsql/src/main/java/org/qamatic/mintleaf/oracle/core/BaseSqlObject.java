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


import org.qamatic.mintleaf.core.SqlExecutor;
import org.qamatic.mintleaf.core.SqlFileReader;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlReader;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class BaseSqlObject implements SqlObject {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected DbContext mvContext;
    protected String mvName;
    protected String mvSource;
    protected SqlReaderListener mvfirstChildListner;
    protected SqlReaderListener mvlastChildListner;
    protected SqlReaderListener mvlistener;

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
        SqlObjectInfo p = getDbObjectInfo(this);
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
        SqlObjectInfo p = getDbObjectInfo(this);
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
        SqlObjectInfo p = getDbObjectInfo(this);
        if (p == null) {
            return null;
        }
        return p.dropSource();
    }

    public final SqlReaderListener getChildListener() {
        return mvfirstChildListner;
    }

    protected String getCreateSourceDelimiter() {
        SqlObjectInfo p = getDbObjectInfo(this);
        if (p == null) {
            return null;
        }
        return p.sourceDelimiter().equals("") ? null : p.sourceDelimiter();
    }

    protected String getDropSourceDelimiter() {
        SqlObjectInfo p = getDbObjectInfo(this);
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


    @Override
    public void createDependencies() throws SQLException, IOException {

    }

    @Override
    public void dropDependencies() throws SQLException, IOException {

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
            mvlistener = new SqlExecutor(mvContext);
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


    @SuppressWarnings("unchecked")
    public static SqlObjectInfo getDbObjectInfo(Class<? extends SqlObject> sqlObjectClass) {
        SqlObjectInfo sqlObjectInfo = null;
        Annotation[] annotations = sqlObjectClass.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof SqlObjectInfo) {
                sqlObjectInfo = (SqlObjectInfo) annotation;
            }
        }
        if (sqlObjectInfo == null) {
            if (SqlObject.class.isAssignableFrom(sqlObjectClass.getSuperclass())) {
                Class<? extends SqlObject> sc = (Class<? extends SqlObject>) sqlObjectClass.getSuperclass();
                sqlObjectInfo = getDbObjectInfo(sc);
            }
        }
        return sqlObjectInfo;
    }

    public static SqlObjectInfo getDbObjectInfo(SqlObject sqlObj) {
        return getDbObjectInfo(sqlObj.getClass());
    }

    public static SqlObject createSqlObjectInstance(DbContext context, Class<? extends SqlObject> pkgClass) throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        SqlObject sqlObject = null;
        @SuppressWarnings("rawtypes")
        Constructor ctor = pkgClass.getConstructors()[0];
        // ugly fix but revisit if test fails,
        if (ctor.getParameterTypes().length == 1) {
            sqlObject = (SqlObject) ctor.newInstance(context);
        }
        if (ctor.getParameterTypes().length == 2) {
            sqlObject = (SqlObject) ctor.newInstance(null, context);
        }
        return sqlObject;
    }
}
