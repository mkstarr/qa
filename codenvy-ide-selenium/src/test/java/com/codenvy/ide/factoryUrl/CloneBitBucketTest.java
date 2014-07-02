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
package com.codenvy.ide.factoryUrl;

import junit.framework.Assert;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/** @author Musienko Maxim */
public class CloneBitBucketTest extends BaseTest {

    String text = "Text" +
                  DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US).format(new Date()).replaceAll("[/:]", "")
                            .replaceAll(" ", "").toLowerCase();

    String commitMessage = "update BitBucket" +
                           DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US).format(new Date())
                                     .replaceAll("[/:]", "").replaceAll(" ", "").toLowerCase();

    String testFile = "changelog.txt";

    @Override
    @Before
    public void start() throws Exception {

        //Choose browser Web driver:
        String bitbucketUrl = "https://bitbucket.org";
        ;

        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new FirefoxDriver();
        }

        IDE = new com.codenvy.ide.IDE(bitbucketUrl, driver);
        try {
            driver.manage().window().maximize();
            driver.get(bitbucketUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        new BitbucketAPIUtils().checkAviailableBitBucketKeysAndDelete();
    }


    @Test
    public void cloneBitBucketPrivateProject() throws Exception {
        IDE.BITBUCKET.signInToBitBucket(TENANT_NAME, readCredential("bitbucket.pass"));
        IDE.BITBUCKET.selectProjectInProjectList("exoinvitemain/scribe-java");
        IDE.BITBUCKET.clickOnCloudIdeListBtn();
        IDE.BITBUCKET.clickOnCloudIdeCodenvy();
        driver.get(createFactoryURL());
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(testFile);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(testFile);
        IDE.EDITOR.selectTab(testFile);
        IDE.EDITOR.waitActiveFile();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.moveCursorRight(10);
        IDE.EDITOR.typeTextIntoEditor(text);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        // Commit
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.clickAddAllChangesCheckboxInCommitForm();
        IDE.GIT.typeCommitMessage(commitMessage);
        IDE.GIT.clickCommitButton();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.LOADER.waitClosed();
        IDE.GIT.selectOnPushFormRemoteBranchInDropDown("master");
        IDE.GIT.clickPushBtn();
        IDE.OUTPUT.waitForSubTextPresent("Successfully pushed to ssh://git@bitbucket.org/exoinvitemain/scribe-java.git");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        Assert.assertTrue(new BitbucketAPIUtils().getAllDiffContentByMess(commitMessage).contains(text));
    }


    public String createFactoryURL() {
        String factoryUrl = IDE.BITBUCKET.createFactoryUrl();
        factoryUrl = PROTOCOL + "://" + IDE_HOST + factoryUrl.substring(19);
        return factoryUrl;
    }
}
