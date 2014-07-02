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
package com.codenvy.ide.project;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 @author Roman Iuvshin
 *
 */
public class CheckOpenNonExistedFileByURLTest extends BaseTest {

    private static final String PROJECT   = "existedPrj";

    protected static Map<String, Link> project;

    private static final String WARNING_MESSAGE = "The requested file URL was not found on this project.";

    private static final String WARNING_TITLE = "Not found resource";

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void checkOpenNonExistedProjectByURLTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        String wrongFileNameUrl = PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME + "/" + PROJECT + "/"+ System.currentTimeMillis()+".xml";
        System.out.println(wrongFileNameUrl);
        driver.get(wrongFileNameUrl);
        IDE.WARNING_DIALOG.waitOpened();
        IDE.WARNING_DIALOG.waitTextInDialogPresent(WARNING_MESSAGE);
        IDE.WARNING_DIALOG.waitTitleInDialogPresent(WARNING_TITLE);
        IDE.LOADER.waitClosed();
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
    }

    @Test
    public void checkThatWarningPopupNotAppearAfterRefresh() throws Exception {
        driver.navigate().refresh();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.LOADER.waitClosed();
        IDE.WARNING_DIALOG.waitClosed();
    }
}
