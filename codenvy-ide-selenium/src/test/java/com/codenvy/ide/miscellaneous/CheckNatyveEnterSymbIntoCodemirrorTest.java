package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.File;
import com.codenvy.ide.operation.java.ServicesJavaTextFuction;
import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by codenvy on 19.02.14.
 */
public class CheckNatyveEnterSymbIntoCodemirrorTest extends BaseTest {
    private static final String PROJECT = KeyTypingIntoEditorsWithWedriverTest.class.getSimpleName();
    String mainKeySymbols = "`0123456789-=qwertyuiop[]asdfghjkl;'zxcvbnm,./\\";
    String mainKeySymbolsWithEnabledShift = "`0123456789-=qwertyuiop[]asdfghjkl;'zxcvbnm,./\\~)!@#$%^&*(_+QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?|";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/calc.zip";

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
        new ServicesJavaTextFuction().expandAllNodesForCalcInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("calc.jsp");
        IDE.EDITOR.waitActiveFile();
        clearContent();
        testMainKeys();
        IDE.EDITOR.waitContentIsPresent(mainKeySymbols);
        testMainKeysWithEnabledShift();
        IDE.EDITOR.waitContentIsPresent(mainKeySymbolsWithEnabledShift);
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

    public void clearContent() throws Exception {
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "a");
        IDE.EDITOR.typeTextIntoEditor(Keys.DELETE.toString());
        IDE.EDITOR.waitContentIsClear();
    }

}
