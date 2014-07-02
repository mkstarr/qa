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
package com.codenvy.ide.git;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/** @author Roman Iuvshin */
public class CloneRemotePublicPersonalRepositoryWithDifferentProjectTypesTest extends GitServices {

    private static final String PROJECT = "phpTestRepo";

    private static final String PROJECT_2 = "rubyTestProject";

    private static final String PROJECT_3 = "jsTestProject";

    private static final String PROJECT_4 = "pythonTestProject";

    private static final String PROJECT_5 = "nodejsTestPrj";

    private static final String ALL_PROJECTS[] = {PROJECT, PROJECT_2, PROJECT_3, PROJECT_4, PROJECT_5};

    private static final String REPO_URL_5 = "git@github.com:exoinvitemain/nodejsTestPrj.git";

    private static final String REPO_URL = "git@github.com:exoinvitemain/phpTestRepo.git";

    private static final String REPO_URL_2 = "git@github.com:exoinvitemain/rubyTestProject.git";

    private static final String REPO_URL_3 = "git://github.com/exoinvitemain/jsTestProject.git";

    private static final String REPO_URL_4 = "git@github.com:exoinvitemain/pythonTestProject.git";


    @AfterClass
    public static void tearDown() throws Exception {
        for (String allProject : ALL_PROJECTS) {
            if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + allProject).getStatusCode() == 200) {
                VirtualFileSystemUtils.delete(allProject);
            }

        }


    }

    @After
    public void checkAndClosePopup() throws Exception {
        if (IDE.WARNING_DIALOG.isDisplayed()) {
            IDE.WARNING_DIALOG.clickOk();
            IDE.WARNING_DIALOG.waitClosed();
        }

    }

    @Test
    public void clonePhpProjectTest() throws Exception {

        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL + " was successfully cloned.");
        // IDE.GIT.waitSelectProjectTypeForm();
        // IDE.GIT.selectProjectType("PHP");
        //IDE.GIT.clickOkButtonOnSelectProjectTypeForm();
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project type updated.");
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("index.php", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("README.md", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("bin", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("tmp", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("webroot", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("config", Git.GitIcons.IN_REPOSITORY);

        checkMenuStateWhenRepositoryInited();

        // check project type
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesOpened();
        System.out.println("***********************************************:" + IDE.PROPERTIES.getTextFromRowProjectProperties(0));
        assertTrue(IDE.PROPERTIES.getAllTextFromProjectProperties().contains("PHP"));
        IDE.PROPERTIES.clickCancelButtonOnPropertiesForm();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);

        // clean console
        IDE.OUTPUT.clickClearButton();
    }


    @Test
    public void cloneJavaScriptProjectTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL_3);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL_3 + " was successfully cloned.");
        IDE.GIT.waitSelectProjectTypeForm();
        IDE.GIT.selectProjectType("JavaScript");
        IDE.GIT.clickOkButtonOnSelectProjectTypeForm();
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project type updated.");
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.waitElementWithGitIcon(PROJECT_3, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("images", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("js", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("styles", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("README.md", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("index.html", Git.GitIcons.IN_REPOSITORY);

        checkMenuStateWhenRepositoryInited();

        // check project type
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesOpened();
        assertTrue(IDE.PROPERTIES.getAllTextFromProjectProperties().contains("JavaScript"));
        IDE.PROPERTIES.clickCancelButtonOnPropertiesForm();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);

        // clean console
        IDE.OUTPUT.clickClearButton();
    }

    @Test
    public void clonePythonProjectTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL_4);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL_4 + " was successfully cloned.");
        //  IDE.GIT.waitSelectProjectTypeForm();
        // IDE.GIT.selectProjectType("Python");
        //IDE.GIT.clickOkButtonOnSelectProjectTypeForm();
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project type updated.");
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.waitElementWithGitIcon(PROJECT_4, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("requirements.txt", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("wsgi.py", Git.GitIcons.IN_REPOSITORY);

        checkMenuStateWhenRepositoryInited();

        // check project type
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesOpened();
        assertTrue(IDE.PROPERTIES.getAllTextFromProjectProperties().contains("Python"));
        IDE.PROPERTIES.clickCancelButtonOnPropertiesForm();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);

        // clean console
        IDE.OUTPUT.clickClearButton();
    }


    @Test
    public void cloneNodeJsProjectTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL_5);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL_5 + " was successfully cloned.");
        //  IDE.GIT.waitSelectProjectTypeForm();
        // IDE.GIT.selectProjectType("Python");
        //IDE.GIT.clickOkButtonOnSelectProjectTypeForm();
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project type updated.");
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.waitElementWithGitIcon(PROJECT_5, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("README.md", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("app.js", Git.GitIcons.IN_REPOSITORY);

        checkMenuStateWhenRepositoryInited();

        // check project type
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesOpened();
        assertTrue(IDE.PROPERTIES.getAllTextFromProjectProperties().contains("nodejs"));
        IDE.PROPERTIES.clickCancelButtonOnPropertiesForm();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        // clean console
        IDE.OUTPUT.clickClearButton();
    }


    @Test
    public void cloneRubyProjectTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL_2);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL_2 + " was successfully cloned.");
        //  IDE.GIT.waitSelectProjectTypeForm();
        // IDE.GIT.selectProjectType("Rails");
        // IDE.GIT.clickOkButtonOnSelectProjectTypeForm();
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project type updated.");
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.waitElementWithGitIcon(PROJECT_2, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("app", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("db", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("lib", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("log", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("script", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("vendor", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("Gemfile.lock", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("README.rdoc", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("Rakefile", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("config.ru", Git.GitIcons.IN_REPOSITORY);

        checkMenuStateWhenRepositoryInited();

        // check project type
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesOpened();
        assertTrue(IDE.PROPERTIES.getAllTextFromProjectProperties().contains("Rails"));
        IDE.PROPERTIES.clickCancelButtonOnPropertiesForm();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);

        // clean console
        IDE.OUTPUT.clickClearButton();
    }

    /** @param currentWin */
    private void switchToGithubLoginWindow(String currentWin) {
        for (String handle : driver.getWindowHandles()) {
            if (currentWin.equals(handle)) {

            } else {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
}
