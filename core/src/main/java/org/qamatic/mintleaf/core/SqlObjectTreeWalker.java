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

import org.qamatic.mintleaf.interfaces.SqlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SqlObjectTreeWalker {

    protected static Logger logger = LoggerFactory.getLogger(SqlObjectTreeWalker.class);
    private final Class<? extends SqlObject> mvClassItem;
    private final List<SqlObjectTreeWalker> mvChildren = new ArrayList<SqlObjectTreeWalker>();
    private int mvNodeId;
    private boolean mvDbObjectImported = true;
    private SqlObjectTreeWalker mvParent;

    public <T extends SqlObject> SqlObjectTreeWalker(Class<T> aClass) {
        mvClassItem = aClass;
        mvNodeId = 0;
    }

    private static void flaten(SqlObjectTreeWalker node, List<SqlObjectTreeWalker> list) {
        // logger.info(node.toString());
        list.add(node);
        for (SqlObjectTreeWalker child : node.getChildren()) {
            flaten(child, list);
        }
    }

    public static List<SqlObjectTreeWalker> flaten(SqlObjectTreeWalker node) {
        List<SqlObjectTreeWalker> list = new ArrayList<SqlObjectTreeWalker>();
        flaten(node, list);
        return list;
    }

    public static int findMaxLevel(List<SqlObjectTreeWalker> list) {
        int maxLevel = 0;
        for (SqlObjectTreeWalker sqlObjectTreeWalker : list) {
            if (sqlObjectTreeWalker.getNodeId() > maxLevel) {
                maxLevel = sqlObjectTreeWalker.getNodeId();
            }
        }
        return maxLevel;
    }

    public static List<Class<? extends SqlObject>> distinct(SqlObjectTreeWalker node) {
        List<Class<? extends SqlObject>> result = new ArrayList<Class<? extends SqlObject>>();

        List<SqlObjectTreeWalker> flatList = flaten(node);
        int maxLvl = findMaxLevel(flatList);
        for (int i = maxLvl; i > 0; i--) {
            for (SqlObjectTreeWalker sqlObjectTreeWalker : flatList) {
                if ((sqlObjectTreeWalker.getNodeId() != i) || (!sqlObjectTreeWalker.isImported())) {
                    continue;
                }

                if (!result.contains(sqlObjectTreeWalker.getClassItem())) {
                    result.add(sqlObjectTreeWalker.getClassItem());
                    logger.info("distinct - " + sqlObjectTreeWalker.toString());
                }
            }
        }

        return result;
    }

    public Class<? extends SqlObject> getClassItem() {
        return mvClassItem;
    }

    public int getNodeId() {
        return mvNodeId;
    }

    public SqlObjectTreeWalker getParent() {
        return mvParent;
    }

    public void setParent(SqlObjectTreeWalker parent) {
        mvParent = parent;
        mvNodeId = parent.mvNodeId + 1;
    }

    public List<SqlObjectTreeWalker> getChildren() {
        return mvChildren;
    }

    public void addChild(SqlObjectTreeWalker dependent) {
        mvChildren.add(dependent);
        dependent.setParent(this);
    }

    @Override
    public String toString() {
        return getClassItem() == null ? "root ; level : 0" : getClassItem().getName() + " ; level: " + getNodeId();
    }

    public boolean isImported() {
        return mvDbObjectImported;
    }

    public void setImported(boolean imported) {
        mvDbObjectImported = imported;
    }

}