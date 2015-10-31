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

package org.qamatic.mintleaf.oracle.codeobjects;

public class PLStringBuilder {
    private final StringBuilder mvsb = new StringBuilder();

    public static String cleanStr(String str) {
        return str.replaceAll(" ", "").replaceAll(String.format("%n"), "").replaceAll("\t", "").replaceAll(String.format("\n"), "");
    }

    public void append(String txt) {
        mvsb.append(txt);
    }

    public void append(int indent, String txt) {
        for (int i = 0; i < indent; i++) {
            txt = "\t" + txt;
        }
        mvsb.append(txt);
    }

    public void appendLine() {
        appendLine("");
    }

    public void appendLine(int indent, String txt) {
        for (int i = 0; i < indent; i++) {
            txt = "\t" + txt;
        }
        append(String.format(txt + "%n"));
    }

    public void appendLine(String txt) {
        append(String.format(txt + "%n"));
    }

    public void appendLineCustom(String txt) {
        append(String.format(txt + "\n"));
    }

    @Override
    public String toString() {
        return mvsb.toString();
    }

    public StringBuilder getStringBuilder() {
        return mvsb;
    }

}
