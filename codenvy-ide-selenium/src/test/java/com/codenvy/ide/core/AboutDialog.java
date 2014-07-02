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
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:mmusienko@exoplatform.com">Musienko Maksim</a>
 *
 */

public class AboutDialog extends AbstractTestModule {

    /** @param ide */
    public AboutDialog(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    //Locators basic elements Dialog About Menu
    private interface Locators {

        String VIEW_ID = "ideAboutView";

        String OK_BUTTON_DIALOGABOUT = "ideAboutViewOkButton";

        String INFO_CONTETNT = "//div[@view-id=\"ideAboutView\"]//table/tbody//tr//div[@class=\"gwt-Label\"]";

        String LOGO = "//div[@view-id=\"ideAboutView\"]//table//tbody//tbody//td/img[@class=\"gwt-Image\"]";
    }

    //WebElemnts DialogAbout menu
    @FindBy(id = Locators.OK_BUTTON_DIALOGABOUT)
    private WebElement okButton;

    @FindBy(xpath = Locators.INFO_CONTETNT)
    private WebElement content;

    @FindBy(xpath = Locators.LOGO)
    private WebElement logo;

    /**
     * Click on button 'ok' and closing About menu
     *
     * @throws Exception
     */
    public void closeDialogAboutForm() throws Exception {
        okButton.click();
        waitClosedDialogAbout();
    }

    /** Check basic elements DialogAbout form */
    public void waitCheckInfoAboutWindow() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                String textContentAbout = content.getText();
                return (textContentAbout.contains("Codenvy") && textContentAbout.contains("Version:")
                        && textContentAbout.contains("Codenvy S.A. ©") && textContentAbout.contains("Revision:") &&
                        textContentAbout
                                .contains("Build Time:"));
            }
        });

    }

    public void waitLogoPresent() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.LOGO)));
    }

    /**
     * Wait DialogAbout dialog closed.
     *
     * @throws Exception
     */
    public void waitClosedDialogAbout() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.id(Locators.VIEW_ID));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });

    }

}
