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
package com.codenvy.ide.operation.file;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.operation.java.ServicesJavaTextFuction;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * @author Musienko Maxim
 *
 */
public class DeleteFileFromPackagetExplorerTest extends BaseTest {
    private static final String PROJECT = DeleteFileBeforeAllProjectWasBeSelectedTest.class.getSimpleName();

    final String SOURCE_FOLDER = "src/main/java";

    final String PACKAGE = "sumcontroller";

    final String CLASS = "SumController.java";

    final String SRC = "src";

    final String POM = "pom.xml";

    final String WEB_INF = "WEB-INF";

    final String WEBAPP = "webapp";

    final String MAIN_FOLDER = "main";


    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/calc.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
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
    public void deleteFromPackageExplorer() throws Exception {
        ServicesJavaTextFuction servOperation = new ServicesJavaTextFuction();
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        servOperation.expandAllNodesForCalcInPackageExplorer();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("calc.jsp");
        IDE.DELETE.deleteSelectedItems();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("calc.jsp");

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(SOURCE_FOLDER);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PACKAGE);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(CLASS);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(SRC);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(POM);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(WEB_INF);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(WEBAPP);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(MAIN_FOLDER);

    }
}
