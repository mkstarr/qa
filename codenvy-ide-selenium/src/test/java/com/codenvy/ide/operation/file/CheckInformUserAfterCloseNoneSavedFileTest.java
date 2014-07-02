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
package com.codenvy.ide.operation.file;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.EnumBrowserCommand;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CheckInformUserAfterCloseNoneSavedFileTest extends BaseTest {


    private static String PROJECT = ClosingAndSaveAsFileTest.class.getSimpleName();

    private static String TEST_FILE = "newXMLFile.xml";

    private static String TEST_FILE2 = "newCssFile.css";

    private static String ALERT_FIREFOX_LABEL =
            "This page is asking you to confirm that you want to leave - data you have entered may not be saved.";

    private static String ALERT_CHROME_LABEL = "You have unsaved files, that may be lost!";

    @BeforeClass
    public static void setUp() {
        String filePath = "src/test/resources/org/exoplatform/ide/operation/file/";
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);

            Link link = project.get(Link.REL_CREATE_FILE);

            VirtualFileSystemUtils.createFileFromLocal(link, TEST_FILE, MimeType.APPLICATION_XML, filePath
                                                                                                  + "newXMLFile.xml");
            VirtualFileSystemUtils.createFileFromLocal(link, TEST_FILE2, MimeType.TEXT_CSS, filePath
                                                                                            + "newCssFile.css");

        } catch (Exception e) {
            fail("Cant create project ");
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
    public void checkInformAfterClose() throws Exception {
        // step one, open file, change content and does not save
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();

        // step one, reopen file, change content and does not save
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FILE2);
        IDE.EXPLORER.openItem(PROJECT + "/" + TEST_FILE2);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("TEST123");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("TEST123");
        IDE.EDITOR.waitFileContentModificationMark(TEST_FILE2);
        // step two, try closed not saved file, check inform in pop up window
        // in chrome and firefox browsers pop up messages is different
        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.FIREFOX)) {
            driver.close();
            IDE.POPUP.waitOpened();
            assertEquals(IDE.POPUP.getTextFromAlert(), ALERT_FIREFOX_LABEL);
        } else {
            // used refresh for chrome because current version of chrome driver just killing browser instead close.
            driver.switchTo().defaultContent();
            driver.navigate().refresh();
            IDE.POPUP.waitOpened();
            assertTrue(IDE.POPUP.getTextFromAlert().contains(ALERT_CHROME_LABEL));
        }
        IDE.POPUP.dismissAlert();
        IDE.EDITOR.closeTabIgnoringChanges(TEST_FILE2);
    }



    @Test
    public void checkInformAfterRefresh() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FILE);
        IDE.WELCOME_PAGE.close();
        IDE.EXPLORER.openItem(PROJECT + "/" + TEST_FILE);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.waitContentIsClear();
        IDE.EDITOR.waitFileContentModificationMark(TEST_FILE);
        // step two, check inform after refresh browser
        // in chrome and firefox browsers pop up messages is different
        driver.switchTo().defaultContent();
        driver.navigate().refresh();
        IDE.POPUP.waitOpened();

        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.FIREFOX)) {
            assertEquals(IDE.POPUP.getTextFromAlert(), ALERT_FIREFOX_LABEL);
        } else {
            assertTrue(IDE.POPUP.getTextFromAlert().contains(ALERT_CHROME_LABEL));
        }
        IDE.POPUP.dismissAlert();
        IDE.EDITOR.closeTabIgnoringChanges(TEST_FILE);
    }
}
