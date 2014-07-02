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

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Formatter extends AbstractTestModule {
    /** @param ide */
    public Formatter(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String FORMATTER_VIEW = "div[view-id='eXoIdeJavaCodeFormatterProfileView']";

        String OK_BTN = "//div[@view-id='eXoIdeJavaCodeFormatterProfileView']//td[text()='Ok']";

        String TEXT_CONTAINER = "//div[@view-id='eXoIdeJavaCodeFormatterProfileView']//div[@tabindex='-1']/div";

        String LAST_TEXT_ELEMENT_IN_DEFAULT_TEXT =
                "//div[@view-id='eXoIdeJavaCodeFormatterProfileView']//div[@tabindex='-1']/div/div[20]";

        String COMBOBOX = "//div[@view-id='eXoIdeJavaCodeFormatterProfileView']//select";

        String ECLIPSE_FORMATTER_OPTION = "//option[@value='Eclipse [built-in]']";

        String CODENVY_FORMATTER_OPTION = "//option[@value='Codenvy [built-in]']";

    }

    @FindBy(css = Locators.FORMATTER_VIEW)
    WebElement view;

    @FindBy(xpath = Locators.TEXT_CONTAINER)
    WebElement textArea;

    @FindBy(xpath = Locators.LAST_TEXT_ELEMENT_IN_DEFAULT_TEXT)
    WebElement textReparse;

    @FindBy(xpath = Locators.COMBOBOX)
    WebElement comboBox;

    @FindBy(xpath = Locators.ECLIPSE_FORMATTER_OPTION)
    WebElement eclipseFormat;

    @FindBy(xpath = Locators.CODENVY_FORMATTER_OPTION)
    WebElement codenvyFormat;

    @FindBy(xpath = Locators.OK_BTN)
    WebElement okBtn;

    /**
     * Wait Formatter opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                return view != null && view.isDisplayed();

            }
        });
    }

    /**
     * Wait Eclipse Formatter is selected.
     *
     * @throws Exception
     */
    public void waitEclipseFormatterIsSelect() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                return eclipseFormat.isSelected();
            }
        });
    }

    /**
     * Wait Exo Formatter is selected.
     *
     * @throws Exception
     */
    public void waitCodenvyFormatterIsSelect() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                return codenvyFormat.isSelected();
            }
        });
    }

    /**
     * Wait while all sample text in formatter appearance
     *
     * @throws Exception
     */
    public void waitRedrawSampleText() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                return textReparse != null && textReparse.isDisplayed();

            }
        });
    }

    /** get text from formatter */
    public String getFormatterText() {
        return textArea.getText();
    }

    /**
     * Wait Formatter opened.
     *
     * @throws Exception
     */
    public void waitLastTextElement() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                return view != null && view.isDisplayed();

            }
        });
    }

    /**
     * expand combobox of the formatter
     *
     * @throws Exception
     */
    public void clickOnCombobox() throws Exception {
        comboBox.click();
    }

    /**
     * click on ok button on formmater form
     *
     * @throws Exception
     */
    public void clickOkBtn() throws Exception {
        okBtn.click();
    }

    /**
     * select Exo Format
     *
     * @throws Exception
     */
    public void selectCodenvyFormatter() throws Exception {
        //In this method used options select with key events, because select with mouse actions,
        // works incorrect in Chrome browser
        new Actions(driver()).moveToElement(comboBox).click().build().perform();
        new Actions(driver()).sendKeys(comboBox, Keys.ARROW_DOWN.toString()).build().perform();
        waitCodenvyFormatterIsSelect();
        clickOkBtn();
        IDE().LOADER.waitClosed();
    }

    /**
     * select Eclipse Format
     *
     * @throws Exception
     */
    public void selectEclipseFormatter() throws Exception {
        //In this method used options select with key events, because select with mouse actions,
        // works incorrect in Chrome browser
        new Actions(driver()).moveToElement(comboBox).click().build().perform();
        new Actions(driver()).sendKeys(comboBox, Keys.ARROW_UP.toString()).build().perform();
        waitEclipseFormatterIsSelect();
        clickOkBtn();
        IDE().LOADER.waitClosed();
    }

}