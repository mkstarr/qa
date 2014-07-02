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
package com.codenvy.sitecldide;

import com.codenvy.sitecldide.core.FooterMainElements;
import com.codenvy.sitecldide.core.HeaderMainElements;
import com.codenvy.sitecldide.core.MainElements;
import com.codenvy.sitecldide.core.ProfilePage;
import com.codenvy.sitecldide.core.RecoverPasswordPage;
import com.codenvy.sitecldide.core.ResetPasswordPage;
import com.codenvy.sitecldide.core.ThankYouPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Musienko Maxim
 *
 */
public class CLDIDE {

    public WebDriver driver;

    public MainElements        MAINPAGE;
    public ThankYouPage        THANK_YOU_PAGE;
    public FooterMainElements  FOOTER;
    public HeaderMainElements  HEADER;
    public RecoverPasswordPage RECOVER_PASSWORD;
    public ResetPasswordPage   RESET_PASSWORD;
    public ProfilePage         PROFILE;

    public CLDIDE(WebDriver instDriver) {
        this.driver = instDriver;
        PageFactory.initElements(driver, MAINPAGE = new MainElements(this));
        PageFactory.initElements(driver, THANK_YOU_PAGE = new ThankYouPage(this));
        PageFactory.initElements(driver, FOOTER = new FooterMainElements(this));
        PageFactory.initElements(driver, HEADER = new HeaderMainElements(this));
        PageFactory.initElements(driver, RECOVER_PASSWORD = new RecoverPasswordPage(this));
        PageFactory.initElements(driver, RESET_PASSWORD = new ResetPasswordPage(this));
        PageFactory.initElements(driver, PROFILE = new ProfilePage(this));
    }


    public WebDriver driver() {
        return driver;
    }


}
