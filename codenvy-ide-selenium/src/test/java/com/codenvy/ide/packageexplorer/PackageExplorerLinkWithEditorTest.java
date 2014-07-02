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
package com.codenvy.ide.packageexplorer;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * @author Roman Iuvshin
 *
 */
public class PackageExplorerLinkWithEditorTest extends BaseTest {
    private static final String PROJECT = "LinkEditorPrj";

    final static String filePath =
            "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip";

    private static final String FIRST_FILE = "index.jsp";

    private static final String SECOND_FILE = "web.xml";

    static Map<String, Link> project;

    @BeforeClass
    public static void setUp() {
        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void linkWithEditorTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.EXPLORER.clickCloseProjectExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.clickOnLinkWithEditorButton();

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FIRST_FILE);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FIRST_FILE);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitTabPresent(FIRST_FILE);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("WEB-INF");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("WEB-INF");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(SECOND_FILE);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(SECOND_FILE);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitTabPresent(SECOND_FILE);

        //close folders
        IDE.PACKAGE_EXPLORER.openItemWithClickOnOpenIcon("WEB-INF");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithClickOnOpenIcon("src");

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(FIRST_FILE);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(SECOND_FILE);
        IDE.EDITOR.selectTab(FIRST_FILE);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FIRST_FILE);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(SECOND_FILE);
        IDE.EDITOR.selectTab(SECOND_FILE);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(SECOND_FILE);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FIRST_FILE);
    }
}