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
package com.codenvy.ide.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/** @author Musienko Maxim */
public class Shell extends AbstractTestModule {
    /** @param ide */
    public Shell(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {

        String SHELL_ID = "shell-link";

        String SHELL_CONTENT_ID = "shellContent";

        String TERM_CONTENT_ID = "termContent";
    }

    private String ideWinHand;

    @FindBy(id = Locators.SHELL_ID)
    private WebElement shellLink;

    @FindBy(id = Locators.SHELL_CONTENT_ID)
    public WebElement shell;

    @FindBy(id = Locators.TERM_CONTENT_ID)
    public WebElement term;

    public void setIDEWindowHandle(String IDEhandl) {
        ideWinHand = IDEhandl;
    }

    /**
     * click on shell menu in IDE
     *
     * @throws Exception
     */
    public void callShellFromIde() throws InterruptedException {
        waitShellLinkInIDE();
        IDE().LOADER.waitClosed();
        shellLink.click();
        switchOnShellWindow();
        waitShellIsOpened();
    }


    /**
     * click on shell menu in IDE under read only mode
     *
     * @throws Exception
     */
    public void callShellFromIdeInReadOnleMode() throws InterruptedException {
        waitShellLinkInIDE();
        IDE().LOADER.waitClosed();
        shellLink.click();
        switchOnShellWindow();
    }

    /** wait welcome describe in shell */
    private void waitShellIsOpened() {

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return getContent().contains("Welcome to Codenvy Shell");
                } catch (Exception e) {
                    return false;
                }
            }
        });

    }


    /** wait welcome describe in shell for readonly mode */
    private void waitShellIsOpenedInReadOnlyMode() {

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return getContent().contains("User not authorized to call this method.");
                } catch (Exception e) {
                    return false;
                }
            }
        });

    }

    /** wait text is present in shell */
    public void waitContainsTextInShell(final String txt) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    System.out.println("**************************:" + getContent());
                    return getContent().contains(txt);
                } catch (Exception e) {
                    return false;
                }
            }
        });

    }


    /** wait text is equals in shell */
    public void waitEqualsInShell(final String txt) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return getContent().equals(txt);
                } catch (Exception e) {
                    return false;
                }
            }
        });

    }


    /** wait text is present in shell after execute a command */
    public void waitContainsTextAfterCommandInShell(final String txt) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return getTerm().contains(txt);
                } catch (Exception e) {
                    return false;
                }
            }
        });

    }

    /** wait text is not present in shell */
    public void waitNotContainsTextInShell(final String txt) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return !getContent().contains(txt);
                } catch (Exception e) {
                    return false;
                }
            }
        });

    }

    /**
     * wait while opened one more window (shell opened in a separate window)
     *
     * @throws Exception
     */
    private void waitOpenNewShellWindow() {
        IDE().DEBUGER.waitOpenedSomeWin();
    }

    /**
     * wait while shell link is appear in IDE
     *
     * @throws Exception
     */
    public void waitShellLinkInIDE() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(shellLink));
    }

    /** switch on ShellWindow */
    public void switchOnShellWindow() {

        Set<String> driverWindows = driver().getWindowHandles();
        for (String wins : driverWindows) {
            if (!wins.equals(ideWinHand)) {
                driver().switchTo().window(wins);

            }
        }
    }

    /** return to IDE window */
    public void switchToIDE() {
        driver().switchTo().window(ideWinHand);
    }

    /**
     * get term text from shell
     *
     * @return
     */
    public String getTerm() {
        return term.getText();
    }

    /**
     * get text from Shell
     *
     * @return
     */
    public String getContent() {
        return term.getText() + shell.getText();
    }

    /**
     * type command to shell and press enter
     *
     * @param command
     */
    public void typeAndExecuteCommand(String command) {
        term.sendKeys(command);
        term.sendKeys("\n");
    }

}
