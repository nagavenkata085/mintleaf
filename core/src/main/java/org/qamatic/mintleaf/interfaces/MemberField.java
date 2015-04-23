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

public abstract class MemberField implements CodeObject {

    protected final String mvleftSide;
    protected final String mvrightSide;

    public MemberField(String leftSide, String rightSide) {
        mvleftSide = leftSide;
        mvrightSide = rightSide;
    }

    public String getLeftSide() {
        return mvleftSide;
    }

    public String getrightSide() {
        return mvrightSide;
    }

    @Override
    public String toString() {
        return mvleftSide + " " + mvrightSide;
    }

}
