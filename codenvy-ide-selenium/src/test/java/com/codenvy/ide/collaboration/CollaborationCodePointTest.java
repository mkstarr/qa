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
import org.openqa.selenium.Keys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/** @author Musienko Maxim */
public class CollaborationCodePointTest extends CollaborationService {

    private static final String PROJECT = CollaborationCodePointTest.class.getSimpleName();

    private final String selectionText1 =
            "ModelAndView view = new ModelAndView ( \"hello_view\" ) ; view . addObject ( \"greeting\" , result ) ; return view ;";

    private final String selectionText2 =
            "ModelAndView view = new ModelAndView ( \"hello_view\" ) ; view . addObject ( \"greeting\" ,  result ) ;  return view ;";


    protected static Map<String, Link> project;

    CollaborationActivityInProjectExplorerTest instance = new CollaborationActivityInProjectExplorerTest();


    private String FIRST_USER = USER_NAME.split("@")[0];

    private String SECOND_USER = NOT_ROOT_USER_NAME.split("@")[0];


    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,
                                                            "src/test/resources/org/exoplatform/ide/operation/java/spring-with-big-sample" +
                                                            ".zip");
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
    public void codePoint() throws Exception {


        // step one run two browsers. And open file and chat under first collaborator
        initSecondBrowser();
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        expandMainItemsInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControl();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);

        IDE.TOOLBAR.runCommand(ToolbarCommands.Collaboration.COLLABORATION);
        IDE.CHAT.waitChatOpened();

        // step two open project and chat under second user
        IDE2.EXPLORER.waitOpened();
        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        expandItemsInSecondBrowser();
        IDE2.MENU.waitForMenuItemPresent(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        IDE2.TOOLBAR.runCommand(ToolbarCommands.Collaboration.COLLABORATION);
        IDE2.CHAT.waitChatOpened();

        // check main notifications in chats
        IDE2.CHAT.waitChatParticipants(FIRST_USER);
        IDE.CHAT.waitChatParticipants(SECOND_USER);
        IDE.CHAT.waitWhileMessageAppearInChat(SECOND_USER + " has joined the " + PROJECT + " project.");
        IDE2.CHAT.waitWhileMessageAppearInChat(FIRST_USER + " has joined the " + PROJECT + " project.");

        // step 3 send code fragment with code pointer and check notifications
        IDE.EDITOR.selectTab("GreetingController.java");
        IDE.GOTOLINE.goToLine(25);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN.toString());
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SEND_CODE_POINTER);
        IDE.CHAT.waitWhileMessageAppearInChat(getTime());
        IDE.CHAT.waitWhileMessageAppearInChat("me: \nGreetingController.java (25..28)");
        IDE2.CHAT.waitWhileMessageAppearInChat(FIRST_USER + ": \n" + "GreetingController.java" + " (25..28)");
        IDE2.CHAT.waitWhileMessageAppearInChat(getTime());
        IDE2.CHAT.waitLinkInChat("GreetingController.java (25..28)");
        IDE2.CHAT.clickOnLinkInChat("GreetingController.java (25..28)");
        IDE2.PROGRESS_BAR.waitProgressBarControlClose();

        // on this moment after first click on codepointer link selection wrong. But if will be fixed test should be pass.
        // IDE-2621
        // IDE2.CHAT.clickOnLinkInChat("GreetingController.java (25..28)");

        // step 4 close common file under second user click on code pointer link. Checking opening of the file and selected area.
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();
        // IDE2.PROGRESS_BAR.waitProgressBarControlClose();
        IDE2.JAVAEDITOR.waitStringActiveSelection(selectionText1);

        IDE2.JAVAEDITOR.waitWhileAreaAfterCodeIsActiveSelection(25);
        IDE2.JAVAEDITOR.waitWhileAreaAfterCodeIsActiveSelection(26);
        IDE2.JAVAEDITOR.waitWhileAreaAfterCodeIsActiveSelection(27);

        // step 5 close common file under second user click on code pointer link. Checking opening of the file and selected area.
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("SomeClass.java");
        IDE2.EDITOR.waitTabPresent("SomeClass.java");
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();
        IDE2.PROGRESS_BAR.waitProgressBarControl();
        IDE2.PROGRESS_BAR.waitProgressBarControlClose();

        IDE2.GOTOLINE.goToLine(17);
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN.toString());
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN.toString());
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN.toString());

        IDE2.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SEND_CODE_POINTER);
        IDE2.CHAT.waitWhileMessageAppearInChat(getTime());
        IDE2.CHAT.waitWhileMessageAppearInChat("me: \nSomeClass.java (17..20)");


        IDE.CHAT.waitLinkInChat("SomeClass.java (17..20)");
        IDE.CHAT.clickOnLinkInChat("SomeClass.java (17..20)");
        IDE2.EDITOR.waitTabPresent("SomeClass.java");
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();

        // TODO complete after fix issue IDE-2621. Check selections
        // IDE2.JAVAEDITOR.waitWhileAreaAfterCodeIsInactiveSelection(17);
        // [16:41:16] maxura: SomeClass.java (17..22)
        // [16:41:16]me: +": \n"+ SomeClass.java (17..22)


    }

    /** expand main nodes in package explorer for first user */
    protected void expandMainItemsInPackageExplorer() {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SomeClass.java");
    }


    /** expand main nodes in package explorer for second user */
    protected void expandItemsInSecondBrowser() {
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("SomeClass.java");
    }

    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return dateFormat.format(date).toString();

    }


    private void progressBarWaits() {
        IDE.PROGRESS_BAR.waitProgressBarControl();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }


}
