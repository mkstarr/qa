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

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:mmusienko@exoplatform.com">Musienko Maksim</a>
 *
 */

public class FindReplace extends AbstractTestModule {
    /** @param ide */
    public FindReplace(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_LOCATOR = "//div[@view-id='ideFindReplaceTextView']";

        String FIND_BUTTON_ID = "ideFindReplaceTextFormFindButton";

        String REPLACE_BUTTON_ID = "ideFindReplaceTextFormReplaceButton";

        String REPLACE_FIND_BUTTON_ID = "ideFindReplaceTextFormReplaceFindButton";

        String REPLACE_ALL_BUTTON_ID = "ideFindReplaceTextFormReplaceAllButton";

        String FIND_FIELD_ID = "ideFindReplaceTextFormFindField";

        String REPLACE_FIELD_ID = "ideFindReplaceTextFormReplaceField";

        String CASE_SENSITIVE_FIELD_ID = "ideFindReplaceTextFormCaseSensitiveField";

        String FIND_RESULT_LABEL_ID = "ideFindReplaceTextFormFindResult";

        String OPERATION_FORM = "//div[@id='operation']/ancestor::div[contains(@style, 'height: 300')]";

    }

    private static final String VIEW_TITLE = "Find/Replace";

    public static final String NOT_FOUND_RESULT = "String not found.";

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(id = Locators.FIND_BUTTON_ID)
    private WebElement findButton;

    @FindBy(id = Locators.REPLACE_BUTTON_ID)
    private WebElement replaceButton;

    @FindBy(id = Locators.REPLACE_FIND_BUTTON_ID)
    private WebElement replaceFindButton;

    @FindBy(id = Locators.REPLACE_ALL_BUTTON_ID)
    private WebElement replaceAllButton;

    @FindBy(name = Locators.FIND_FIELD_ID)
    private WebElement findField;

    @FindBy(name = Locators.REPLACE_FIELD_ID)
    private WebElement replaceField;

    @FindBy(name = Locators.CASE_SENSITIVE_FIELD_ID)
    private WebElement caseSensitiveField;

    @FindBy(id = Locators.FIND_RESULT_LABEL_ID)
    private WebElement resultLabel;

    @FindBy(xpath = Locators.OPERATION_FORM)
    private WebElement operationForm;

    /**
     * Wait Find/Replace text view opened.
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

    public boolean isOpened() {
        return (view != null && view.isDisplayed() && findField != null && findField.isDisplayed()
                && replaceField != null && replaceField.isDisplayed() && findButton != null && findButton.isDisplayed()
                && replaceButton != null && replaceButton.isDisplayed() && replaceFindButton != null
                && replaceFindButton.isDisplayed() && replaceAllButton != null && replaceAllButton.isDisplayed()
                && caseSensitiveField != null && caseSensitiveField.isDisplayed() && operationForm.isDisplayed() &&
                operationForm != null);
    }

    public void waitCloseButtonAppeared() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    WebElement closeButton = IDE().PERSPECTIVE.getCloseViewButton(VIEW_TITLE);
                    return closeButton != null && closeButton.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    public void waitFindButtonAppeared() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return findButton != null && findButton.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    public void waitFindFieldAppeared() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return findField != null && findField.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait Find/Replace text view closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return view == null || !view.isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    public void closeView() {
        IDE().PERSPECTIVE.getCloseViewButton(VIEW_TITLE).click();
    }

    /**
     * Get enabled state of find button.
     *
     * @return boolean enabled state of find button
     */
    public boolean isFindButtonEnabled() {
        return IDE().BUTTON.isButtonEnabled(findButton);
    }

    /** wait enabled state of find button. */
    public void waitFindButtonEnabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return IDE().BUTTON.isButtonEnabled(findButton);
            }
        });
    }

    /** wait disabled state of find button. */
    public void waitFindButtonDisabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !(IDE().BUTTON.isButtonEnabled(findButton));
            }
        });
    }

    /**
     * Get enabled state of replace button.
     *
     * @return boolean enabled state of replace button
     */
    public boolean isReplaceButtonEnabled() {
        return IDE().BUTTON.isButtonEnabled(replaceButton);
    }

    /** wait enabled state of replace button. */
    public void waitReplaceButtonEnabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return IDE().BUTTON.isButtonEnabled(replaceButton);
            }
        });
    }

    /** wait disabled state of replace button. */
    public void waitReplaceButtonDisabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !(IDE().BUTTON.isButtonEnabled(replaceButton));
            }
        });
    }

    /**
     * Get enabled state of replace/find button.
     *
     * @return boolean enabled state of replace/find button
     */
    public boolean isReplaceFindButtonEnabled() {
        return IDE().BUTTON.isButtonEnabled(replaceFindButton);
    }

    /** wait enabled state of replace/find button. */
    public void waitReplaceFindButtonEnabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return IDE().BUTTON.isButtonEnabled(replaceFindButton);
            }
        });
    }

    /** wait disabled state of replace/find button. */
    public void waitReplaceFindButtonDisabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !(IDE().BUTTON.isButtonEnabled(replaceFindButton));
            }
        });
    }

    /**
     * Get enabled state of replace all button.
     *
     * @return boolean enabled state of replace all button
     */
    public boolean isReplaceAllButtonEnabled() {
        return IDE().BUTTON.isButtonEnabled(replaceAllButton);
    }

    /** wait enabled state of replace all button button. */
    public void waitReplaceAllButtonEnabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return IDE().BUTTON.isButtonEnabled(replaceAllButton);
            }
        });
    }

    /** wait disabled state of replace all button button. */
    public void waitReplaceAllButtonDisabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !(IDE().BUTTON.isButtonEnabled(replaceAllButton));
            }
        });
    }

    public void typeInFindField(String text) throws InterruptedException {
        findField.click();
        findField.sendKeys(text);
    }

    public void typeInReplaceField(String text) throws InterruptedException {
        replaceField.click();
        replaceField.sendKeys(text);
    }

    public void clickFindButton() throws InterruptedException {
        findButton.click();
    }

    public void clickReplaceButton() throws InterruptedException {
        replaceButton.click();
    }

    public void clickReplaceFindButton() throws InterruptedException {
        replaceFindButton.click();
    }

    public void clickReplaceAllButton() throws InterruptedException {
        replaceAllButton.click();
    }

    public void waitFindResultEmpty() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return getFindResultText().isEmpty();
            }
        });
    }

    public void waitFindResultNotFound() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return NOT_FOUND_RESULT.equals(getFindResultText());
            }
        });
    }

    /**
     * Returns the value of the find text result label.
     *
     * @return {@link String} result of the find text operation
     */
    public String getFindResultText() {
        return resultLabel.getText();
    }

    /** Click on case sensitive field. */
    public void clickCaseSensitiveField() {
        caseSensitiveField.click();
    }
}
