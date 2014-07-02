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

import com.codenvy.ide.IDE;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 @author Roman Iuvshin
 *
 */
public class Chat extends AbstractTestModule {
    /** @param ide */
    public Chat(IDE ide) {
        super(ide);
    }

    private interface Locators {
        String CHAT_VIEW_ID = "//div[@view-id='codenvyIdeChat']";

        String CHAT_INPUT = "ideChatInput";

        String CHAT_PARTICIPANTS = "ideParticipants";

        String CHAT_MESSAGES = "ideChatMessages";
    }

    @FindBy(id = Locators.CHAT_INPUT)
    private WebElement chatInput;

    @FindBy(id = Locators.CHAT_MESSAGES)
    private WebElement chatMessages;

    @FindBy(id = Locators.CHAT_PARTICIPANTS)
    private WebElement chatParticipants;

    /** Wait chat window */
    public void waitChatOpened() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators


                                                                                                                   .CHAT_VIEW_ID)));
    }

    /** Wait in chat link with specified Name */
    public void waitLinkInChat(final String nameLink) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                return chatMessages.findElement(By.linkText(nameLink)).isDisplayed();
            }
        });
    }

    /** Wait in chat link with specified Name */
    public void clickOnLinkInChat(final String nameLink) {
        chatMessages.findElement(By.linkText(nameLink)).click();
    }


    /**
     * Type and send message to chat input form
     *
     * @param message
     *         Chat message
     */
    public void typeAndSendMessage(String message) {
        chatInput.sendKeys(message);
        chatInput.sendKeys(Keys.RETURN);
    }

    /**
     * Wait message in chat
     *
     * @param message
     *         message that should appear in chat.
     */
    public void waitWhileMessageAppearInChat(final String message) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                if (chatMessages.getText().contains(message)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * Wait users in chat
     *
     * @param userName
     *         user name
     */
    public void waitChatParticipants(final String userName) {
        new WebDriverWait(driver(), 40).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                if (chatParticipants.getText().contains(userName)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * Click on notification link in chat to open file.
     *
     * @param fileName
     *         file name that need to open
     */
    public void clickOnNotificationLinkToOpenFile(final String fileName) {
        driver().findElement(By.partialLinkText(fileName)).click();
    }


    /**
     * get all participants from current chat
     *
     * @return
     */
    public String getCurrentPartipicant() {
        return chatParticipants.getText();
    }
}
