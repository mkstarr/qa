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

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.codenvy.ide.core.WarningDialog.Locators;

import static org.junit.Assert.assertTrue;

/**
 * @author Oksana Vereshchaka
 *
 */
public class CodeAssistantJava extends AbstractTestModule {

    /** @param ide */
    public CodeAssistantJava(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    /** XPath autocompletion panel locator. */
    private static final String PANEL_ID              = "codenvy-ide-autocomplete-panel";

    private static final String PANEL                 = "//*[@id='codenvy-ide-autocomplete-panel']";

    private static final String FIRST_ITEM            = PANEL + "/table/tr";

    private static final String ELEMENT_LOCATOR       = PANEL + "//tr[contains(.,'%s')]";

    private static final String SUBSTITUTE_ELEMENT    = PANEL + "//div[text()='%s']";

    private static final String AUTOCOMPLETE_PROPOSAL = FIRST_ITEM + "[contains(.,'%s')]";

    /** Xpath autocompletion input locator. */

    private static final String JAVADOC_DIV           = "exo-ide-autocomplete-doc-panel";

    private static final String IMPORT_PANEL_ID       = "ideAssistImportDeclarationForm";

    @FindBy(xpath = PANEL)
    private WebElement          panel;

    @FindBy(id = JAVADOC_DIV)
    private WebElement          doc;

    @FindBy(id = IMPORT_PANEL_ID)
    private WebElement          importPanel;

    /**
     * check is proposal text presnt in panel
     * 
     * @param elementTitle
     * @return
     */
    public boolean isElementPresent(String elementTitle) {
        try {
            WebElement element = driver().findElement(By.xpath(String.format(ELEMENT_LOCATOR, elementTitle)));
            return element != null && element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * get all text from codeassistant panel
     * 
     * @return
     */
    public String getAllFormProposalsText() {
        WebElement elements = driver().findElement(By.cssSelector("div#codenvy-ide-autocomplete-panel table"));
        return elements.getText();
    }

    /**
     * Move cursor down
     * 
     * @param row Number of rows to move down
     * @throws InterruptedException
     */
    public void moveCursorDown(int row) throws InterruptedException {
        selectProposalPanel();
        for (int i = 0; i < row; i++) {
            new Actions(driver()).sendKeys(Keys.DOWN.toString()).build().perform();
            // input.sendKeys(Keys.DOWN.toString());
        }
    }

    /**
     * wait while autocomplete panel opens
     */
    public void waitCodenvyAutocompletePanel() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOfElementLocated(By.id(PANEL_ID)));
    }

    
    

    /**
     * wait while autocomplete panel is closed
     */
    public void waitCodenvyAutocompletePanelIsClosed() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id(PANEL_ID)));
    }

    /**
     * wait while autocomplete panel opens
     */
    public void waitCodenvyImportsPanelIsClosed() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id(JAVADOC_DIV)));
    }
    
    /**
     * Move cursor down
     * 
     * @param row Number of rows to move down
     * @throws InterruptedException
     */
    public void moveCursorUp(int row) throws InterruptedException {
        for (int i = 0; i < row; i++) {
            selectProposalPanel();
            new Actions(driver()).sendKeys(Keys.UP.toString()).build().perform();
            // input.sendKeys(Keys.DOWN.toString());
        }
    }

    /** Close codeassitant by press Escape Button */
    public void closeForm() {
        selectProposalPanel();
        new Actions(driver()).sendKeys(Keys.ESCAPE).build().perform();
        (new WebDriverWait(driver(), 30)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.id(PANEL_ID));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /** Click to the proposal panel on first item */
    public void selectProposalPanel() {
        driver().findElement(By.xpath(FIRST_ITEM)).click();
    }

    /**
     * Press Enter key to close form and paste selected item in to the editor
     * 
     * @throws InterruptedException
     */
    public void insertSelectedItem() throws InterruptedException {
        // RETURN key is used instead of ENTER because
        // of issue http://code.google.com/p/selenium/issues/detail?id=2180
        new Actions(driver()).sendKeys(Keys.RETURN.toString()).build().perform();
    }

    /**
     * double click on selected item in java autocomplete form
     * 
     * @param parameter
     * @throws InterruptedException
     */
    public void doudleClickSelectedItem(String parameter) throws InterruptedException {
        // RETURN key is used instead of ENTER because
        // of issue http://code.google.com/p/selenium/issues/detail?id=2180
        WebElement prop = driver().findElement(By.xpath(String.format(AUTOCOMPLETE_PROPOSAL, parameter)));
        new Actions(driver()).doubleClick(prop).build().perform();
        // input.sendKeys(Keys.RETURN);
    }

    /**
     * Open autocompletion Form and wait appereance
     * 
     * @throws Exception
     */
    public void openForm() throws Exception {
        Thread.sleep(1000);
        IDE().JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        waitCodenvyAutocompletePanel();
    }

    /**
     * wait while javadoc window will be opened
     */
    public void waitForDocPanelOpened() {
        (new WebDriverWait(driver(), 30)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return doc != null && doc.isDisplayed();
            }
        });

    }

    /**
     * wait import with specific name
     * 
     * @param nameImport
     */
    public void waitFromImportContent(final String nameImport) {
        (new WebDriverWait(driver(), 30)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                WebElement elem = driver().findElement(By.xpath(String.format(ELEMENT_LOCATOR, nameImport)));
                return elem.isDisplayed();
            }
        });

    }

    /**
     * get all text from javadoc (after press ctrl +q in java editor)
     * 
     * @return
     */
    public String getDocPanelText() {
        return doc.getText();
    }

    /**
     * return true if javadoc window is opened
     */
    public void checkDocFormPresent() {

        assertTrue(driver().findElement(By.xpath(JAVADOC_DIV)).isDisplayed());
    }

    @Deprecated
    public void clickOnLineNumer(int num) {
        driver().findElement(By.xpath("//div[@class='CodeMirror-line-numbers']/div[contains(text(), '" + num + "')]"))
                .click();
    }

    /**
     * wait while imported form opened
     */
    public void waitForImportAssistForOpened() {
        (new WebDriverWait(driver(), 30)).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver d) {
                return importPanel != null && importPanel.isDisplayed();
            }
        });
    }

    /**
     * click on imported element
     * 
     * @param name
     * @throws InterruptedException
     */
    public void selectImportProposal(String name) throws InterruptedException {
        driver().findElement(By.xpath(String.format(ELEMENT_LOCATOR, name))).click();

    }

    /**
     * @param name
     * @throws InterruptedException
     */
    public void selectProposal(String name, String afterDash) throws InterruptedException {
        WebElement im =
                        driver().findElement(
                                             By.xpath("//div[@class='gwt-HTML' and contains(text(),'" + name + "')]/span[contains(text(),'"
                                                      +
                                                      afterDash
                                                      + "')]"));
        Actions a = new Actions(driver());
        im.click();
        Action sel = a.doubleClick(im).build();
        sel.perform();
    }


    /**
     * select item with specified name
     * 
     * @param name
     * @throws InterruptedException
     */
    public void selectProposal(String name) throws InterruptedException {
        WebElement item = driver().findElement(By.xpath(String.format(ELEMENT_LOCATOR, name)));
        new Actions(driver()).moveToElement(item).build().perform();
        item.click();
    }


    /**
     * wait while javatooling panel disappear
     */
    public void waitForJavaToolingInitialized(String fileName) {
        final String title = "Initialize Java tooling for " + fileName;
        new WebDriverWait(driver(), 1000).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                WebElement element =
                                     driver.findElement(
                                           By.xpath("//div[@control-id='__request-notification-control' and @title='" + title
                                                    + "']"));
                return !element.isDisplayed();
            }
        });
    }

    /**
     * wait specified element in codeassistant list
     * 
     * @param value
     */
    public void waitForElementInCodeAssistant(final String value) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                                                                                                                   ELEMENT_LOCATOR, value))));
    }

    /** wait while element disappear or not present in codeassistant list @param elementTitle */
    public void checkElementNotPresent(String elementTitle) {
        (new WebDriverWait(driver(), 30)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(PANEL
                                                                                                         +
                                                                                                         "//div[text()='" +
                                                                                                         elementTitle +
                                                                                                         "']")));
    }
}
