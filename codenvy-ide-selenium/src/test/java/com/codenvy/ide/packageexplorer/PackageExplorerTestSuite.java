/*
 *
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
 *  All Rights Reserved.
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
package com.codenvy.ide.packageexplorer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Evgen Vidolob
 *
 */
@RunWith(Suite.class)
@SuiteClasses({PackageExplorerCutAndPasteFromContextMenuTest.class,
               PackageExplorerCopyAndPasteFromContextMenuTest.class, PackageExplorerLinkWithEditorTest.class,
               PackageExplorerShowApplicationLogsFromContextMenuTest.class,
               PackageExplorerCreationContextMenuTest.class,
               PackageExplorerPackageCreationTest.class, PackageExplorerSimpleOperationsTest.class,
               PackageExplorerReferencedLibsTest.class, PackageExplorerStartAndStopApplicationFromContextMenuTest.class,
               PackageExplorerDeleteFromContextMenuTest.class, PackageExplorerRenameFromContextMenuTest.class,
               PackageExplorerOpenFileTest.class})
public class PackageExplorerTestSuite {

}
