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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

public class Debuger extends AbstractTestModule {
    /** @param ide */
    public Debuger(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String DEBUGER_VIEW_PANEL = "//div[@view-id='ideDebuggerView']";

        String DEBUGER_TAB = "//td[@class='tabTitleText' and text()='Debug']";

        String CHANGE_VAR_FIELD = "ideChangeVariableValueViewExpressionField";

        String CONFIRM_CHANGE_VAR_BTN = "ideChangeVariableValueViewChangeButton";

        String NUMBER_STRING_JAVAEDITOR =
                "//div[@panel-id='editor' and @is-active='true']//div[contains(@style, " +
                "'float: left;')][2]//div[@style and @class and text()='%s']";

        String BREACK_POINT_LINE_CONTAINER =
                "//div[@panel-id='editor' and @is-active='true']//div[contains(@style, 'float: left;')][3]/div";

        String BREACK_POINT_IMG = BREACK_POINT_LINE_CONTAINER + "//img";

        String VARIABLES_TAB = "//td[text()='Variables']";

        String BREACK_POINTS_TAB = "//td[text()='BreakPoints']";

        String BREACK_POINTS_TAB_CONTAINER = "//div[@__gwtcellbasedwidgetimpldispatchingfocus]";

        String VARIABLES_TAB_CONTAINER =
                "//div[@id='idedebuggervariabelpanelid']//div[@__gwtcellbasedwidgetimpldispatchingfocus='true']";

        String CLICK_ON_SPECIFIED_NAME_VARIABLE_TAB_VALUE =
                "//div[@id='idedebuggervariabelpanelid']//span[contains(., '%s')]";

        //Buttons locators section
        String RESUME_BTN = DEBUGER_VIEW_PANEL + "//div[@title='Resume']";

        String ENABLED_BTN_STATUS_CHANGE_RESUME_BTN = DEBUGER_VIEW_PANEL
                                                      + "//div[@title='Resume' and @button-enabled='%s']";

        String STEP_INTO_BTN = DEBUGER_VIEW_PANEL + "//div[@title='Step Into']";

        String ENABLED_BTN_STATUS_STEP_INTO_BTN = DEBUGER_VIEW_PANEL
                                                  + "//div[@title='Step Into' and @button-enabled='%s']";

        String STEP_OVER_BTN = DEBUGER_VIEW_PANEL + "//div[@title='Step Over']";

        String ENABLED_BTN_STATUS_STEP_OVER_BTN = DEBUGER_VIEW_PANEL
                                                  + "//div[@title='Step Over' and @button-enabled='%s']";

        String STEP_RETURN_BTN = DEBUGER_VIEW_PANEL + "//div[@title='Step Over']";

        String ENABLED_BTN_STATUS_STEP_RETURN_BTN = DEBUGER_VIEW_PANEL
                                                    + "//div[@title='Step Over' and @button-enabled='%s']";

        String ENABLED_BTN_STATUS_DISCONNECT_BTN = DEBUGER_VIEW_PANEL
                                                   + "//div[@title='Disconnect' and @button-enabled='%s']";

        String DISCONNECT_BTN = DEBUGER_VIEW_PANEL + "//div[@title='Disconnect']";

        String ENABLED_BTN_STATUS_REMOVE_ALL_BREACKPOINTS_BTN = DEBUGER_VIEW_PANEL
                                                                +
                                                                "//div[@title='Remove All BreakPoints' and " +
                                                                "@button-enabled='%s']";

        String REMOVE_ALL_BREACKPOINTS_BTN = DEBUGER_VIEW_PANEL + "//div[@title='Remove All BreakPoints']";

        String CHANGE_VALUE_BTN = DEBUGER_VIEW_PANEL + "//div[@title='Change Value']";

        String ENABLED_BTN_STATUS_CHANGE_VALUE = DEBUGER_VIEW_PANEL
                                                 + "//div[@title='Change Value' and @button-enabled='%s']";

        String EVALUATE_EXPRESSION = DEBUGER_VIEW_PANEL + "//div[@title='Evaluate Expression']";

        String ENABLED_BTN_STATUS_EVALUATE_EXPRESSION = DEBUGER_VIEW_PANEL
                                                        +
                                                        "//div[@title='Evaluate Expression' and @button-enabled='%s']";

        String EVALUATE_EXPRESSION_WINDOW = "ideEvaluateExpressionView-window";

        String EVALUATE_EXPRESSION_ENTER_FIELD = "ideEvaluateExpressionViewExpressionField";

        String EVALUATE_EXPRESSION_RESULT_FIELD = "ideEvaluateExpressionViewResultField";

        String EVALUATE_BUTTON_ENABLED = "div#ideEvaluateExpressionViewEvaluateButton[button-enabled='true']";

        String EVALUATE_BUTTON_DISABLED = "div#ideEvaluateExpressionViewEvaluateButton[button-enabled='false']";

        String EVALUATE_CANCEL_BUTTON = "ideEvaluateExpressionViewCancelButton";

        String CHANGE_VARIABLE_WINDOW = "ideChangeVariableValueView-window";

        String CHANGE_VARIABLE_TEXTAREA = "ideChangeVariableValueViewExpressionField";

        String CHANGE_VARIABLE_CHANGE_BTN = "ideChangeVariableValueViewChangeButton";

        String CHANGE_VARIABLE_CANCEL_BTN = "ideChangeVariableValueViewCancelButton";

        String BREACK_POINT_SET = "breakpoint-place-%s";

        String BREACK_POINT_ACTIVE = "//div[@is-active='true']//img[@id='breakpoit-active-%s']";

        String BREACK_POINT_TOGGLE = "//div[@is-active='true']//img[@id='breakpoit-toggle-%s']";

        String OPERATION_FORM = "//div[@id='operation']/ancestor::div[contains(@style, 'height: 300')]";

        String EXPIRED_SESSION_ASK_DIALOG_FORM = " //td//div[text()='Running time has expired']";

        String EXPIRED_SESSION_ASK_DIALOG_FORM_YES =
                "//td[contains(.,'Application will be stopped')]//td[text()='Yes']";

        String EXPIRED_SESSION_ASK_DIALOG_FORM_NO = "//td[contains(.,'Application will be stopped')]//td[text()='No']";

        String EXPIRED_SESSION_ASK_DIALOG_FORM_CLOSE =
                "//td//div[text()='Running time has expired']/parent::div/parent::div/img[@title='Close']";

    }

    @FindBy(xpath = Locators.RESUME_BTN)
    private WebElement resumeBtn;

    @FindBy(xpath = Locators.STEP_INTO_BTN)
    private WebElement stepIntoBtn;

    @FindBy(xpath = Locators.STEP_OVER_BTN)
    private WebElement stepOver;

    @FindBy(xpath = Locators.CHANGE_VALUE_BTN)
    private WebElement changeValueBtn;

    @FindBy(xpath = Locators.STEP_RETURN_BTN)
    private WebElement stepReturnBtn;

    @FindBy(xpath = Locators.DISCONNECT_BTN)
    private WebElement disconnectBtn;

    @FindBy(xpath = Locators.REMOVE_ALL_BREACKPOINTS_BTN)
    private WebElement removeAllBreakPointsBtn;

    @FindBy(xpath = Locators.EVALUATE_EXPRESSION)
    private WebElement evalueteExpressionBtn;

    @FindBy(xpath = Locators.DEBUGER_VIEW_PANEL)
    private WebElement view;

    @FindBy(name = Locators.CHANGE_VAR_FIELD)
    private WebElement changeField;

    @FindBy(id = Locators.CONFIRM_CHANGE_VAR_BTN)
    private WebElement confirmChange;

    @FindBy(id = Locators.CHANGE_VARIABLE_WINDOW)
    private WebElement changeVariableWin;

    @FindBy(xpath = Locators.BREACK_POINT_LINE_CONTAINER)
    private WebElement bracPointContainer;

    @FindBy(xpath = Locators.DEBUGER_TAB)
    private WebElement debugerTab;

    @FindBy(xpath = Locators.BREACK_POINT_IMG)
    private WebElement breackPoint;

    @FindBy(xpath = Locators.BREACK_POINTS_TAB)
    private WebElement breackPointTab;

    @FindBy(xpath = Locators.VARIABLES_TAB)
    private WebElement variablesTab;

    @FindBy(xpath = Locators.BREACK_POINTS_TAB_CONTAINER)
    private WebElement breakPointsContainer;

    @FindBy(id = Locators.VARIABLES_TAB_CONTAINER)
    private WebElement variablesTabContainer;

    @FindBy(xpath = Locators.CLICK_ON_SPECIFIED_NAME_VARIABLE_TAB_VALUE)
    private WebElement clickOnValueInVariableTab;

    @FindBy(name = Locators.CHANGE_VARIABLE_TEXTAREA)
    private WebElement changVariableTextField;

    @FindBy(id = Locators.CHANGE_VARIABLE_CHANGE_BTN)
    private WebElement changeVariableChangeBtn;

    @FindBy(id = Locators.CHANGE_VARIABLE_CANCEL_BTN)
    private WebElement changeVariableCancelBtn;

    @FindBy(xpath = Locators.OPERATION_FORM)
    private WebElement operationForm;

    @FindBy(id = Locators.EVALUATE_EXPRESSION_WINDOW)
    private WebElement evaluateExpressionWindow;

    @FindBy(name = Locators.EVALUATE_EXPRESSION_ENTER_FIELD)
    private WebElement evaluateExpressionEnterField;

    @FindBy(name = Locators.EVALUATE_EXPRESSION_RESULT_FIELD)
    private WebElement evaluateExpressionResultField;

    @FindBy(id = Locators.EVALUATE_CANCEL_BUTTON)
    private WebElement ideEvaluateCancelButton;

    @FindBy(css = Locators.EVALUATE_BUTTON_DISABLED)
    private WebElement ideEvaluateButtonDisabled;

    @FindBy(css = Locators.EVALUATE_BUTTON_ENABLED)
    private WebElement ideEvaluateButtonEnabled;

    @FindBy(xpath = Locators.EXPIRED_SESSION_ASK_DIALOG_FORM)
    private WebElement expiredFormView;

    @FindBy(xpath = Locators.EXPIRED_SESSION_ASK_DIALOG_FORM_YES)
    private WebElement expiredFormYesBtn;

    @FindBy(xpath = Locators.EXPIRED_SESSION_ASK_DIALOG_FORM_NO)
    private WebElement expiredFormNoBtn;

    @FindBy(xpath = Locators.EXPIRED_SESSION_ASK_DIALOG_FORM_CLOSE)
    private WebElement expiredFormClose;

    /**
     * click on change variable btn
     *
     * @throws Exception
     */
    public void changeVarBtnClick() throws Exception {
        changeValueBtn.click();
    }

    /**
     * Wait Evaluate expression view opened.
     *
     * @throws Exception
     */
    public void waitEvaluateOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return evaluateExpressionResultField != null && evaluateExpressionResultField.isDisplayed()
                       && evaluateExpressionEnterField != null && evaluateExpressionEnterField.isDisplayed()
                       && ideEvaluateCancelButton.isDisplayed() && ideEvaluateButtonDisabled.isDisplayed();
            }
        });
    }

    /**
     * Wait Evaluate expression view opened.
     *
     * @throws Exception
     */
    public void waitEvaluateResult(final String result) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return getTextFromEvaluateResult().equals(result);
                } catch (Exception e) {
                    return false;
                }

            }
        });
    }

    /**
     * Wait while evaluateform closed
     *
     * @throws Exception
     */
    public void waitEvaluateWindowIsClosed() throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.EVALUATE_EXPRESSION_WINDOW)));
    }

    /**
     * Wait while evaluate btn change on enabled status
     *
     * @throws Exception
     */
    public void waitEvaluateButtonEnabled() throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(ideEvaluateButtonEnabled));
    }

    /**
     * Wait build project view opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return operationForm != null && operationForm.isDisplayed() && view != null && view.isDisplayed();
            }
        });
    }

    /** wait appearance debugger tab */
    public void waitTabOfDebuger() {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return debugerTab != null && debugerTab.isDisplayed();
            }
        });
    }

    /** wait appearance variables tab in the IDE */
    public void waitVariablesTab() {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return variablesTab.isDisplayed();
            }
        });
    }

    /** wait appearance window for change variable value */
    public void waitChangeVariableWindow() {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return changeVariableWin.isDisplayed();
            }
        });
    }

    /** wait appearance variables tab in the IDE */
    public void waitVariablesTabContainer() {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return variablesTabContainer.isDisplayed();
            }
        });
    }

    /** wait appearance variables tab in the IDE */
    public void waitVariablesTabContainerIsEmpty() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .xpath(Locators
                                                                                                                     .VARIABLES_TAB_CONTAINER)));

    }

    /** wait appearance value with specified value */
    public void waitInVariableTabContainerWithSpecidiedValue(final String value) {
        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(By.xpath(Locators.VARIABLES_TAB_CONTAINER)).getText().contains(value);
            }
        });
    }

    /** wait appearance in breakpointTab some breakpoint with specified string */
    public void waitBreackPointsTabContainetWithSpecifiedValue(final String value) {
        waitBreackPointsTabContainer();
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getTextFromBreackPointTabContainer().contains("[line :");
            }
        });
    }

    /** wait appearance breakpointTab tab in the IDE */
    public void waitBreackPointsTab() {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return breackPointTab.isDisplayed();
            }
        });
    }

    /** wait appearance tab with breakpoints */
    public void waitBreackPointsTabContainer() {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return breakPointsContainer.isDisplayed();
            }
        });
    }

    /** wait while breakpoint container will is empty */
    public void waitBreackPointsTabContainerIsEmpty() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getTextFromBreackPointTabContainer().isEmpty();
            }
        });
    }

    /**
     * Wait while will open more 1 window. This method uses for check opening the window with debug demo project.
     * Because
     * demo project opened In a separate window
     *
     * @throws Exception
     */
    public void waitOpenedSomeWin() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Set<String> driverWindows = driver().getWindowHandles();
                return (driverWindows.size() > 1) ? (true) : (false);
            }
        });
    }

    /**
     * Wait change variable field
     *
     * @throws Exception
     */
    public void waitChangeWarField() throws Exception {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return changeField != null && changeField.isDisplayed();
            }
        });
    }

    /**
     * wait appearance toggle breakpoint image in specified position
     *
     * @param position
     * @throws Exception
     */
    public void waitToggleBreackPointIsSet(int position) throws Exception {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.BREACK_POINT_TOGGLE, position))));

    }

    /**
     * wait appearance active breakpoint image in specified position
     *
     * @param position
     * @throws Exception
     */
    public void waitActiveBreackPointIsSet(int position) throws Exception {

        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.BREACK_POINT_ACTIVE, position))));

    }

    public void waitExparedSessionView(int timeImMinute, int responsePauseInSeconds) throws Exception {
        new WebDriverWait(driver(), 60 * timeImMinute, responsePauseInSeconds * 1000).until(ExpectedConditions
                                                                                                    .visibilityOfElementLocated(
                                                                                                            By.xpath(
                                                                                                                    Locators
                                                                                                                            .EXPIRED_SESSION_ASK_DIALOG_FORM)));
    }

    public void waitExparedSessionViewIsClose() throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .xpath(Locators
                                                                                                                     .EXPIRED_SESSION_ASK_DIALOG_FORM)));
    }

    /**
     * wait YES button on expire session debuer form
     *
     * @throws Exception
     */
    public void waitExparedSessionYesBtn() throws Exception {
        new WebDriverWait(driver(), 5).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                   .xpath(Locators
                                                                                                                  .EXPIRED_SESSION_ASK_DIALOG_FORM_YES)));
    }

    /**
     * wait NO button on expire session debuer form
     *
     * @throws Exception
     */

    public void waitExparedSessionNoBtn() throws Exception {
        new WebDriverWait(driver(), 5).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                   .xpath(Locators
                                                                                                                  .EXPIRED_SESSION_ASK_DIALOG_FORM_NO)));
    }

    /**
     * wait Close Img button on expire session debuer form
     *
     * @throws Exception
     */

    public void waitExparedSessionCloseImgBtn() throws Exception {
        new WebDriverWait(driver(), 5).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                   .xpath(Locators
                                                                                                                  .EXPIRED_SESSION_ASK_DIALOG_FORM_CLOSE)));
    }

    /**
     * Wait change variable field
     *
     * @throws Exception
     */
    public void typeToChangeVariableField(String value) throws Exception {
        changeField.sendKeys(value);
    }

    /**
     * Wait change variable field
     *
     * @throws Exception
     */
    public void confirmChangeBtnClick() throws Exception {
        confirmChange.click();
    }

    public void waitDebugerIsClosed() throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .xpath(Locators.DEBUGER_VIEW_PANEL)));

    }

    // waits for enabled/disabled status button section:
    //-------------------------------------------

    /**
     * wait evaluate expression enabled/disabled button
     *
     * @param status
     * @throws Exception
     */
    public void waitEvaluateExpressionIsEnabled(final boolean status) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(
                        By.xpath(String.format(Locators.ENABLED_BTN_STATUS_EVALUATE_EXPRESSION,
                                               Boolean.toString(status))))
                        .isDisplayed();
            }
        });
    }

    /**
     * wait remove all breakpoints enabled/disabled button
     *
     * @param status
     * @throws Exception
     */
    public void waitRemoveAllBreakPointsBtnIsEnabled(final boolean status) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver()
                        .findElement(
                                By.xpath(String.format(Locators.ENABLED_BTN_STATUS_REMOVE_ALL_BREACKPOINTS_BTN,
                                                       Boolean.toString(status)))).isDisplayed();
            }
        });
    }

    /**
     * wait disconnect enabled/disabled button
     *
     * @param status
     * @throws Exception
     */
    public void waitDisconnectBtnIsEnabled(final boolean status) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(
                        By.xpath(String.format(Locators.ENABLED_BTN_STATUS_DISCONNECT_BTN, Boolean.toString(status))))
                        .isDisplayed();
            }
        });
    }

    /**
     * wait step return enabled/disabled button
     *
     * @param status
     * @throws Exception
     */
    public void waitStepReturnBtnIsEnabled(final boolean status) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(
                        By.xpath(String.format(Locators.ENABLED_BTN_STATUS_STEP_RETURN_BTN, Boolean.toString(status))))
                        .isDisplayed();
            }
        });
    }

    /**
     * wait step over enabled/disabled button
     *
     * @param status
     * @throws Exception
     */
    public void waitStepOverBtnIsEnabled(final boolean status) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(
                        By.xpath(String.format(Locators.ENABLED_BTN_STATUS_STEP_OVER_BTN, Boolean.toString(status))))
                        .isDisplayed();
            }
        });
    }

    /**
     * wait step into enabled/disabled button
     *
     * @param status
     * @throws Exception
     */
    public void waitStepIntoBtnIsEnabled(final boolean status) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(
                        By.xpath(String.format(Locators.ENABLED_BTN_STATUS_STEP_INTO_BTN, Boolean.toString(status))))
                        .isDisplayed();
            }
        });
    }

    /**
     * wait resume enabled/disabled button
     *
     * @param status
     * @throws Exception
     */
    public void waitResumeBtnIsEnabled(final boolean status) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(
                        By.xpath(
                                String.format(Locators.ENABLED_BTN_STATUS_CHANGE_RESUME_BTN, Boolean.toString(status))))
                        .isDisplayed();
            }
        });
    }

    /**
     * wait value enabled/disabled button
     *
     * @param status
     * @throws Exception
     */
    public void waitChangeValueBtnIsEnabled(final boolean status) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(
                        By.xpath(String.format(Locators.ENABLED_BTN_STATUS_CHANGE_VALUE, Boolean.toString(status))))
                        .isDisplayed();
            }
        });
    }

    /** wait while change variable window is closed */
    public void waitChangeVariableWindowClose() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .id(Locators
                                                                                                                  .CHANGE_VARIABLE_WINDOW)));
    }

    public void waitBrecpointSettedInPusition(int position) {

    }

    /** click in debuger tab */
    public void clickOnDebugerTab() {
        debugerTab.click();
    }

    /**
     * set breackpoint in specified position
     *
     * @param numLine
     * @throws Exception
     */
    public void setBreakPoint(final int numLine) throws Exception {
        WebElement elem = driver().findElement(By.id(String.format(Locators.BREACK_POINT_SET, numLine)));
        elem.click();
    }

    /**
     * get all text in breakpoint Tab control in IDE
     *
     * @return
     */
    public String getTextFromBreackPointTabContainer() {
        return breakPointsContainer.getText();
    }

    /**
     * get all text from variables tab container
     *
     * @return
     */
    public String getTextFromVariablesTabContainer() {
        return variablesTabContainer.getText();
    }

    /**
     * get all text from variables tab container
     *
     * @return
     */
    public void clickOnValueInVariableTabContainer(String val) {
        driver().findElement(By.xpath(String.format(Locators.CLICK_ON_SPECIFIED_NAME_VARIABLE_TAB_VALUE, val))).click();
    }

    /**
     * click on change variable on debug panel btn
     *
     * @return
     */
    public void clickOnChangeValueBtn() {
        changeValueBtn.click();
    }

    /** clear all text in change variable field */
    public void clearChangVariableTexField() {
        changVariableTextField.clear();
    }

    /** type new values into change variable text field */
    public void typeValueToChangeVariable(String val) {
        changeValueBtn.sendKeys(val);
    }

    /** click on cancel btn change variable control */
    public void clickCancelBtnChangeVariableWindow() {
        changeVariableCancelBtn.click();
    }

    /** click on change btn change variable control */
    public void clickChangeVariableBtnOnVariableWindow() {
        changeVariableChangeBtn.click();
    }

    /**
     * click on resume btn
     *
     * @throws Exception
     */
    public void clickResumeBtnClick() throws Exception {
        resumeBtn.click();
    }

    /**
     * click on steoOver btn
     *
     * @throws Exception
     */
    public void clickStepOverBtnClick() throws Exception {
        stepOver.click();
    }

    /**
     * click on stepinto btn
     *
     * @throws Exception
     */
    public void clickStepIntoBrnClick() throws Exception {
        stepIntoBtn.click();
    }

    /**
     * click on ste return btn
     *
     * @throws Exception
     */
    public void clickStepReturnBtnClick() throws Exception {
        stepReturnBtn.click();
    }

    /**
     * click on disconnect button
     *
     * @throws Exception
     */
    public void clickDisconnectBtnClick() throws Exception {
        disconnectBtn.click();
    }

    /**
     * click on evaluateExpression button
     *
     * @throws Exception
     */
    public void clickToolBarEvaluateExpression() throws Exception {
        evalueteExpressionBtn.click();
    }

    /**
     * click on evaluateExpression button
     *
     * @throws Exception
     */
    public void clickEvaluateOnForm() throws Exception {
        ideEvaluateButtonEnabled.click();
    }

    /**
     * click on cancel button
     *
     * @throws Exception
     */
    public void clickCancelEvaluteForm() throws Exception {
        ideEvaluateCancelButton.click();
    }

    /**
     * type expression
     *
     * @throws Exception
     */
    public void typeEvaluateExpression(String expression) throws Exception {
        evaluateExpressionEnterField.sendKeys(expression);
    }

    /**
     * click on
     *
     * @throws Exception
     */
    public String getTextFromEvaluateResult() throws Exception {
        return evaluateExpressionResultField.getAttribute("value");
    }

    /**
     * click on yes button of expired session debugger form
     *
     * @throws Exception
     */
    public void expiredYesBtnClick() {
        expiredFormYesBtn.click();
    }

    /**
     * click on no button of expired session debugger form
     *
     * @throws Exception
     */
    public void expiredNoBtnClick() {
        expiredFormNoBtn.click();
    }

    /**
     * click on no close label of expired session debugger form
     *
     * @throws Exception
     */
    public void expiredCloseClick() {
        expiredFormClose.click();
    }

}
