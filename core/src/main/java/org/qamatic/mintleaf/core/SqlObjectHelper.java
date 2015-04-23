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

import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlObject;
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

    public static SqlObjectDependsOn getPLImportAnnotation(SqlObject sqlObject) {
        return getPLImportAnnotation(sqlObject.getClass());
    }

    public static SqlObjectDependsOn getPLImportAnnotation(Class<? extends SqlObject> sqlObjectClass) {
        Annotation[] annotations = sqlObjectClass.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof SqlObjectDependsOn) {
                return (SqlObjectDependsOn) annotation;
            }
        }
        return null;
    }

    public static SqlObjectDefaults getPLApplyDefaultsAnnotation(SqlObject sqlObject) {
        return getPLApplyDefaultsAnnotation(sqlObject.getClass());
    }

    public static SqlObjectDefaults getPLApplyDefaultsAnnotation(Class<? extends SqlObject> sqlObjectClass) {
        Annotation[] annotations = sqlObjectClass.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof SqlObjectDefaults) {
                return (SqlObjectDefaults) annotation;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends SqlObject> void walkDependency(SqlObjectTreeWalker item, List<String> lookUp) throws IllegalStateException {

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
    public static <T extends SqlObject> void walkDependency(List<Class<T>> list1, Class<T> rootClass) {

        List<String> lookUp = new ArrayList<String>();
        SqlObjectTreeWalker rootNode = new SqlObjectTreeWalker(rootClass);
        walkDependency(rootNode, lookUp);
        List<Class<? extends SqlObject>> distinctList = SqlObjectTreeWalker.distinct(rootNode);
        for (Class<? extends SqlObject> dependencyTree : distinctList) {
            list1.add((Class<T>) dependencyTree);
        }

    }

    public static Class<? extends SqlObject>[] getDependencyItems(SqlObject sqlObject) {
        return getDependencyItems(sqlObject, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends SqlObject> Class<T>[] getDependencyItems(SqlObject sqlObject, Class<T> filterbyClassType) {
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
