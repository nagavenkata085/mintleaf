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

package org.qamatic.mintleaf.interfaces;

public class ArgumentTypeMap implements SqlArgumentTypeMap {

    private String mvcol1;
    private String mvcol2;

    public ArgumentTypeMap() {
    }

    public ArgumentTypeMap(String col1, String col2) {
        mvcol1 = col1;
        mvcol2 = col2;
    }

    @Override
    public String getCol1() {
        return mvcol1;
    }

    @Override
    public void setCol1(String col1) {
        mvcol1 = col1;
    }

    @Override
    public String getCol2() {
        return mvcol2;
    }

    @Override
    public void setCol2(String col2) {
        mvcol2 = col2;
    }

}
