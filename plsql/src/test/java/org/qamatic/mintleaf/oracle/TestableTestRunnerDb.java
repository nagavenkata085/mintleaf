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

package org.qamatic.mintleaf.oracle;

import org.junit.runners.model.InitializationError;
import org.qamatic.mintleaf.interfaces.DbModule;
import org.qamatic.mintleaf.oracle.junitsupport.DbWorksTestRunner;

public class TestableTestRunnerDb extends DbWorksTestRunner {

    private DbModule mvinstaller;

    public TestableTestRunnerDb(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected void bootStrapperCheck(Object testInstance) throws IllegalStateException {

    }


    public int getInstallerState() {
        return mvtestInstallerState;
    }

    public boolean getDatabaseReady() {
        return mvdatabaseReady;
    }

    public void reset() {
        mvdatabaseReady = false;
        mvtestInstallerState = 0;

    }

}
