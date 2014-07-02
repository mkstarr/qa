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

import com.codenvy.ide.*;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Oksana Vereshchaka
 *
 */
public class PreviewHtmlFileTest extends BaseTest {
    private final static String PROJECT = PreviewHtmlFileTest.class.getSimpleName();

    private final static String FILE_NAME = "PreviewHtmlFile.html";

    @BeforeClass
    public static void setUp() {
        String filePath = "src/test/resources/org/exoplatform/ide/operation/file/PreviewHtmlFile.html";
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link linkFile = project.get(Link.REL_CREATE_FILE);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, FILE_NAME, MimeType.TEXT_HTML, filePath);
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

    /**
     * IDE-65: Preview HTML File
     *
     * @throws Exception
     */
    @Test
    public void previewHtmlFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();

      /*
       * 1. open "PreviewHtmlFileTest/PreviewHtmlFile.html" file
       */
        String path = PROJECT + "/" + FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
      /*
       * 2. "Preview" button must be enabled
       */
        IDE.MENU.waitCommandEnabled(MenuCommands.Run.RUN, MenuCommands.Run.SHOW_PREVIEW);
        IDE.TOOLBAR.waitButtonPresentAtRight(ToolbarCommands.Run.SHOW_PREVIEW);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Run.SHOW_PREVIEW);

      /*
       * 3. Click on "Preview" button
       */
        IDE.TOOLBAR.runCommand(ToolbarCommands.Run.SHOW_PREVIEW);
        IDE.PREVIEW.waitHtmlPreviewOpened();

        IDE.PREVIEW.selectPreviewHtmlIFrame();
        assertTrue(driver.findElements(By.xpath("//p/b/i[text()='Changed Content.']")).size() > 0);
        assertTrue(driver.findElements(
                By.xpath("//img[@src='http://www.google.com.ua/intl/en_com/images/logo_plain.png']")).size() > 0);
        IDE.selectMainFrame();

      /*
       * 4. Close "Preview".
       */
        Thread.sleep(TestConstants.SLEEP_SHORT);
        IDE.PREVIEW.closeView();

      /*
       * 5. Close "PreviewHtmlFile.html"
       */
        IDE.EDITOR.closeFile(FILE_NAME);

      /*
       * 6. Reopen "PreviewHtmlFile.html" and click "Preview".
       */
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.openItem(path);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.SHOW_PREVIEW);

      /*
       * 7. Check "Preview" again.
       */
        IDE.PREVIEW.waitHtmlPreviewOpened();
        assertTrue(IDE.PREVIEW.isHtmlPreviewOpened());
        IDE.PREVIEW.selectPreviewHtmlIFrame();
        assertTrue(driver.findElements(By.xpath("//p/b/i[text()='Changed Content.']")).size() > 0);
        assertTrue(driver.findElements(
                By.xpath("//img[@src='http://www.google.com.ua/intl/en_com/images/logo_plain.png']")).size() > 0);
        IDE.selectMainFrame();

      /*
       * 8. Close all tabs in editor.
       */
        IDE.EDITOR.closeFile(FILE_NAME);
    }
}
