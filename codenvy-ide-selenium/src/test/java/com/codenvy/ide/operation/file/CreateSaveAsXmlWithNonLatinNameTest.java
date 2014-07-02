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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.*;

/**
 * IDE-47: Creating and "Saving As" new XML file with non-latin name.
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author Oksana Vereshchaka
 *
 */
public class CreateSaveAsXmlWithNonLatinNameTest extends BaseTest {
    private static final String PROJECT = CreateSaveAsXmlWithNonLatinNameTest.class.getSimpleName();

    /** Name of first XML file. */
    private static final String XML_FILE = "файл";

    /** Name of second XML file */
    private static final String NEW_XML_FILE = "новый файл";

    /** Content of first XML file. */
    private static final String XML_CONTENT = "<?xml version='1.0' encoding='UTF-8'?><test>test</test>";

    /** Content of secont XML file. */
    private static final String XML_CONTENT_2 =
            "<?xml version='1.0' encoding='UTF-8'?>\n" + "<settings>test</settings>";

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);

        } catch (Exception e) {
            fail("Cant create project ");
        }
    }

    /**
     * Test added to Ignore, because at the moment not solved a problem with encoding Cyrillic characters to URL. For
     * example: create new file with cyrillic name, save him, and get URL in IDE. In URL IDE we shall see encoding
     * characters in file name
     *
     * @throws Exception
     */
    @Test
    public void testCreateAndSaveAsXmlWithNonLatinName() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        IDE.EDITOR.waitActiveFile();

        IDE.EDITOR.waitTabPresent("Untitled file.xml");
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.SAVE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.SAVE_AS);

        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.typeTextIntoEditor(XML_CONTENT);
        IDE.EDITOR.saveAs(1, XML_FILE);

        String pathXml = PROJECT + "/" + XML_FILE;
        IDE.EXPLORER.waitForItem(pathXml);

        assertEquals(XML_FILE, IDE.EDITOR.getTabTitle(1));

        // check file properties
        IDE.PROPERTIES.openProperties();
        // code mirror adds one more newline at the end of file
        checkProperties(XML_CONTENT.length(), MimeType.TEXT_XML, XML_FILE);

        IDE.EDITOR.closeFile(1);

        // check file on server
        assertTrue(VirtualFileSystemUtils.getContent(
                REST_URL + "contentbypath/" + PROJECT + "/" + URLEncoder.encode(XML_FILE, "UTF-8")).contains("?><test>test</test>"));


        IDE.EXPLORER.waitItemPresent(pathXml);

        IDE.EXPLORER.openItem(pathXml);
        IDE.EDITOR.waitActiveFile();

        // change file content
        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.typeTextIntoEditor(XML_CONTENT_2);

        // save as file
        IDE.EDITOR.saveAs(1, NEW_XML_FILE);
        String pathNewXml = PROJECT + "/" + NEW_XML_FILE;
        IDE.EXPLORER.waitForItem(pathNewXml);

        assertEquals(NEW_XML_FILE, IDE.EDITOR.getTabTitle(1));
        IDE.PROPERTIES.openProperties();
        // code mirror adds one more newline at the end of file
        checkProperties(XML_CONTENT_2.length(), MimeType.TEXT_XML, NEW_XML_FILE);

        IDE.EDITOR.closeFile(1);

        // check two files exist
        assertTrue(VirtualFileSystemUtils.getContent(
                REST_URL + "contentbypath/" + PROJECT + "/" + URLEncoder.encode(XML_FILE, "UTF-8")).contains("?><test>test</test>"));
        assertTrue(VirtualFileSystemUtils.getContent(
                REST_URL + "contentbypath/" + PROJECT + "/" + URLEncoder.encode(NEW_XML_FILE, "UTF-8")).contains(">\n" + "<settings>test</settings>"));

        IDE.EXPLORER.waitItemPresent(pathXml);
        IDE.EXPLORER.waitItemPresent(pathNewXml);
    }

    /**
     * Check properties values.
     *
     * @param contentLengh
     *         content length property
     * @param contentType
     *         content type
     * @param displayName
     *         display name
     * @throws Exception
     */
    private void checkProperties(int contentLengh, String contentType, String displayName) throws Exception {
        assertEquals(contentType, IDE.PROPERTIES.getContentType());
        assertEquals(displayName, IDE.PROPERTIES.getDisplayName());
        //in FF browser codemirror return +1 symbols, in chrome - no

        assertTrue(Integer.parseInt(IDE.PROPERTIES.getContentLength()) == contentLengh
                   || Integer.parseInt(IDE.PROPERTIES.getContentLength()) == contentLengh + 1);
    }

}
