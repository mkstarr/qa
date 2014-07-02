/*
 *
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2014] Codenvy, S.A.
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
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author Zaryana Dombrovskaya
 */
public class FactoryUrlGerritHubTest extends BaseTest {

    private static final String PROJECT_NAME = "jsTestProject";

    private static final String COMMIT_MESSAGE = String.valueOf(System.currentTimeMillis());

    private static final String CHANGE_TEXT = String.valueOf(System.currentTimeMillis());

    private static final String succesPushMessage = "Successfully pushed to https://exoinvitemain:VLPN9MevgP6k@review.gerrithub.io/exoinvitemain/jsTestProject";

    private static final String PROJECT_NAME_GERRIT = "exoinvitemain/jsTestProject";

    @Override
    @Before
    public void start() throws Exception {

        String gerritHubUrl = "https://review.gerrithub.io/static/intro.html";;

        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new FirefoxDriver();
        }

        IDE = new com.codenvy.ide.IDE(gerritHubUrl, driver);
        try {
            driver.manage().window().maximize();
            driver.get(gerritHubUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cloneGerritHubProject() throws Exception {
        IDE.GERRITHUB.signInToGerritHub(USER_NAME, USER_PASSWORD);
        driver.get(createFactoryURL());
        IDE.LOADER.waitClosed();
//        IDE.ASK_DIALOG.waitOpened();
//        IDE.LOADER.waitClosed();
//        IDE.ASK_DIALOG.clickYes();
//        IDE.ASK_DIALOG.waitClosed();
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        String url = driver.getCurrentUrl();
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItem(PROJECT_NAME + "/js");
        IDE.EXPLORER.openItem(PROJECT_NAME + "/js");
        IDE.EXPLORER.waitForItem(PROJECT_NAME + "/js/script.js");
        IDE.EXPLORER.openItem(PROJECT_NAME + "/js/script.js");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(1);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(CHANGE_TEXT);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        //ssh key
        String currentWin = driver.getWindowHandle();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.SSH_KEY);
        IDE.LOADER.waitClosed();
        IDE.SSH.waitSshView();
        IDE.LOADER.waitClosed();
        IDE.SSH.clickOnGenerateAndUploadButton();
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        switchToNonCurrentWindow(currentWin);
//        IDE.GITHUB.waitAuthorizeBtn();
//        IDE.GITHUB.clickOnAuthorizeBtn();
        driver.switchTo().window(currentWin);
        IDE.SSH.waitSshView();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        // Commit
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.clickAddAllChangesCheckboxInCommitForm();
        IDE.GIT.typeCommitMessage(COMMIT_MESSAGE);
        IDE.GIT.clickCommitButton();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.LOADER.waitClosed();
        IDE.GIT.selectOnPushFormRemoteBranchInDropDown("master");
        IDE.GIT.clickPushBtn();
        IDE.OUTPUT.waitForSubTextPresent(succesPushMessage);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        driver.get("https://review.gerrithub.io/#/");
        IDE.GERRITHUB.clickGerritHubMy();
        IDE.GERRITHUB.clickGerritHubMyChanges();
        IDE.GERRITHUB.clickGerritHubCommit(COMMIT_MESSAGE);
        IDE.GERRITHUB.clickGerritHubChangeReply();
        IDE.GERRITHUB.typeGerritHubReviewText();
        IDE.GERRITHUB.clickGerritHubCodeReview();
        IDE.GERRITHUB.clickGerritHubVerifiedReview();
        IDE.GERRITHUB.clickGerritHubReviewPostButton();
        IDE.GERRITHUB.clickGerritHubMergeChangeButton();
        IDE.GERRITHUB.waitGerritHubHistoryRow1();
        IDE.GERRITHUB.waitGerritHubHistoryRow2();
        IDE.GERRITHUB.waitGerritHubHistoryRow3();
        Thread.sleep(10000);   //workaround
        IDE.GERRITHUB.clickGerritHubGithubChangeLink();
        driver.findElement(By.xpath("//pre[contains(., 'exoinvitemain/jsTestProject')]"));
        driver.get(url);
        IDE.OPEN.openProject(PROJECT_NAME);
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItem(PROJECT_NAME + "/js");
        IDE.EXPLORER.openItem(PROJECT_NAME + "/js");
        IDE.EXPLORER.waitForItem(PROJECT_NAME + "/js/script.js");
        IDE.EXPLORER.openItem(PROJECT_NAME + "/js/script.js");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(CHANGE_TEXT);

    }

    public String createFactoryURL() throws Exception{
        String factoryUrl = "";
        IDE.GERRITHUB.clickGerritHubProfileLink();
        IDE.GERRITHUB.clickGerritHubProfileSettings();
        IDE.GERRITHUB.clickGerritHubhttpPasswordLink();
        String gerritHubHttpUsername = IDE.GERRITHUB.getGerritHubhttpPasswordUsername();
        String gerritHubHttpPassword = IDE.GERRITHUB.getGerritHubhttpPasswordPassword();
        IDE.GERRITHUB.clickGerritHubProjectLink();
        IDE.GERRITHUB.clickGerritHubProjectListLink();
        IDE.GERRITHUB.typeGerritHubProjectSearch(PROJECT_NAME_GERRIT);
        IDE.GERRITHUB.clickGerritHubProjectCloneLink(PROJECT_NAME_GERRIT);
        String gerritHubProjectUrl = IDE.GERRITHUB.getGerritHubProjectCloneUrl();
        gerritHubProjectUrl = gerritHubProjectUrl.substring(18);
        factoryUrl = PROTOCOL + "://" + IDE_HOST + "/factory?v=1.2&" +
                "projectattributes.pname=jsTestProject&" +
                "projectattributes.ptype=JavaScript&" +
                "vcs=git&" +
                "vcsurl=https://" + gerritHubHttpUsername + ":" + gerritHubHttpPassword + "@" + gerritHubProjectUrl + "&" +
                "git.configremoteoriginfetch=+refs/changes/*:refs/remotes/origin/changes/*&" +
                "vcsbranch=master&" +
                "vcsinfo=true";
        return factoryUrl;
    }
}
