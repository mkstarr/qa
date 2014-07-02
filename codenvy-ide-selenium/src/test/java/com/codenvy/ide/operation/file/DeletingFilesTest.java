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
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * IDE-11: Deleting files.
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:musienko.maxim@gmail.com">Musienko Maxim</a>
 *
 */
public class DeletingFilesTest extends BaseTest {

    private static String PROJECT = DeletingFilesTest.class.getSimpleName();

    private static String HTML_FILE_NAME = "newHtmlFile.html";

    private static String JAVA_SCRIPT_FILE_NAME = "newJavaScriptFile.js";

    private static String XML_FILE_NAME = "newXMLFile.xml";

    private static String TEXT_FILE_NAME = "newTxtFile.txt";
    
    private static String PHP_FILE_NAME = "newPhpFile.php";
    
    private static String RUBY_FILE_NAME = "newRubyFile.rb";

    private static String PYTHON_FILE_NAME = "newRubyFile.py";
    
    private static String CSS_FILE_NAME = "newCSSFile.css";
    
    private static String JAVA_FILE_NAME = "newJavaFile.java";
    
    private static String JSP_FILE_NAME = "newJSPFile.jsp";
    
    private static String YAML_FILE_NAME = "newYamlFile.yaml";
    
    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFile(link, HTML_FILE_NAME, MimeType.TEXT_HTML, "");
            VirtualFileSystemUtils.createFile(link, JAVA_SCRIPT_FILE_NAME, MimeType.APPLICATION_JAVASCRIPT, "");
            VirtualFileSystemUtils.createFile(link, XML_FILE_NAME, MimeType.APPLICATION_XML, "");
            VirtualFileSystemUtils.createFile(link, TEXT_FILE_NAME, MimeType.TEXT_PLAIN, "");
            VirtualFileSystemUtils.createFile(link, PHP_FILE_NAME, MimeType.APPLICATION_PHP, "");
            VirtualFileSystemUtils.createFile(link, RUBY_FILE_NAME, MimeType.APPLICATION_RUBY, "");
            VirtualFileSystemUtils.createFile(link, PYTHON_FILE_NAME, MimeType.TEXT_X_PYTHON, "");
            VirtualFileSystemUtils.createFile(link, CSS_FILE_NAME, MimeType.TEXT_CSS, "");
            VirtualFileSystemUtils.createFile(link, JAVA_FILE_NAME, MimeType.APPLICATION_JAVA, "package helloworld; public class Test { }");
            VirtualFileSystemUtils.createFile(link, JSP_FILE_NAME, MimeType.APPLICATION_JSP, "");
            VirtualFileSystemUtils.createFile(link, YAML_FILE_NAME, MimeType.TEXT_YAML, "");
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

    // IDE-11: Deleting files.
    @Test
    public void testDeletingFile() throws Exception {

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);

        String path = PROJECT + "/" + JAVA_SCRIPT_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());

        path = PROJECT + "/" + XML_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());

        path = PROJECT + "/" + TEXT_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());

        path = PROJECT + "/" + HTML_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());
        
        path = PROJECT + "/" + PHP_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());

        path = PROJECT + "/" + RUBY_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());
        
        path = PROJECT + "/" + PYTHON_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());
        
        path = PROJECT + "/" + CSS_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());
        
        path = PROJECT + "/" + JAVA_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());
        
        path = PROJECT + "/" + JSP_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());
        
        path = PROJECT + "/" + YAML_FILE_NAME;
        IDE.EXPLORER.openItem(path);
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.waitForItem(path);
        IDE.EXPLORER.selectItem(path);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(path);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + path).getStatusCode());

    }
}
