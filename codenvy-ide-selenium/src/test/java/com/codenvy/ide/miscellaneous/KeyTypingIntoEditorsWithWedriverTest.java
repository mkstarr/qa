/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;
import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.util.Map;

/**
 * @author Musienko Maxim
 */
public class KeyTypingIntoEditorsWithWedriverTest extends BaseTest {

    private static final String PROJECT = KeyTypingIntoEditorsWithWedriverTest.class.getSimpleName();
    private String lowerCasePreset = "`1234567890-=qwertyuiop[]asdfghjkl;'zxcvbnm,./ \\ \"";
    private String upperCasePreset = "~!@#$%^&*()_+QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<> ?|";
    private String numpadAndOtherSymbols = "0123456789;=+*.";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/project/javascriptProject.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void typingIntoCollabEditor() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.openItem(PROJECT + "/" + "js");
        IDE.EXPLORER.waitForItem(PROJECT + "/" + "js" + "/script.js");
        IDE.EXPLORER.openItem(PROJECT + "/" + "js" + "/script.js");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(lowerCasePreset);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(lowerCasePreset);
        clearEditor();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(upperCasePreset);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(upperCasePreset);
        clearEditor();
        testNumLockKeys();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(numpadAndOtherSymbols);
    }

    private void clearEditor() throws Exception {
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "a");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.DELETE.toString());
        IDE.JAVAEDITOR.waitWhileJavaEditorIsEmpty();
    }

    public void testNumLockKeys() throws Exception {
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD0.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD1.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD2.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD3.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD4.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD5.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD6.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD7.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD8.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.NUMPAD9.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SEMICOLON.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.EQUALS.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ADD.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.MULTIPLY.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.DECIMAL.toString());
    }
}
