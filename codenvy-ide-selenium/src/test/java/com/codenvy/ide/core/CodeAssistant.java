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

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Oksana Vereshchaka
 *
 */
public class CodeAssistant extends AbstractTestModule {

    /** @param ide */
    public CodeAssistant(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    /** XPath autocompletion panel locator. */
    private static final String PANEL_ID = "exo-ide-autocomplete-panel";

    private static final String PANEL = "//table[@id='exo-ide-autocomplete-panel']";

    private static final String ELEMENT_LOCATOR = PANEL + "//div[text()='%s']";

    private static final String SUBSTITUTE_ELEMENT = PANEL + "//div[text()='%s']";

    /** Xpath autocompletion input locator. */
    private static final String INPUT = "exo-ide-autocomplete-edit";

    private static final String JAVADOC_DIV = "exo-ide-autocomplete-doc-panel";

    private static final String IMPORT_PANEL_ID = "ideAssistImportDeclarationForm";

    @FindBy(xpath = PANEL)
    private WebElement panel;

    @FindBy(id = INPUT)
    private WebElement input;

    @FindBy(id = JAVADOC_DIV)
    private WebElement doc;

    @FindBy(id = IMPORT_PANEL_ID)
    private WebElement importPanel;

    /**
     * Type text to input field of autocompletion form.
     *
     * @param text
     *         - text to type
     * @throws Exception
     */
    public void typeToInput(String text) throws Exception {
        IDE().INPUT.typeToElement(input, text);
    }

    /**
     * Type to input with cleaning previous value.
     *
     * @param text
     *         text to type
     * @param clearInput
     *         if <code>true</code> - clear input befor type
     * @throws Exception
     */
    public void typeToInput(String text, boolean clearInput) throws Exception {
        if (clearInput) {
            clearInput();
        }
        typeToInput(text);
    }

    public boolean isElementPresent(String elementTitle) {
        try {
            WebElement element = driver().findElement(By.xpath(String.format(ELEMENT_LOCATOR, elementTitle)));
            return element != null && element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * wait for element in codeassistant
     *
     * @param value
     */
    public void waitForElementInCodeAssistant(final String value) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                ELEMENT_LOCATOR, value))));
    }

    public String[] getFormProposalsText() {
        List<WebElement> elements =
                driver().findElements(By.cssSelector("table#exo-ide-autocomplete-panel div>div>div table"));
        String[] texts = new String[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            WebElement el = elements.get(i);
            String elementText = el.getText();
            if (elementText.contains("\n")) {
                texts[i] = elementText.split("\n")[1];
            } else {
                texts[i] = elementText;
            }
        }

        return texts;
    }

    /** @param elementTitle */
    public void checkElementNotPresent(String elementTitle) {
        (new WebDriverWait(driver(), 30)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(PANEL
                                                                                                         +
                                                                                                         "//div[text" +
                                                                                                         "()='" +
                                                                                                         elementTitle +
                                                                                                         "']")));
    }

    /**
     * Move cursor down
     *
     * @param row
     *         Number of rows to move down
     * @throws InterruptedException
     */
    public void moveCursorDown(int row) throws InterruptedException {
        for (int i = 0; i < row; i++) {
            input.sendKeys(Keys.DOWN.toString());
        }
    }

    /** Clear input field of Autocompletion form */
    public void clearInput() {
        input.clear();
    }

    /** Close codeasstant by press Escape Button */
    public void closeForm() {
        selectProposalPanel();
        input.sendKeys(Keys.ESCAPE);
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

    /** Click to the proposal panel */
    public void selectProposalPanel() {
        driver().findElement(By.id(PANEL_ID)).click();
    }

    /**
     * Press Enter key to close form and paste selected item in to the editor
     *
     * @throws InterruptedException
     */
    public void insertSelectedItem() throws InterruptedException {
        // RETURN key is used instead of ENTER because
        // of issue http://code.google.com/p/selenium/issues/detail?id=2180
        input.sendKeys(Keys.RETURN);
    }

    /**
     * Open Autocompletion Form
     *
     * @throws Exception
     */
    public void openForm() throws Exception {
        IDE().EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.id(PANEL_ID)));
    }

    public void waitForDocPanelOpened() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return doc != null && doc.isDisplayed();
            }
        });

    }

    public String getDocPanelText() {
        return doc.getText();
    }

    public void checkDocFormPresent() {
        assertTrue(driver().findElement(By.id(JAVADOC_DIV)).isDisplayed());
    }

    public void clickOnLineNumer(int num) {
        driver().switchTo().frame(driver().findElement(By.cssSelector("iframe.gwt-Frame")));
        driver().findElement(By.xpath("//div[@class='CodeMirror-line-numbers']/div[contains(text(), '" + num + "')]"))
                .click();
        IDE().selectMainFrame();
    }

    public void waitForImportAssistForOpened() {
        (new WebDriverWait(driver(), 30)).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver d) {
                return importPanel != null && importPanel.isDisplayed();
            }
        });
    }

    public void waitForInput() {
        (new WebDriverWait(driver(), 30)).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver d) {
                return input != null && input.isDisplayed();
            }
        });
    }

    public void setFocusTInput() {
        if (input != null && input.isDisplayed()) {
            input.click();
        }
    }

    /**
     * @param name
     * @throws InterruptedException
     */
    public void selectImportProposal(String name) throws InterruptedException {
        WebElement im = driver().findElement(By.xpath("//div[@class='gwt-Label' and contains(text(),'" + name + "')]"));
        Actions a = new Actions(driver());
        im.click();
        Action sel = a.doubleClick(im).build();
        sel.perform();
    }

    /**
     * select item in codeaasysten by declaration name
     *
     * @param name
     * @throws InterruptedException
     */
    public void selectByImportDeclaration(String name) throws InterruptedException {
        WebElement elem =
                driver().findElement(
                        By.xpath("//div[@id='ideAssistImportDeclarationForm']//div[contains(text(), '" + name + "')]"));
        new Actions(driver()).doubleClick(elem).perform();
    }

    /**
     * @param name
     * @throws InterruptedException
     */
    public void selectProposal(String name, String afterDash) throws InterruptedException {
        WebElement im =
                driver().findElement(
                        By.xpath("//div[@class='gwt-HTML' and contains(text(),'" + name + "')]/span[contains(text(),'" +
                                 afterDash
                                 + "')]"));
        Actions a = new Actions(driver());
        im.click();
        Action sel = a.doubleClick(im).build();
        sel.perform();
    }

    /**
     *
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
}
