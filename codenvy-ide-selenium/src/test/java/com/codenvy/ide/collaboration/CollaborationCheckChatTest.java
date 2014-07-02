/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.ide.collaboration;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/** @authorRoman Iuvshin */
public class CollaborationCheckChatTest extends CollaborationService {
    private static final String PROJECT = CheckCodeCollaborationTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String FIRST_USER = USER_NAME.split("@")[0];

    private static final String SECOND_USER = NOT_ROOT_USER_NAME.split("@")[0];

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            killSecondBrowser();
        } catch (Exception e) {
        }
    }

    @Test
    public void chatingInProjectTest() throws Exception {
        initSecondBrowser();

        // opening project from first user
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);
        // opening project from second user
        IDE2.EXPLORER.waitOpened();
        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE2.MENU.waitForMenuItemPresent(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        // open chat as first user
        IDE.TOOLBAR.runCommand(ToolbarCommands.Collaboration.COLLABORATION);
        IDE.CHAT.waitChatOpened();
        // open chat as second user
        IDE2.TOOLBAR.runCommand(ToolbarCommands.Collaboration.COLLABORATION);
        IDE2.CHAT.waitChatOpened();

        // check that users visible to each other
        IDE.CHAT.waitChatParticipants(NOT_ROOT_USER_NAME.split("@")[0]);
        IDE2.CHAT.waitChatParticipants(USER_NAME.split("@")[0]);

        // checking information message that user joined to chat
        IDE.CHAT.waitWhileMessageAppearInChat(SECOND_USER + " has joined the " + PROJECT + " project.");
        IDE2.CHAT.waitWhileMessageAppearInChat(FIRST_USER + " has joined the " + PROJECT + " project.");

        // type message from second user
        IDE2.CHAT.typeAndSendMessage("Hello, can u help me?");

        // check that message appear for both users
        IDE2.CHAT.waitWhileMessageAppearInChat("me: \n" +
                                               "Hello, can u help me?");
        IDE.CHAT.waitWhileMessageAppearInChat(SECOND_USER + ": \nHello, can u help me?");

        // type message from first user
        IDE.CHAT.typeAndSendMessage("Hi! How can I help you?");

        // check that message appear for both users
        IDE2.CHAT.waitWhileMessageAppearInChat(FIRST_USER + ": \nHi! How can I help you?");
        IDE.CHAT.waitWhileMessageAppearInChat("me: \nHi! How can I help you?");
    }

    @Test
    public void chatingWithOpenedSameFile() throws Exception {
        // open file as second user
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();
        IDE2.PROGRESS_BAR.waitProgressBarControlClose();

        // first user, check notification that second user has opened a file
        IDE.CHAT.waitWhileMessageAppearInChat(SECOND_USER + " opened GreetingController.java file.");

        // first user open that file by clicking on file name in chat
        IDE.CHAT.clickOnNotificationLinkToOpenFile("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        // second user check notification that first user has opened file
        IDE2.CHAT.waitWhileMessageAppearInChat(FIRST_USER + " opened GreetingController.java file.");

        // second user send new chat message
        IDE2.CHAT.typeAndSendMessage("Hi I am opened that file");

        // check that message appear for both users
        IDE2.CHAT.waitWhileMessageAppearInChat("me: \nHi I am opened that file");
        IDE.CHAT.waitWhileMessageAppearInChat(SECOND_USER + ": \nHi I am opened that file");

        // send message from first user
        IDE.CHAT.typeAndSendMessage("aha! I see!!@#$%^&*()_+|");

        // check that message appear for both users
        IDE.CHAT.waitWhileMessageAppearInChat("me: \naha! I see!!@#$%^&*()_+|");
        IDE2.CHAT.waitWhileMessageAppearInChat(FIRST_USER + ": \naha! I see!!@#$%^&*()_+|");

        // one more message from first user
        IDE.CHAT.typeAndSendMessage("test test");
        IDE.CHAT.waitWhileMessageAppearInChat("... \ntest test");
        IDE2.CHAT.waitWhileMessageAppearInChat("... \ntest test");

        IDE.CHAT.typeAndSendMessage("test2 2222");
        IDE.CHAT.waitWhileMessageAppearInChat("... \ntest2 2222");
        IDE2.CHAT.waitWhileMessageAppearInChat("... \ntest2 2222");

        IDE2.CHAT.typeAndSendMessage("test33333 33");
        IDE2.CHAT.waitWhileMessageAppearInChat("me: \ntest33333 33");
        IDE.CHAT.waitWhileMessageAppearInChat(SECOND_USER + ": \ntest33333 33");

        IDE2.CHAT.typeAndSendMessage("test555555 5 5 5");
        IDE2.CHAT.waitWhileMessageAppearInChat("... \ntest555555 5 5 5");
        IDE.CHAT.waitWhileMessageAppearInChat("... \ntest555555 5 5 5");

        // check close file notification
        IDE.EDITOR.closeFile("GreetingController.java");
        IDE2.CHAT.waitWhileMessageAppearInChat(FIRST_USER + " closed the GreetingController.java file.");
    }
}
