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
