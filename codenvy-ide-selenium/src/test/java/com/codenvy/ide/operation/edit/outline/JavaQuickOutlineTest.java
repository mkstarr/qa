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
package com.codenvy.ide.operation.edit.outline;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.operation.autocompletion.CodeAssistantBaseTest;
import com.codenvy.ide.operation.autocompletion.JavaCodeAssistantTest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author Evgen Vidolob
 *
 */
public class JavaQuickOutlineTest extends CodeAssistantBaseTest {
    private static final String FILE_NAME = "GreetingController.java";

    private static final String QUICK_OUTLINE_PANEL = "ideQuickOutlinePopup";

    @BeforeClass
    public static void beforeTest() throws Exception {
        try {
            createProject(JavaCodeAssistantTest.class.getSimpleName(),
                          "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/JavaTestProject.zip");
        } catch (Exception e) {
            fail("Can't create test folder");
        }


    }

    @Test
    public void quickOutline() throws Exception {

        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(projectName);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(projectName);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);
        IDE.CODE_ASSISTANT_JAVA.waitForJavaToolingInitialized(FILE_NAME);

        IDE.GOTOLINE.goToLine(24);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.chord(Keys.CONTROL, "o"));
        waitForQuickOutlineOpened();
        assertElementPresent("helloworld");
        assertElementPresent("GreetingController");
        assertElementPresent("handleRequest(HttpServletRequest, HttpServletResponse)");

        selectItem("GreetingController");
        waitForQuickOutlineClosed();
        IDE.STATUSBAR.waitCursorPositionAt("12 : 1");
    }

    /**
     *
     */
    private void waitForQuickOutlineClosed() {
        (new WebDriverWait(IDE.driver(), 30)).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver d) {
                try {
                    driver.findElement(By.id(QUICK_OUTLINE_PANEL));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }

            }
        });
    }

    /** @param name */
    private void selectItem(String name) {
        String expression = "//div[@id='ideQuickOutlineTree']//span[text()='" + name + "']";
        WebElement element = driver.findElement(By.xpath(expression));
        element.click();
        element = driver.findElement(By.xpath(expression));
        // element.click();
        // element.click();
        new Actions(driver).doubleClick(element).perform();
    }

    /** @param name */
    private void assertElementPresent(String name) {
        assertNotNull(
                IDE.driver().findElement(By.xpath("//div[@id='ideQuickOutlineTree']//span[text()='" + name + "']")));
    }

    /**
     *
     */
    private void waitForQuickOutlineOpened() {
        (new WebDriverWait(IDE.driver(), 30)).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id(QUICK_OUTLINE_PANEL)) != null;
            }
        });
    }

}
