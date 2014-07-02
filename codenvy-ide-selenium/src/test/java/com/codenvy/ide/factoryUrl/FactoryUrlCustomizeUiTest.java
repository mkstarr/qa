package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/** @author Musienko Maxim */
public class FactoryUrlCustomizeUiTest extends BaseTest {
    private static final String PROJECT = "factory_prj";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/project/JavaScriptAutoComplete.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void checkButtonDisabledStateTest() throws Exception {
        //go to the IDE, call Factory URL, check init states into customize section
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + "js");
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.waitShowNumProjectChkBoxIsChecked();
        IDE.FACTORY_URL.waitSpecifiedNumberOfCounterThProject(0);
        IDE.FACTORY_URL.waitWhiteStyleCodenwyImg();
        //click on codenow img-button and check counter of the projects
        checkCounterOfTheApp(3);
        IDE.selectMainFrame();
        //click on dark style buttom and check style
        IDE.FACTORY_URL.clickRadioBtnDark();
        IDE.FACTORY_URL.waitDarkStyleCodenvyImg();
        //click on vertical radiobutton and check this
        IDE.FACTORY_URL.clickRadioBtnVertical();
        IDE.FACTORY_URL.waitVerticalStyleIsSetted();
        //uncheck number of project checkbox and check disappearing number of project button
        IDE.FACTORY_URL.clickOnShowNumberOfProjectCheckBox();
        IDE.FACTORY_URL.waitCodenowButtonDisappear();
        IDE.FACTORY_URL.clickOnShowNumberOfProjectCheckBox();
        IDE.FACTORY_URL.clickRadioBtnHorizontal();
        IDE.FACTORY_URL.clickRadioBtnWhite();
        IDE.FACTORY_URL.waitHorizontalStyleIsSetted();
        IDE.FACTORY_URL.waitWhiteStyleCodenwyImg();
        IDE.FACTORY_URL.waitSpecifiedNumberOfCounterThProject(0);


    }

    private void checkCounterOfTheApp(int countVal) {
        for (int i = 0; i < countVal; i++)
            IDE.FACTORY_URL.clickOnCodenowBtnIndependStyle();
        IDE.FACTORY_URL.waitSpecifiedNumberOfCounterThProject(countVal);
    }


}
