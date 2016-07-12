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