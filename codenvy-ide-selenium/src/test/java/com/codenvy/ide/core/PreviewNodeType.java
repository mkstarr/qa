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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Anna Shumilova
 *
 */
public class PreviewNodeType extends AbstractTestModule {
    /** @param ide */
    public PreviewNodeType(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final class Locators {
        static final String VIEW_ID = "ideGenerateNodeTypeView";

        static final String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        static final String GENERATE_BUTTON = "ideGenerateNodeTypeViewGenerateButton";

        static final String CANCEL_BUTTON = "ideGenerateNodeTypeViewCancelButton";

        static final String NODE_TYPE_FIELD = "ideGenerateNodeTypeViewFormatField";

        static final String GENERATED_TYPE_VIEW_ID = "ideGeneratedTypePreviewView";

        static final String GENERATED_TYPE_VIEW_LOCATOR = "//div[@view-id='" + GENERATED_TYPE_VIEW_ID + "']";

        static final String GENERATED_TYPE_FRAME = GENERATED_TYPE_VIEW_LOCATOR + "//iframe";

        static final String OPERATION_FORM = "//div[@id='operation']/ancestor::div[contains(@style, 'height: 300')]";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(id = Locators.GENERATE_BUTTON)
    private WebElement generateButton;

    @FindBy(id = Locators.CANCEL_BUTTON)
    private WebElement cancelButton;

    @FindBy(name = Locators.NODE_TYPE_FIELD)
    private WebElement nodeTypeField;

    @FindBy(xpath = Locators.GENERATED_TYPE_VIEW_LOCATOR)
    private WebElement generatedTypeView;

    @FindBy(xpath = Locators.GENERATED_TYPE_FRAME)
    private WebElement generatedTypeFrame;

    @FindBy(xpath = Locators.OPERATION_FORM)
    private WebElement operationForm;

    /**
     * Wait Preview node type view opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return isOpened();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait Preview node type view closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(Locators.VIEW_LOCATOR));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Returns the opened state of the view and it's elements.
     *
     * @return {@link Boolean} opened state
     */
    public boolean isOpened() {
        return (view != null && view.isDisplayed() && generateButton != null && generateButton.isDisplayed()
                && cancelButton != null && cancelButton.isDisplayed() && nodeTypeField != null);
    }

    /**
     * Wait Generated type preview opened.
     *
     * @throws Exception
     */
    public void waitGeneratedTypeViewOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return isGeneratedTypeViewOpened();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait Generated type preview closed.
     *
     * @throws Exception
     */
    public void waitGeneratedTypeViewClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(Locators.GENERATED_TYPE_VIEW_LOCATOR));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Returns the opened state of the view and it's elements.
     *
     * @return {@link Boolean} opened state
     */
    public boolean isGeneratedTypeViewOpened() {
        return (operationForm != null && operationForm.isDisplayed() && generatedTypeView != null && generatedTypeView
                .isDisplayed());
    }

    /** Click Generate button. */
    public void clickGenerateButton() {
        generateButton.click();
    }

    /** Click Cancel button. */
    public void clickCancelButton() {
        cancelButton.click();
    }

    /** Select format of the node type. */
    public void selectFormat(String format) {
        new Select(nodeTypeField).selectByValue(format);
    }

    public String getGeneratedNodeType() throws InterruptedException {
        //FIXME Switch frames doesn't work with Google Chrome WebDriver.
        //Issue - http://code.google.com/p/selenium/issues/detail?id=1969
        driver().switchTo().frame(generatedTypeFrame);
        String text = driver().findElement(By.tagName("body")).getText();
        IDE().selectMainFrame();
        return text;
    }
}
