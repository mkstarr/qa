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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Vitaly Parfonov
 */
public class WelcomePage extends AbstractTestModule {

    /** @param ide */
    public WelcomePage(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {

        String IMPORTFROM_GITHUB_BTN = "//a[text()='Import from GitHub']";

        String CREATE_A_NEW_PROJECT_FROM_SCRATCH = "//a[text()='Create a New Project from Scratch']";

        String DOCUMENTATION_LINK = "//a[text()='Documentation']";

        String SUPPORT_LINK = "//a[text()='Support']";

        String INVITE_PEOPLE_BTN = "//a[text()='Invite People']";

        String CLONE_A_GIT_REPOSITORY = "//a[text()='Clone a Git Repository']";

    }

    @FindBy(xpath = Locators.IMPORTFROM_GITHUB_BTN)
    WebElement importFromGithub;

    @FindBy(xpath = Locators.CREATE_A_NEW_PROJECT_FROM_SCRATCH)
    WebElement createNewProjectFromScratch;

    @FindBy(xpath = Locators.DOCUMENTATION_LINK)
    WebElement documentationButton;

    @FindBy(xpath = Locators.SUPPORT_LINK)
    WebElement supportLink;

    @FindBy(xpath = Locators.INVITE_PEOPLE_BTN)
    WebElement invitePeopleBtn;

    @FindBy(xpath = Locators.CLONE_A_GIT_REPOSITORY)
    WebElement cloneGitRepo;

    /** wait appearance invite people link on welcome tab */
    public void waitInvitePeopleBtn() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(invitePeopleBtn));
    }

    /** wait appearance documentation button  on welcome tab */
    public void waitDocumentationBtn() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(documentationButton));
    }

    /** wait appearance Support link welcome tab */
    public void waitSupportBtn() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(supportLink));
    }

    /** wait opening button 'Import from github' in 'Welcome' tab */
    public void waitImportFromGithubBtn() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(importFromGithub));
    }

    /** wait opening button 'Create a New Project from Scratch' in 'Welcome' tab */
    public void waitCreateNewProjectFromScratchBtn() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(createNewProjectFromScratch));
    }

    /** wait opening button 'Create a New Project from Scratch' in 'Welcome' tab */
    public void waitCloneGitRepositoryBtn() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(cloneGitRepo));
    }

    /** click on import from github btn in IDE */
    public void clickImportFromGithub() {
        importFromGithub.click();
    }

    /** click on Create a New Project from Scratch on welcome page */
    public void clickCreateNewProjectFromScratch() {
        createNewProjectFromScratch.click();
    }

    /** close 'Welcome' tab in IDE */
    public void close() {
        try {
            driver().findElement(By.cssSelector("div[tab-title=\"Welcome\"]")).click();
        } catch (NoSuchElementException e) {
            //Nothing to do Welcome page already closed
        }
    }

    /**
     * wait while 'Welcome' tab in IDE is closed
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        IDE().EDITOR.waitTabNotPresent("Welcome");
    }

    /**
     * click on documentation btn.
     *
     * @throws Exception
     */
    public void clickOnDocumentationBtn() {
        documentationButton.click();
    }

    /**
     * click on support link
     *
     * @throws Exception
     */
    public void clickSupportLink() {
        supportLink.click();
    }

    /**
     * click on support link
     *
     * @throws Exception
     */
    public void clickonInvitePeopleBtn() {
        invitePeopleBtn.click();
    }

    /**
     * click on clone a git repo button
     *
     * @throws Exception
     */
    public void clickOnCloneGitRepoBtn() {
        cloneGitRepo.click();
    }

}
