package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;
import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by codenvy on 17.02.14.
 */
public class CheckSymbIntoCollabEditorWithOSNatyveTest extends BaseTest {
    private static final String PROJECT = CheckSymbIntoCollabEditorWithOSNatyveTest.class.getSimpleName();
    String mainKeySymbols = "`0123456789-=qwertyuiop[]asdfghjkl;'zxcvbnm,./\\";
    String mainKeySymbolsWithEnabledShift = "~)!@#$%^&*(_+QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?|";

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
         testMainKeys();
         IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(mainKeySymbols);
        testMainKeysWithEnabledShift();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(mainKeySymbolsWithEnabledShift);
    }

    public void testMainKeys() throws AWTException, InterruptedException {
        Robot rb = new Robot();
        for (Integer keyEvent : new KeyEventsUtils().getKeys()) {
            rb.keyPress(keyEvent);
            //for emulating user's delays
            Thread.sleep(200);
            rb.keyRelease(keyEvent);
        }
    }

    public void testMainKeysWithEnabledShift() throws AWTException, InterruptedException {
        Robot rb = new Robot();
        rb.keyPress(KeyEvent.VK_SHIFT);
        for (Integer keyEvent : new KeyEventsUtils().getKeys()) {

            rb.keyPress(keyEvent);
            //for emulating user's delays
            Thread.sleep(200);
            rb.keyRelease(keyEvent);
        }
        rb.keyRelease(KeyEvent.VK_SHIFT);
    }
}