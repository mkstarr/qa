/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.ide.mail;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/** The class checks links in email after registration, creating TENANT_NAME */
public class MailCheck extends AbstractTestModule {

    /** @param ide */
    public MailCheck(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private static final Logger LOG = LoggerFactory.getLogger(MailCheck.class);

    private static String INVITE_LINK;

    private static String CONFIRM_LINK;

    private static String FACTORY_URL;

    private static String PROTOCOL = "https://"; //http:// or https://;

    /** The method goto page to conform invite */
    public void gotoConfirmInvitePage() {
        LOG.info("Invite link ............ " + INVITE_LINK);
        driver().get(INVITE_LINK);
    }

    /** The method goto page to conform create */
    public void gotoConfirmCreatePage() {
        LOG.info("Invite link ............ " + CONFIRM_LINK);
        driver().get(CONFIRM_LINK);
    }

    /**
     * Waits for email with credentials
     *
     * @throws MessagingException
     * @throws TimeoutException
     */
    public void waitAndGetInviteLink(final String user, final String password) {

        new WebDriverWait(driver(), 400, 40000).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                try {
                    MailReceiver.receiveInviteEmail(user, password);
                    INVITE_LINK =
                            MailReceiver.getLink("You've been invited to use " + BaseTest.TENANT_NAME + " workspace", BaseTest.LOGIN_URL_VFS + "/ide/" + BaseTest.TENANT_NAME);
                                                 //BaseTest.LOGIN_URL_VFS + "/api/" + BaseTest.TENANT_NAME + "/invite/activate");
                    System.out.println(INVITE_LINK);

                    if (INVITE_LINK != null) {
                        return true;
                    }
                    return false;

                } catch (MessagingException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                } catch (TimeoutException e1) {
                    return false;
                }
            }
        });

    }

    /**
     * wait confirmation link in email box
     *
     * @param user
     *         (your nick for e-mail)
     * @param password
     *         (your password for access to e-mail box)
     */
    public void waitAndGetConfirmLink(final String user, final String password, final String tenantName) {

        new WebDriverWait(driver(), 400, 40000).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                try {
                    MailReceiver.receiveInviteEmail(user, password);
                    CONFIRM_LINK =
                            MailReceiver.getConfirmLink("Your Codenvy Workspace", tenantName);
                    System.out.println("Confirm tenant creation link:\n" + CONFIRM_LINK);

                    if (CONFIRM_LINK != null) {
                        return true;
                    }
                    return false;

                } catch (MessagingException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                } catch (TimeoutException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    return false;
                }
            }
        });

    }

    public void readMail(final String user, final String password) throws TimeoutException, MessagingException {
        try {
            MailReceiver.receiveInviteEmail(user, password);


        } catch (TimeoutException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MessagingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /**
     * Waits for email with factory url
     *
     * @throws MessagingException
     * @throws TimeoutException
     */
    public void waitAndGetFactoryURL(final String user, final String password) {

        new WebDriverWait(driver(), 500, 20000).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                try {
                    MailReceiver.receiveInviteEmail(user, password);
                    FACTORY_URL =
                            MailReceiver.getFactoryLink("Check out my Codenvy project", BaseTest.USER_NAME);
                    System.out.println(FACTORY_URL);

                    if (FACTORY_URL != null) {
                        return true;
                    }
                    return false;

                } catch (MessagingException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                } catch (TimeoutException e1) {
                    return false;
                }
            }
        });

    }

    /** The method goto page to conform invite */
    public void openFactoryURL() throws InterruptedException {

        driver().get(FACTORY_URL);
    }

    public void cleanMailBox(String user, String password) throws TimeoutException, MessagingException {
        MailReceiver.cleanMailBox(user, password);
    }
}
