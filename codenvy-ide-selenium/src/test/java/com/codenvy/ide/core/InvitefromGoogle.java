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
package com.codenvy.ide.core;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Object Page for describing main elements Invite developers control */

public class InvitefromGoogle extends AbstractTestModule {
    /** @param ide */
    public InvitefromGoogle(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final class Locators {
        final static String INVITE_DEVELOPERS_ID_ACTIVE = "//div[@id='ide.inviteGitHubDevelopersView-window']";

        final static String INVITE_BTN =
                "//div[@id='ide.inviteGitHubDevelopersView-window']//div[@button-enabled and contains(.,'Invite')]";

        final static String CANCEL_BTN = "//div[@view-id='ide.inviteGitHubDevelopersView']//td[text()='Cancel']";

        final static String INVITE_ALL_CHKBOX = "//div[text()='Invite all']/parent::node()//input";

        final static String ENTER_EMAIL_ADRESS_MANYALLY =
                "//div[@id='ide.inviteGitHubDevelopersView-window']//input[@type='text']";

        final static String INVITE_CHECK_BOX_GOOGLE =
                "//div[@id='ide.inviteGitHubDevelopersView-window']/div/table/tbody/tr[2]/td[2]/div/div/div[2]/div"
                +
                "/div[2]/div/div[5]/div[1]/div[%s]//input[@type='checkbox']";

        final static String INVITE_CHECK_BOX_GITHUB =
                "//div[@id='ide.inviteGitHubDevelopersView-window']//div[text()='%s']/..//input[@type='checkbox']";

        final static String INVITATION_MESSAGE = "//div[@id='ide.inviteGitHubDevelopersView-window']//textarea";

        final static String INFO_POP_UP = "ideInformationModalView-window";

        final static String POP_UP_OK_BUTTON = "OkButton";

        final static String USER_FROM_CONTACTS_TO_INVITE =
                "//div[@id='ide.inviteGitHubDevelopersView-window']//div[text()='%s']";

        final static String CONNECT_TO_GOOGLE_VIEW =
                "//div[@id='ideAskModalView-window']//div[text()='You have to be logged in Google account!']";

        final static String OK_BUTTON_ON_CONNECT_TO_GOOGLE_FORM =
                "//div[@id='ideAskModalView-window']//div[@id='YesButton']";

        final static String SHOW_GMAIL_CONTACTS_BUTTON =
                "//div[@view-id='ide.inviteGitHubDevelopersView']//td[text()='Show gmail contacts']";

        final static String ADD_A_MESSAGE_BUTTON = "//div[@view-id='ide.inviteGitHubDevelopersView']//td[text()='Add a message']";
    }

    @FindBy(xpath = Locators.INVITE_DEVELOPERS_ID_ACTIVE)
    private WebElement viewDevelopers;

    @FindBy(xpath = Locators.INVITE_BTN)
    private WebElement invBtn;

    @FindBy(xpath = Locators.INVITE_ALL_CHKBOX)
    private WebElement invAll;

    @FindBy(xpath = Locators.ENTER_EMAIL_ADRESS_MANYALLY)
    private WebElement typeEmailForInvite;

    @FindBy(xpath = Locators.INVITATION_MESSAGE)
    private WebElement invitationMessage;

    @FindBy(id = Locators.INFO_POP_UP)
    private WebElement popUp;

    @FindBy(id = Locators.POP_UP_OK_BUTTON)
    private WebElement popUpOkBtn;

    @FindBy(xpath = Locators.CONNECT_TO_GOOGLE_VIEW)
    private WebElement connectToGoogleView;

    @FindBy(xpath = Locators.OK_BUTTON_ON_CONNECT_TO_GOOGLE_FORM)
    private WebElement okButtonOnConnectToGoogleView;

    @FindBy(xpath = Locators.CANCEL_BTN)
    private WebElement cancelBtn;

    @FindBy(xpath = Locators.SHOW_GMAIL_CONTACTS_BUTTON)
    private WebElement showGmailContactsButton;

    @FindBy(xpath = Locators.ADD_A_MESSAGE_BUTTON)
    private WebElement addAMessageButton;

    /**
     * Wait Available dependencies view opened.
     *
     * @throws Exception
     */
    public void waitInviteDevelopersOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return viewDevelopers.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }


    /** wait while collaboration form is closed */
    public void waitInviteDevelopersClosed() {
        new WebDriverWait(driver(), 15)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.INVITE_DEVELOPERS_ID_ACTIVE)));

    }


    /** click on invite button */
    public void inviteClick() {
        invBtn.click();
    }

    /** Click on invite all check box */
    public void clickInviteAll() {
        invAll.click();
    }

    /**
     * Type email in to form
     *
     * @param value
     */
    public void typeEmailForInvite(String value) {
        typeEmailForInvite.clear();
        typeEmailForInvite.sendKeys(value);
    }

    /**
     * Type to personal message
     *
     * @param value
     */
    public void typeInvitationMeassge(String value) {
        invitationMessage.sendKeys(value);
    }

    /** get text from invite button */
    public String getTextFromIviteButton() {
        return invBtn.getText();
    }

    /**
     * wait for information pop up form
     *
     * @throws Exception
     */
    public void waitPopUp() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return popUp != null && popUp.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** Click ok on information pop up form */
    public void clickOkOnPopUp() {
        popUpOkBtn.click();
    }

    /**
     * wait for appearing users from contact in form to invite
     *
     * @throws Exception
     */
    public void waitForUsersFromContactsToInvite(final String user) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    WebElement elem =
                            driver().findElement(By.xpath(String.format(Locators.USER_FROM_CONTACTS_TO_INVITE, user)));
                    return elem.isDisplayed() && elem != null;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * wait for appearing connect to google form
     *
     * @throws Exception
     */
    public void waitForConnectToGoogleForm() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {

                    return connectToGoogleView.isDisplayed() && connectToGoogleView != null;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** click ok button on connect to google form */
    public void clickOnOkButtonOnConnectToGoogleForm() {
        okButtonOnConnectToGoogleView.click();
    }

    /**
     * wait while checkbox is unchecked on google form
     *
     * @param checkBoxNum
     * @throws Exception
     */
    public void waitCheckboxIsCheckedGoogleForm(final String checkBoxNum) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    WebElement box =
                            driver().findElement(
                                    By.xpath(String.format(Locators.INVITE_CHECK_BOX_GOOGLE, checkBoxNum)));

                    return box.isSelected();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * wait while chekbox in form unchecked on google form
     *
     * @param checkBoxNum
     */
    public void waitChekBoxUnchekedGoogleForm(final String checkBoxNum) {
        WebElement elem = driver().findElement(By.xpath(String.format(Locators.INVITE_CHECK_BOX_GOOGLE, checkBoxNum)));
        (new WebDriverWait(driver(), 30)).until(ExpectedConditions.elementSelectionStateToBe(elem, false));
    }

    /**
     * click on check box by index google form
     *
     * @param checkBoxNum
     */
    public void clickOnCheckBoxGoogleForm(String checkBoxNum) {
        WebElement box = driver().findElement(By.xpath(String.format(Locators.INVITE_CHECK_BOX_GOOGLE, checkBoxNum)));
        box.click();
    }

    /**
     * wait while checkbox is unchecked on github form
     *
     * @param userMail
     * @throws Exception
     */
    public void waitCheckboxIsCheckedGithubForm(final String userMail) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    WebElement box =
                            driver().findElement(
                                    By.xpath(String.format(Locators.INVITE_CHECK_BOX_GITHUB, userMail)));

                    return box.isSelected();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * wait while chekbox in form unchecked on github form
     *
     * @param userMail
     */
    public void waitChekBoxUnchekedGithubForm(final String userMail) {
        WebElement elem = driver().findElement(By.xpath(String.format(Locators.INVITE_CHECK_BOX_GITHUB, userMail)));
        (new WebDriverWait(driver(), 30)).until(ExpectedConditions.elementSelectionStateToBe(elem, false));
    }

    /**
     * click on check box by index github form
     *
     * @param userMail
     */
    public void clickOnCheckBoxGithubForm(String userMail) {
        WebElement box = driver().findElement(By.xpath(String.format(Locators.INVITE_CHECK_BOX_GITHUB, userMail)));
        box.click();
    }

    /** Check that invite button is enabled */
    public boolean isInviteButtonEnabled() {
        return invBtn.isEnabled();
    }

    /** Check that invite button is disabled */
    public boolean isInviteButtonDisabled() {
        return invBtn.isEnabled();
    }

    /** Click on cancel button; */
    public void clickOnCancelButton() {
        cancelBtn.click();
    }

    /** click on show gmail contacts button */
    public void clickOnShowGmailContactsButton() {
        showGmailContactsButton.click();
    }

    /** click on show gmail contacts button */
    public void clickOnAddAMessageButton() {
        addAMessageButton.click();
    }

    /** wait area for typing message */
    public void waitMessageInputTextarea() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.INVITATION_MESSAGE)));
    }

}
