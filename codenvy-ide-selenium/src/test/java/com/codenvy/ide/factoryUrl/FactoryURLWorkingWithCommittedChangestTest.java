/*
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
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/** @author Musienko Maxim */
public class FactoryURLWorkingWithCommittedChangestTest extends BaseTest {

    private final String afterCommitContentPom = "<packaging>jar</packaging>";

    private final String REPO_INIT_MESSAGE = "[INFO] Repository was successfully initialized.";

    private static final String PROJECT = "WorkingFactUrlWitCommUncommPrj";

    private final String changePom = "   <packaging>jar</packaging>";

    private String factoryUrl;

    private static Map<String, Link> project;

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException, TimeoutException, MessagingException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void checkUncommitedBehaviorFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.INITIALIZE_REPOSITORY);
        IDE.GIT.waitInitializeLocalRepositoryForm();
        IDE.GIT.typeInitializationRepositoryName(PROJECT);
        IDE.GIT.clickOkInitializeLocalRepository();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_INIT_MESSAGE);
        checkMainIconsAfterInit();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(6);
        IDE.EDITOR.typeTextIntoEditor(Keys.SHIFT.toString() + Keys.END.toString());
        IDE.EDITOR.typeTextIntoEditor(Keys.BACK_SPACE.toString());
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText("   <packaging>war</packaging>");
        IDE.EDITOR.typeTextIntoEditor(changePom);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitNoContentModificationMark("pom.xml");
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitCommitYourChangesForm();
        IDE.FACTORY_URL.typeCommitDescription("first_commit");
        IDE.FACTORY_URL.clickOkBtnCommitYourChangesForm();
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        factoryUrl = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.FACTORY_URL.clickOnFinishButtonInFactoryURLForm();
        IDE.LOGIN.logout();
        driver.get(factoryUrl);
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent(afterCommitContentPom);
    }

    private void checkMainIconsAfterInit() {
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src/main/java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithoutGitIcon("Maven Dependencies", Git.GitIcons.IN_REPOSITORY);
    }

}
