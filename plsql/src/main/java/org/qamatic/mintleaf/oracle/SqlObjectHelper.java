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

import org.qamatic.mintleaf.oracle.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.core.SqlScriptObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SqlObjectHelper {

    protected static Logger logger = LoggerFactory.getLogger(SqlObjectHelper.class);

    @SuppressWarnings("unchecked")
    public static SqlObjectInfo getDbObjectInfo(Class<? extends SqlScriptObject> sqlObjectClass) {
        SqlObjectInfo sqlObjectInfo = null;
        Annotation[] annotations = sqlObjectClass.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof SqlObjectInfo) {
                sqlObjectInfo = (SqlObjectInfo) annotation;
            }
        }
        if (sqlObjectInfo == null) {
            if (SqlScriptObject.class.isAssignableFrom(sqlObjectClass.getSuperclass())) {
                Class<? extends SqlScriptObject> sc = (Class<? extends SqlScriptObject>) sqlObjectClass.getSuperclass();
                sqlObjectInfo = getDbObjectInfo(sc);
            }
        }
        return sqlObjectInfo;
    }

    public static SqlObjectInfo getDbObjectInfo(SqlScriptObject sqlObj) {
        return getDbObjectInfo(sqlObj.getClass());
    }

    public static SqlObjectDependsOn getPLImportAnnotation(SqlScriptObject sqlObject) {
        return getPLImportAnnotation(sqlObject.getClass());
    }

    public static SqlObjectDependsOn getPLImportAnnotation(Class<? extends SqlScriptObject> sqlObjectClass) {
        Annotation[] annotations = sqlObjectClass.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof SqlObjectDependsOn) {
                return (SqlObjectDependsOn) annotation;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends SqlScriptObject> void walkDependency(SqlObjectTreeWalker item, List<String> lookUp) throws IllegalStateException {

        SqlObjectDependsOn importAnnotation = getPLImportAnnotation(item.getClassItem());
        if (importAnnotation == null) {

            Class<T> superClass = ((Class<T>) item.getClassItem().getSuperclass());
            if ((superClass == null) || Modifier.isAbstract(superClass.getModifiers())) {
                return;
            }
            SqlObjectTreeWalker child = new SqlObjectTreeWalker(superClass);
            item.addChild(child);
            child.setImported(false);
            walkDependency(child, lookUp);

            return;
        }
        Class<T>[] items = (Class<T>[]) importAnnotation.Using();
        for (Class<T> item2 : items) {
            if (item.getClassItem() == item2) {
                continue;
            }
            SqlObjectTreeWalker child = new SqlObjectTreeWalker(item2);
            item.addChild(child);

            String pair = child.getClassItem().getSimpleName() + item.getClassItem().getSimpleName();
            String reversePair = item.getClassItem().getSimpleName() + child.getClassItem().getSimpleName();
            if (!lookUp.contains(pair)) {
                lookUp.add(pair);
            } else {

                if (lookUp.contains(reversePair)) {
                    logger.error(String.format("circular dependency detected between child: %s and parent: %s", child.getClassItem().toString(), item.getClassItem()));
                    throw new IllegalStateException(String.format("circular dependency detected between %s and %s", child.getClassItem().toString(),
                            item.getClassItem()));
                }

            }
            /*
             * logger.info(String.format("dependency child: %s  parent: %s",
             * child.getClassItem().getSimpleName(),
             * item.getClassItem().getSimpleName()));
             */
            walkDependency(child, lookUp);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends SqlScriptObject> void walkDependency(List<Class<T>> list1, Class<T> rootClass) {

        List<String> lookUp = new ArrayList<String>();
        SqlObjectTreeWalker rootNode = new SqlObjectTreeWalker(rootClass);
        walkDependency(rootNode, lookUp);
        List<Class<? extends SqlScriptObject>> distinctList = SqlObjectTreeWalker.distinct(rootNode);
        for (Class<? extends SqlScriptObject> dependencyTree : distinctList) {
            list1.add((Class<T>) dependencyTree);
        }

    }

    public static Class<? extends SqlScriptObject>[] getDependencyItems(SqlScriptObject sqlObject) {
        return getDependencyItems(sqlObject, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends SqlScriptObject> Class<T>[] getDependencyItems(SqlScriptObject sqlObject, Class<T> filterbyClassType) {
        List<Class<T>> sqlObjectList = new ArrayList<Class<T>>();

        walkDependency(sqlObjectList, (Class<T>) sqlObject.getClass());

        if (filterbyClassType == null) {
            return sqlObjectList.toArray(new Class[sqlObjectList.size()]);
        }

        List<Class<T>> list = new ArrayList<Class<T>>();
        for (Class<T> sqlClass : sqlObjectList) {
            if (filterbyClassType.isAssignableFrom(sqlClass)) {
                list.add(sqlClass);
            }
        }

        return list.toArray(new Class[list.size()]);
    }

    public static SqlScriptObject createSqlObjectInstance(DbContext context, Class<? extends SqlScriptObject> pkgClass) throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        SqlScriptObject sqlObject = null;
        @SuppressWarnings("rawtypes")
        Constructor ctor = pkgClass.getConstructors()[0];
        // ugly fix but revisit if test fails,
        if (ctor.getParameterTypes().length == 1) {
            sqlObject = (SqlScriptObject) ctor.newInstance(context);
        }
        if (ctor.getParameterTypes().length == 2) {
            sqlObject = (SqlScriptObject) ctor.newInstance(null, context);
        }
        return sqlObject;
    }
}
