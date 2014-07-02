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
public class DeleteFileBeforeAllProjectWasBeSelectedTest extends BaseTest {
    private static final String PROJECT = "DeleteF";

    private final String PATH_TO_JSP = PROJECT + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "calc.jsp";

    private final String PATH_TO_WEBINF = PROJECT + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "WEB-INF";

    private final String PATH_TO_WEBAPP = PROJECT + "/" + "src" + "/" + "main" + "/" + "webapp";

    private final String PATH_TO_CLASS = PROJECT + "/" + "src" + "/" + "main" + "/" + "java" + "/" + "sumcontroller"
                                         + "/" + "SumController.java";

    private final String PAPH_TO_SUMC_CON_FOLDER = PROJECT + "/" + "src" + "/" + "main" + "/" + "java" + "/"
                                                   + "sumcontroller";

    private final String JAVA_FOLDER = PROJECT + "/" + "src" + "/" + "main" + "/" + "java";

    private final String WEBAPP_FOLDER = PROJECT + "/" + "src" + "/" + "main" + "/" + "webapp";

    private final String MAIN_FOLDER = PROJECT + "/" + "src" + "/" + "main";

    private final String SRC_FOLDER = PROJECT + "/" + "src";

    private final String XML_FILE = PROJECT + "/" + "pom.xml";

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
    public void selectAllTesUi() throws Exception {
        ServicesJavaTextFuction servOperation = new ServicesJavaTextFuction();
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        IDE.EXPLORER.selectProjectByNameInProjectsListGrid(PROJECT);
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.closePackageExplorer();
        IDE.EXPLORER.waitOpened();
        servOperation.expandAllNodesForCalcInProjectExplorer(PROJECT);
        IDE.EXPLORER.selectItem(PATH_TO_JSP);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitItemNotPresent(PATH_TO_JSP);

        IDE.EXPLORER.waitItemPresent(PATH_TO_WEBINF);

        IDE.EXPLORER.waitItemPresent(PATH_TO_WEBAPP);
        IDE.EXPLORER.waitItemPresent(PATH_TO_CLASS);
        IDE.EXPLORER.waitItemPresent(PAPH_TO_SUMC_CON_FOLDER);
        IDE.EXPLORER.waitItemPresent(WEBAPP_FOLDER);
        IDE.EXPLORER.waitItemPresent(MAIN_FOLDER);
        IDE.EXPLORER.waitItemPresent(SRC_FOLDER);
        IDE.EXPLORER.waitItemPresent(XML_FILE);

    }

}
