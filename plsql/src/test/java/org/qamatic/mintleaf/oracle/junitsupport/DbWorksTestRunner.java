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

package org.qamatic.mintleaf.oracle.junitsupport;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.qamatic.mintleaf.interfaces.DbModule;
import org.qamatic.mintleaf.oracle.DbUtility;
import org.qamatic.mintleaf.oracle.TestInstaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbWorksTestRunner extends BlockJUnit4ClassRunner {

    private static final int INSTALLATION_SUCCESSFULL = 0;
    protected static int mvtestInstallerState = 0;
    protected static boolean mvdatabaseReady = false;
    protected static boolean mvcallOnce = true;
    private static DbUtility mvutils;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private int mvcurrentTestCount;

    public DbWorksTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }


    @Override
    protected Object createTest() throws Exception {
        Object testInstance = super.createTest();
        bootStrapperCheck(testInstance);
        return testInstance;
    }

    protected void bootStrapperCheck(Object testInstance) throws IllegalStateException {

        if (!mvdatabaseReady && mvcallOnce) {
            try {
                mvcallOnce = false;
                logger.info("Setting up schema");

                DbModule installer = new TestInstaller(TestDatabase.getSysDbaContext(), TestDatabase.getSchemaOwnerContext());
                mvtestInstallerState = installer.install();
                mvutils = new DbUtility(TestDatabase.getSchemaOwnerContext());
                mvutils.createAll();
            } catch (Exception e1) {
                logger.error("error bootStrapping: ", e1);
                mvtestInstallerState = -1;
            }

            mvdatabaseReady = mvtestInstallerState == INSTALLATION_SUCCESSFULL;

        }
    }

    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {

        if (mvtestInstallerState == INSTALLATION_SUCCESSFULL) {
            super.runChild(frameworkMethod, notifier);
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        mvcurrentTestCount = 0;
        if (mvtestInstallerState == INSTALLATION_SUCCESSFULL) {
            super.run(notifier);
        }

    }

    @Override
    protected Statement withAfters(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
        if (mvutils != null) {
            //mvutils.cascadeDelete();
        }
        mvcurrentTestCount++;
        return super.withAfters(frameworkMethod, testInstance, statement);
    }

}
