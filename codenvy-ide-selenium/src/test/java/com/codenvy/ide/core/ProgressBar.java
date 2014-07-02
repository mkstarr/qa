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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Oksana Vereshchaka
 *
 */
public class ProgressBar extends AbstractTestModule {
    /** @param ide */
    public ProgressBar(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private static final String PROGRESS_BAR_CONTROL = "//div[@control-id='__request-notification-control']";

    private static final String PROGRESS_BAR_CONTROL_CLICK =
            "//div[@control-id='__request-notification-control']//table//table";

    public static final String CANCEL_BUTTON_ID = "//div[@control-id='__request-build-cancel-notification-control']";

    private static final String PROGRESS_BAR_VIEW = "//div[@view-id='ideRequestNotificationView']";

    public static final String BUILD_CANCELLED_BAR = "//div[@control-id='__request-notification-control']//td[text()='Build cancelled']";

    private final String BUILD_FAILED = "//div[@control-id='__request-notification-control']//td[text()='Building of project failed']";

    @FindBy(xpath = PROGRESS_BAR_CONTROL)
    private WebElement control;

    @FindBy(xpath = PROGRESS_BAR_VIEW)
    private WebElement view;

    @FindBy(xpath = PROGRESS_BAR_CONTROL_CLICK)
    private WebElement clicker;

    @FindBy(xpath = CANCEL_BUTTON_ID)
    private WebElement cancelBuild;

    @FindBy(xpath = BUILD_FAILED)
    private WebElement buildFailed;

    public void waitProgressBarView() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return view != null && view.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** click on progress bar */
    public void clickProgressBarControl() {
        clicker.click();
    }

    public void waitProgressBarControl() {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return control != null && control.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    public void waitProgressBarControlClose() {
        new WebDriverWait(driver(), 600).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                       .xpath
                                                                                                               (PROGRESS_BAR_CONTROL)));
    }

    /**
     * get all text from progress tab
     *
     * @return
     */
    public String getViewText() {
        return view.getText();
    }

    /** waiting while cancel button appear */
    public void waitCancelButtonAppear() {
        new WebDriverWait(driver(), 300).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CANCEL_BUTTON_ID)));
    }

    /** waiting while cancel button disappear */
    public void waitCancelButtonDisappear() {
        new WebDriverWait(driver(), 300).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CANCEL_BUTTON_ID)));
    }

    /** click on cancel button */
    public void clickOnCancelBuildButton() {
        cancelBuild.click();
    }

    /** waiting for build canceled bar */
    public void waitBuildCanceledBar() {
        new WebDriverWait(driver(), 300).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(BUILD_CANCELLED_BAR)));
    }

    /**
     * wait failed build notifacation on progressor
     *
     * @param timeSec
     *         waiting time in sec
     */
    public void waitBuildFailed(int timeSec) {
        new WebDriverWait(driver(), timeSec).until(ExpectedConditions.visibilityOf(buildFailed));
    }

    /**
     * wait failed build notifacation on progressor with setted delay for DOM responce
     *
     * @param timeSec
     *         waiting notification time
     * @param responceDOMPause
     *         responce DOM in millisec
     */
    public void waitBuildFailed(int timeSec, int responceDOMPause) {
        new WebDriverWait(driver(), timeSec, responceDOMPause).until(ExpectedConditions.visibilityOf(buildFailed));
    }

}
