/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Roman Iuvshin
 *
 */
@RunWith(Suite.class)
@SuiteClasses({AndroidAppRunTest.class,
               GAEJavaWebApplicationRunTest.class,
               JavaLibraryApplicationNotRunTest.class,
               JavaScriptApplicationRunTest.class,
               JavaWebAppAmazonS3RunTest.class,
               JavaWebApplicationRunTest.class,
               MavenMultiModuleApplicationRunTest.class,
               NodeJsApplicationRunTest.class,
               PhpApplicationRunTest.class,
               ProjectWithDatasourceRunTest.class,
               PythonApplicationRunTest.class,
               PythonGAEApplicationRunTest.class,
               RubyOnRailsApplicationNotRunTest.class,
               SpringApplicationRunTest.class,
               SpringMVCAplicationRunTest.class
              })
public class RunnerTestSuite {
}
