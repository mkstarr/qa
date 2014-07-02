package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/** @author Musienko Maxim */

public class CheckMailValidationFromFormTest extends BaseTest {
    private static final String PROJECT = CheckMailValidationFromFormTest.class.getSimpleName();

    public static final String PATH = "src/test/resources/org/exoplatform/ide/project/JavaScriptAutoComplete.zip";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT, PATH);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void after() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void checkMailTest() throws Exception {
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        IDE.OPEN.openProject(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        IDE.FACTORY_URL.clickOnShareViaEmail();
        IDE.FACTORY_URL.waitSendFactoryURLViaEmailFormOpened();
        IDE.FACTORY_URL.typeEmailInSendFactoryURLViaEmailForm("max.codenvy.com");
        IDE.FACTORY_URL.waitSendMailBtnIsDisabled();
        IDE.FACTORY_URL.typeEmailInSendFactoryURLViaEmailForm("max@codenvy.com");
        IDE.FACTORY_URL.waitSendMailBtnIsEnabled();
    }
}