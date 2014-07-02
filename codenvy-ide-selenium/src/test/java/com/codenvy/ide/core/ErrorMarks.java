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
package com.codenvy.ide.core;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:mmusienko@exoplatform.com">Musienko Maksim</a>
 *
 */

public class ErrorMarks extends AbstractTestModule {
    /** @param ide */
    public ErrorMarks(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {

        String ERROR_MARKER_PREFIX = "//div[@class='CodeMirror-line-numbers']/div[text() = '%s' and @title]";

        String ERROR_MARKER_LABEL = "//div[@class='CodeMirror-line-numbers']//div[@title=\"%s\" and text()=%s]";

        String DECLARATION_FORM_ID = "ideAssistImportDeclarationForm";

        String DECLARATION_FORM =
                "//div[@id='ideAssistImportDeclarationForm']//div[@class='gwt-Label' and text()='%s']";

        String LINE_NUMBERS_CLASS = "//div[@class='CodeMirror-line-numbers']/div[text()='%s']";

        String FQN_PREFIX = "//div[@id='ideAssistImportDeclarationForm']//div[text()='%s']";

        String MARKER_EDITOR_LOCATOR = "//div[@panel-id='editor' and @view-id='editor-%s' ]//iframe";

        String NUM_ACTIVE_EDITOR = "//div[@class='gwt-TabLayoutPanelContent' and @is-active='true']";
    }

    @FindBy(xpath = Locators.NUM_ACTIVE_EDITOR)
    private WebElement numActiveEditor;

    /** wait appearance of the error in editor */
    public void waitLineNumberAppearance(final int numLine) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.LINE_NUMBERS_CLASS, numLine))));
    }

    /**
     * checks the appearance of the marker
     *
     * @param numString
     * @return
     */
    public void waitErrorMarkerShow(int numString) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.ERROR_MARKER_PREFIX, numString))));
    }

    /** wait open declaration form */
    public void waitDeclarationFormOpen(final String declaration) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.DECLARATION_FORM, declaration))));
    }

    /** wait marker is appear */
    public void waitErrorMarkerIsAppear(final int numMark) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.ERROR_MARKER_PREFIX, numMark))));
    }

    /** wait marker is disappear */
    public void waitErrorMarkerIsDisAppear(final int numMark) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                Locators.ERROR_MARKER_PREFIX, numMark))));
    }

    /**
     * wait setting changes in error marker after fix or other changes in code
     *
     * @param markLabel
     * @param numMarker
     */
    public void waitChangesInErrorMarker(final String markLabel, final int numMarker) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.ERROR_MARKER_LABEL, markLabel, numMarker))));
    }

    /** wait fqn declaration is appear */
    public void waitFqnDeclarationIsAppear(final String fqnName) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.FQN_PREFIX, fqnName))));
    }

    /**
     * click on declaration with specified name
     *
     * @param declaration
     */
    public void selectAndInsertDeclaration(String declaration) {
        WebElement decl = driver().findElement(By.xpath(String.format(Locators.DECLARATION_FORM, declaration)));
        new Actions(driver()).doubleClick(decl).build().perform();
    }

    /** click on declaration with specified name */
    public void selectAndInsertFqn(String fqn) {
        WebElement declFqn = driver().findElement(By.xpath(String.format(Locators.FQN_PREFIX, fqn)));
        new Actions(driver()).doubleClick(declFqn).build().perform();
    }

    /** wait closed declaration form */
    public void waitDeclarationFormClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .id(Locators


                                                                                                                  .DECLARATION_FORM_ID)));
    }

    /**
     * get title text from error marker
     *
     * @param numString
     * @return
     */
    public String getTextFromErorMarker(int numString) {
        WebElement mark = driver().findElement(By.xpath(String.format(Locators.ERROR_MARKER_PREFIX, numString)));
        return mark.getAttribute("title");
    }

    /**
     * get title text from error marker
     *
     * @param numString
     * @return
     */
    public void clickOnErorMarker(int numString) {
        WebElement mark = driver().findElement(By.xpath(String.format(Locators.ERROR_MARKER_PREFIX, numString)));
        mark.click();
    }

    /**
     * move cursor to down in declaration specified number of times
     *
     * @param numClick
     * @throws InterruptedException
     */
    public void downMoveCursorInDeclForm(int numClick) throws InterruptedException {
        WebElement mark = driver().findElement(By.id(Locators.DECLARATION_FORM_ID));

        for (int i = 0; i < numClick; i++) {
            mark.click();
            new Actions(driver()).sendKeys(Keys.ARROW_DOWN.toString()).build().perform();
            //sendKeys(Keys.ARROW_DOWN.toString());
            //delay for emulation of the user input
        }
    }

    /**
     * move up cursor in declaration specified number of times
     *
     * @param numClick
     * @throws InterruptedException
     */
    public void upMoveCursorInDeclForm(int numClick) throws InterruptedException {
        WebElement mark = driver().findElement(By.id(Locators.DECLARATION_FORM_ID));

        for (int i = 0; i < numClick; i++) {
            mark.sendKeys(Keys.ARROW_UP.toString());
            //delay for emulation of the user input
        }
    }

    /** select iframe with error markers panel */
    public void selectIframeWitErrorMarks() {
        WebElement frameWithMarkers =
                driver().findElement(
                        By.xpath(String.format(Locators.MARKER_EDITOR_LOCATOR, getNumberTabOfActiveEditor())));
        driver().switchTo().frame(frameWithMarkers);
    }

    /**
     * Getting of number current active tab of the code editor
     *
     * @return
     */
    public int getNumberTabOfActiveEditor() {
        return Integer.parseInt(numActiveEditor.getAttribute("view-id").replace("editor-", ""));
    }

}
