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
package com.codenvy.ide.debug;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.core.Build;

import static org.junit.Assert.assertTrue;

/** @author Musienko Maxim */

public class DebuggerServices extends BaseTest {

    /**
     * This method check all buttons on debuger panel after first run (breakpoints is not set)
     *
     * @throws Exception
     */
    public void isDebugerButtonsWithoutBreakPoints() throws Exception {
        IDE.DEBUGER.waitResumeBtnIsEnabled(false);
        IDE.DEBUGER.waitStepIntoBtnIsEnabled(false);
        IDE.DEBUGER.waitStepOverBtnIsEnabled(false);
        IDE.DEBUGER.waitStepReturnBtnIsEnabled(false);
        IDE.DEBUGER.waitDisconnectBtnIsEnabled(true);
        IDE.DEBUGER.waitRemoveAllBreakPointsBtnIsEnabled(false);
        IDE.DEBUGER.waitChangeValueBtnIsEnabled(false);
        IDE.DEBUGER.waitEvaluateExpressionIsEnabled(false);
    }

    /**
     * This method check all buttons on debuger panel if breakpoint set
     *
     * @throws Exception
     */
    public void isDebugerButtonsWithSetBreakPoints() throws Exception {
        IDE.DEBUGER.waitResumeBtnIsEnabled(true);
        IDE.DEBUGER.waitStepIntoBtnIsEnabled(true);
        IDE.DEBUGER.waitStepOverBtnIsEnabled(true);
        IDE.DEBUGER.waitStepReturnBtnIsEnabled(true);
        IDE.DEBUGER.waitDisconnectBtnIsEnabled(true);
        IDE.DEBUGER.waitRemoveAllBreakPointsBtnIsEnabled(true);
        IDE.DEBUGER.waitChangeValueBtnIsEnabled(false);
        IDE.DEBUGER.waitEvaluateExpressionIsEnabled(true);
    }

    /**
     * This method check all buttons on debuger panel if set breakзoint and select variable for chainging
     *
     * @throws Exception
     */
    public void isDebugerButtonsAllBtnActive() throws Exception {
        IDE.DEBUGER.waitResumeBtnIsEnabled(true);
        IDE.DEBUGER.waitStepIntoBtnIsEnabled(true);
        IDE.DEBUGER.waitStepOverBtnIsEnabled(true);
        IDE.DEBUGER.waitStepReturnBtnIsEnabled(true);
        IDE.DEBUGER.waitDisconnectBtnIsEnabled(true);
        IDE.DEBUGER.waitRemoveAllBreakPointsBtnIsEnabled(true);
        IDE.DEBUGER.waitChangeValueBtnIsEnabled(true);
        IDE.DEBUGER.waitEvaluateExpressionIsEnabled(true);
    }

    /**
     * switch on demo application window Important! This method means that will be opened only one window with demo
     * application
     *
     * @param currentWin
     */
    protected void switchDebugAppWin(String currentWin) {
        for (String handle : driver.getWindowHandles()) {
            if (!currentWin.equals(handle)) {
                driver.switchTo().window(handle);
                break;
            }

        }
    }

    protected void runDebugApp(String project) throws Exception {
        IDE.EXPLORER.waitOpened();
        // step 1 Open project
        IDE.OPEN.openProject(project);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        //step 2 run debug app and wait while build finish
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.DEBUG_APPLICATION);
        IDE.BUILD.waitOpened();
        String builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.startsWith(Build.Messages.BUILDING_PROJECT));
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        //step 3 check opening output panel and wait debug control
        IDE.DEBUGER.waitOpened();
    }
}
