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

package org.qamatic.mintleaf;

import org.junit.Test;
import org.qamatic.mintleaf.oracle.core.BaseSqlObject;
import org.qamatic.mintleaf.oracle.SqlObjectTreeWalker;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.core.SqlObject;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SqlObjectTreeWalkerTest {

    @Test
    public void testRootNodeId() {
        SqlObjectTreeWalker tree = new SqlObjectTreeWalker(c5.class);
        assertEquals(tree.getClassItem(), tree.getClassItem());
        assertEquals(0, tree.getNodeId());
    }

    @Test
    public void testParentNode() {
        SqlObjectTreeWalker tree = new SqlObjectTreeWalker(c3.class);
        SqlObjectTreeWalker child1 = new SqlObjectTreeWalker(c1.class);
        tree.addChild(child1);
        assertEquals(tree, child1.getParent());

        SqlObjectTreeWalker child2 = new SqlObjectTreeWalker(c3.class);
        tree.addChild(child2);
        assertEquals(tree, child2.getParent());

        SqlObjectTreeWalker child3 = new SqlObjectTreeWalker(c3.class);
        child2.addChild(child3);
        assertEquals(child2, child3.getParent());
    }

    @Test
    public void testAddChild() {
        SqlObjectTreeWalker tree = new SqlObjectTreeWalker(c5.class);
        SqlObjectTreeWalker child1 = new SqlObjectTreeWalker(c4.class);
        tree.addChild(child1);
        assertEquals(1, tree.getChildren().size());
        assertEquals(1, child1.getNodeId());

        SqlObjectTreeWalker child2 = new SqlObjectTreeWalker(c3.class);
        tree.addChild(child2);
        assertEquals(2, tree.getChildren().size());
        assertEquals(1, child2.getNodeId());

        SqlObjectTreeWalker child3 = new SqlObjectTreeWalker(c3.class);
        child2.addChild(child3);
        assertEquals(2, tree.getChildren().size());
        assertEquals(1, child2.getChildren().size());
        assertEquals(2, child3.getNodeId());
    }

    @Test
    public void testFlaten() {
        SqlObjectTreeWalker root = new SqlObjectTreeWalker(null);
        SqlObjectTreeWalker child1 = new SqlObjectTreeWalker(c1.class);
        root.addChild(child1);

        SqlObjectTreeWalker child11 = new SqlObjectTreeWalker(c11.class);
        child1.addChild(child11);

        SqlObjectTreeWalker child12 = new SqlObjectTreeWalker(c12.class);
        child1.addChild(child12);

        SqlObjectTreeWalker child2 = new SqlObjectTreeWalker(c2.class);
        root.addChild(child2);

        SqlObjectTreeWalker child21 = new SqlObjectTreeWalker(c21.class);
        child2.addChild(child21);

        SqlObjectTreeWalker child211 = new SqlObjectTreeWalker(c211.class);
        child21.addChild(child211);

        SqlObjectTreeWalker child3 = new SqlObjectTreeWalker(c3.class);
        child2.addChild(child3);

        List<SqlObjectTreeWalker> d = SqlObjectTreeWalker.flaten(root);

        assertEquals(null, d.get(0).getClassItem());
        assertEquals(c1.class, d.get(1).getClassItem());
        assertEquals(c11.class, d.get(2).getClassItem());
        assertEquals(c12.class, d.get(3).getClassItem());
        assertEquals(c2.class, d.get(4).getClassItem());
        assertEquals(c21.class, d.get(5).getClassItem());
        assertEquals(c211.class, d.get(6).getClassItem());
        assertEquals(c3.class, d.get(7).getClassItem());

        assertEquals(0, d.get(0).getNodeId());
        assertEquals(1, d.get(1).getNodeId());
        assertEquals(2, d.get(2).getNodeId());
        assertEquals(2, d.get(3).getNodeId());
        assertEquals(1, d.get(4).getNodeId());
        assertEquals(2, d.get(5).getNodeId());
        assertEquals(3, d.get(6).getNodeId());
        assertEquals(2, d.get(7).getNodeId());

        assertEquals(null, d.get(0).getParent());
        assertEquals(null, d.get(1).getParent().getClassItem());
        assertEquals(c1.class, d.get(2).getParent().getClassItem());
        assertEquals(c1.class, d.get(3).getParent().getClassItem());
        assertEquals(null, d.get(4).getParent().getClassItem());
        assertEquals(c2.class, d.get(5).getParent().getClassItem());
        assertEquals(c21.class, d.get(6).getParent().getClassItem());
        assertEquals(c2.class, d.get(7).getParent().getClassItem());

        assertEquals(8, d.size());

    }

    @Test
    public void testFindMaxLevel() {
        SqlObjectTreeWalker root = new SqlObjectTreeWalker(null);
        SqlObjectTreeWalker child1 = new SqlObjectTreeWalker(c1.class);
        root.addChild(child1);
        SqlObjectTreeWalker child11 = new SqlObjectTreeWalker(c11.class);
        child1.addChild(child11);
        SqlObjectTreeWalker child12 = new SqlObjectTreeWalker(c12.class);
        child1.addChild(child12);
        SqlObjectTreeWalker child2 = new SqlObjectTreeWalker(c2.class);
        root.addChild(child2);
        SqlObjectTreeWalker child21 = new SqlObjectTreeWalker(c21.class);
        child2.addChild(child21);
        child21.addChild(new SqlObjectTreeWalker(c211.class));
        child2.addChild(new SqlObjectTreeWalker(c3.class));

        List<SqlObjectTreeWalker> d = SqlObjectTreeWalker.flaten(root);

        assertEquals(3, SqlObjectTreeWalker.findMaxLevel(d));

    }

    @Test
    public void testDistinct() {
        SqlObjectTreeWalker root = new SqlObjectTreeWalker(a.class);
        SqlObjectTreeWalker bclass = new SqlObjectTreeWalker(b.class);
        root.addChild(bclass);
        root.addChild(bclass);
        root.addChild(bclass);
        SqlObjectTreeWalker cclass = new SqlObjectTreeWalker(c.class);
        bclass.addChild(cclass);
        bclass = new SqlObjectTreeWalker(b.class);
        cclass.addChild(bclass);
        SqlObjectTreeWalker dclass = new SqlObjectTreeWalker(d.class);
        bclass.addChild(dclass);

        List<Class<? extends SqlObject>> d = SqlObjectTreeWalker.distinct(root);

        assertEquals(d.class, d.get(0));
        assertEquals(b.class, d.get(1));
        assertEquals(c.class, d.get(2));

        assertEquals(3, d.size());

    }

    private class a extends BaseSqlObject {

        public a(DbContext context) {
            super(context);

        }

    }

    private class b extends BaseSqlObject {

        public b(DbContext context) {
            super(context);

        }

    }

    private class c extends BaseSqlObject {

        public c(DbContext context) {
            super(context);

        }

    }

    private class d extends BaseSqlObject {

        public d(DbContext context) {
            super(context);

        }

    }

    private class c1 extends BaseSqlObject {

        public c1(DbContext context) {
            super(context);

        }

    }

    private class c11 extends BaseSqlObject {

        public c11(DbContext context) {
            super(context);

        }

    }

    private class c12 extends BaseSqlObject {

        public c12(DbContext context) {
            super(context);

        }

    }

    private class c2 extends BaseSqlObject {
        public c2(DbContext context) {
            super(context);

        }
    }

    private class c21 extends BaseSqlObject {
        public c21(DbContext context) {
            super(context);

        }
    }

    private class c211 extends BaseSqlObject {
        public c211(DbContext context) {
            super(context);

        }
    }

    private class c3 extends BaseSqlObject {
        public c3(DbContext context) {
            super(context);

        }
    }

    private class c4 extends BaseSqlObject {
        public c4(DbContext context) {
            super(context);

        }
    }

    private class c5 extends BaseSqlObject {
        public c5(DbContext context) {
            super(context);

        }
    }
}
