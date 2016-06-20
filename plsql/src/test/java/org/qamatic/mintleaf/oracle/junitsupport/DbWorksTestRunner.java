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

package org.qamatic.mintleaf.oracle.junitsupport;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.qamatic.mintleaf.interfaces.DbModule;
import org.qamatic.mintleaf.oracle.OracleDbUtility;
import org.qamatic.mintleaf.oracle.TestInstaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbWorksTestRunner extends BlockJUnit4ClassRunner {

    private static final int INSTALLATION_SUCCESSFULL = 0;
    protected static int mvtestInstallerState = 0;
    protected static boolean mvdatabaseReady = false;
    protected static boolean mvcallOnce = true;
    private static OracleDbUtility mvutils;
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
                mvutils = new OracleDbUtility(TestDatabase.getSchemaOwnerContext());
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
