/*
 *
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2014] Codenvy, S.A.
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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Zaryana Dombrovskaya */
public class GerritHub extends AbstractTestModule {

    public GerritHub(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String GERRITHUB_SININ_BUTTON           = "//span[text()='Sign In']";
        //        String GERRITHUB_GITHUB_SININ_BUTTON = "//a[text()='GitHub Sign-in']";
        String GERRITHUB_PROFILE                = "//div[@class='menuBarUserNamePanel']";
        String GERRITHUB_PROFILE_SETTINGS       = "//a[text()='Settings']";
        String GERRITHUB_HTTP_PASSWORD          = "//a[text()='HTTP Password']";
        String GERRITHUB_HTTP_PASSWORD_USERNAME = "//div[@class='accountUsername']";
        String GERRITHUB_HTTP_PASSWORD_PASSWORD = "//div[@class='accountPassword']";
        String GERRITHUB_PROJECT                = "//div[text()='Projects']";
        String GERRITHUB_PROJECT_LIST           = "//a[text()='List']";
        String GERRITHUB_PROJECT_SEARCH         = "//input[@class='gwt-TextBox']";
        String GERRITHUB_PROJECT_NAME           = "//b[text()='exoinvitemain/jsTestProject']";
        String GERRITHUB_PROJECT_URL            = "//span[@class='com-google-gwtexpui-clippy-client-ClippyCss-label']";
        String GERRITHUB_MY                     = "//div[text()='My']";
        String GERRITHUB_MY_CHANGES             = "//a[text()='Changes']";
        String GERRITHUB_CHANGE_REPLY           = "//div[text()='Reply…']";
        String GERRITHUB_REVIEW_TEXT            = "//label[text()='send email']//..//..//..//textarea";
        String GERRITHUB_CODE_REVIEW            =
                "//label[text()='send email']//..//..//..//tbody//td[text()='Code-Review']//..//td[6]//input";
        String GERRITHUB_VERIFIED               =
                "//label[text()='send email']//..//..//..//tbody//td[text()='Verified']//..//td[5]//input";
        String GERRITHUB_POST                   = "//button/div[text()='Post']";
        String GERRITHUB_MERGE_CHANGE           = "//button/div[text()='Merge Change']";
        String GERRITHUB_HISTORY_ROW_1          = "//div[text()='Uploaded patch set 1.']";
        String GERRITHUB_HISTORY_ROW_2          = "//div[text()='Patch Set 1: Code-Review+2 Verified+1\n\nAwesome!']";
        String GERRITHUB_HISTORY_ROW_3          = "//div[text()='Change has been successfully merged into the git repository.']";
        String GERRITHUB_GITHUB_CHANGE_LINK     = "//a[text()='(GitHub)']";
    }

    @FindBy(xpath = Locators.GERRITHUB_SININ_BUTTON)
    private WebElement gerritHubSingInButton;

//    @FindBy(xpath = Locators.GERRITHUB_GITHUB_SININ_BUTTON)
//    private WebElement gerritHubGitHubSingInButton;

    @FindBy(xpath = Locators.GERRITHUB_PROFILE)
    private WebElement gerritHubProfile;

    @FindBy(xpath = Locators.GERRITHUB_PROFILE_SETTINGS)
    private WebElement gerritHubProfileSettings;

    @FindBy(xpath = Locators.GERRITHUB_HTTP_PASSWORD)
    private WebElement gerritHubHttpPassword;

    @FindBy(xpath = Locators.GERRITHUB_HTTP_PASSWORD_USERNAME)
    private WebElement gerritHubHttpPasswordUsername;

    @FindBy(xpath = Locators.GERRITHUB_HTTP_PASSWORD_PASSWORD)
    private WebElement gerritHubHttpPasswordPassword;

    @FindBy(xpath = Locators.GERRITHUB_PROJECT)
    private WebElement gerritHubProject;

    @FindBy(xpath = Locators.GERRITHUB_PROJECT_LIST)
    private WebElement gerritHubProjectList;

    @FindBy(xpath = Locators.GERRITHUB_PROJECT_SEARCH)
    private WebElement gerritHubProjectSearch;

    @FindBy(xpath = Locators.GERRITHUB_PROJECT_NAME)
    private WebElement gerritHubProjectName;

    @FindBy(xpath = Locators.GERRITHUB_PROJECT_URL)
    private WebElement gerritHubProjectUrl;

    @FindBy(xpath = Locators.GERRITHUB_MY)
    private WebElement gerritHubMy;

    @FindBy(xpath = Locators.GERRITHUB_MY_CHANGES)
    private WebElement gerritHubMyChanges;

    @FindBy(xpath = Locators.GERRITHUB_CHANGE_REPLY)
    private WebElement gerritHubChangeReply;

    @FindBy(xpath = Locators.GERRITHUB_REVIEW_TEXT)
    private WebElement gerritHubReviewText;

    @FindBy(xpath = Locators.GERRITHUB_CODE_REVIEW)
    private WebElement gerritHubCodeReview;

    @FindBy(xpath = Locators.GERRITHUB_VERIFIED)
    private WebElement gerritHubReviewVerified;

    @FindBy(xpath = Locators.GERRITHUB_POST)
    private WebElement gerritHubReviewPost;

    @FindBy(xpath = Locators.GERRITHUB_MERGE_CHANGE)
    private WebElement gerritHubMergeChangeButton;

    @FindBy(xpath = Locators.GERRITHUB_HISTORY_ROW_1)
    private WebElement gerritHubHistoryRow1;

    @FindBy(xpath = Locators.GERRITHUB_HISTORY_ROW_2)
    private WebElement gerritHubHistoryRow2;

    @FindBy(xpath = Locators.GERRITHUB_HISTORY_ROW_3)
    private WebElement gerritHubHistoryRow3;

    @FindBy(xpath = Locators.GERRITHUB_GITHUB_CHANGE_LINK)
    private WebElement gerritHubGithubChangeLink;

    /** wait singin button on GerritHub page */
    public void waitGerritSingInButton() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubSingInButton));
    }

    /** sing in to GerritHub */
    public void signInToGerritHub(String userName, String userPass) throws Exception {
        waitGerritSingInButton();
        gerritHubSingInButton.click();
//        waitGerritHubGitHubSingInButton();
//        gerritHubGitHubSingInButton.click();
        IDE ideInst = IDE();

        IDE().GITHUB.waitAuthorizationPageOpened();
        IDE().GITHUB.typeLogin(userName);
        IDE().GITHUB.typePass(userPass);
        IDE().GITHUB.clickOnSignInButton();
        if (new GitHub(ideInst).checkExistAutorizedAppByName("GerritHub")) {
            IDE().GERRITHUB.waitGerritHubProfileLink();
        }
        else {
            IDE().GITHUB.clickOnAuthorizeBtn();
            IDE().GERRITHUB.waitGerritHubProfileLink();
        }


    }

    /** wait profile on GerritHub page */
    public void waitGerritHubProfileLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubProfile));
    }

    /** wait profile settings on GerritHub page */
    public void waitGerritHubProfileSettingsLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubProfileSettings));
    }

    /** wait http password menu on GerritHub page */
    public void waitGerritHubHttpPasswordLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubHttpPassword));
    }

    /** wait username on http password menu */
    public void waitGerritHubHttpPasswordUsername() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubHttpPasswordUsername));
    }

    /** wait password on http password menu */
    public void waitGerritHubHttpPasswordPassword() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubHttpPasswordPassword));
    }

    /** wait project link on GerritHub page */
    public void waitGerritHubProjectLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubProject));
    }

    /** wait project list link on GerritHub page */
    public void waitGerritHubProjectListLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubProjectList));
    }

    /** wait project search input on GerritHub page */
    public void waitGerritHubProjectSearchInput() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubProjectSearch));
    }


    /** wait project clone link on GerritHub page */
    public void waitGerritHubProjectCloneLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubProjectUrl));
    }

    /** click on profile on GerritHub page */
    public void clickGerritHubProfileLink() {
        waitGerritHubProfileLink();
        gerritHubProfile.click();
    }

    /** click on profile settings on GerritHub page */
    public void clickGerritHubProfileSettings() {
        waitGerritHubProfileSettingsLink();
        gerritHubProfileSettings.click();
    }

    /** click on http password link on GerritHub page */
    public void clickGerritHubhttpPasswordLink() {
        waitGerritHubHttpPasswordLink();
        gerritHubHttpPassword.click();
    }

    /** click on http password username on GerritHub page */
    public String getGerritHubhttpPasswordUsername() {
        waitGerritHubHttpPasswordUsername();
        return gerritHubHttpPasswordUsername.getText();
    }

    /** click on http password password on GerritHub page */
    public String getGerritHubhttpPasswordPassword() {
        waitGerritHubHttpPasswordPassword();
        return gerritHubHttpPasswordPassword.getText();
    }

    /** click on project link on GerritHub page */
    public void clickGerritHubProjectLink() {
        waitGerritHubProjectLink();
        gerritHubProject.click();
    }

    /** click on project list link on GerritHub page */
    public void clickGerritHubProjectListLink() {
        waitGerritHubProjectListLink();
        gerritHubProjectList.click();
    }

    /** search project on GerritHub page */
    public void typeGerritHubProjectSearch(String projectName) throws Exception {
        waitGerritHubProjectSearchInput();
        gerritHubProjectSearch.sendKeys(projectName);
    }

    /** wait project name on GerritHub page */
    public void waitGerritHubProjectName() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubProjectName));
    }

    public void clickGerritHubProjectCloneLink(String projectName) {
        waitGerritHubProjectName();
        driver().findElement(By.partialLinkText(projectName)).click();
    }

    /** get project clone link on GerritHub page */
    public String getGerritHubProjectCloneUrl() {
        waitGerritHubProjectCloneLink();
        return gerritHubProjectUrl.getText();
    }


    /** wait "My" on GerritHub page */
    public void waitGerritHubMy() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubMy));
    }

    /** wait "My->Changes" on GerritHub page */
    public void waitGerritHubChanges() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubMyChanges));
    }

    /** click on "My" on GerritHub page */
    public void clickGerritHubMy() {
        waitGerritHubMy();
        gerritHubMy.click();
    }

    /** click on "My->Changes" on GerritHub page */
    public void clickGerritHubMyChanges() {
        waitGerritHubChanges();
        gerritHubMyChanges.click();
    }

    /** click on Commit on GerritHub page */
    public void clickGerritHubCommit(String commitName) {
        driver().findElement(By.partialLinkText(commitName)).click();
    }

    /** wait change reply on GerritHub page */
    public void waitGerritHubChangeReply() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubChangeReply));
    }

    /** click on Reply on GerritHub page */
    public void clickGerritHubChangeReply() {
        waitGerritHubChangeReply();
        gerritHubChangeReply.click();
    }

    /** wait change reply on GerritHub page */
    public void waitGerritHubReviewText() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubReviewText));
    }

    /** click on Reply on GerritHub page */
    public void typeGerritHubReviewText() {
        waitGerritHubReviewText();
        gerritHubReviewText.sendKeys("Awesome!");
    }

    /** wait code review on GerritHub page */
    public void waitGerritHubCodeReview() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubCodeReview));
    }

    /** click on code review +2 GerritHub page */
    public void clickGerritHubCodeReview() {
        waitGerritHubCodeReview();
        gerritHubCodeReview.click();
    }

    /** wait verified review on GerritHub page */
    public void waitGerritHubReviewVerified() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubReviewVerified));
    }

    /** click on verified review on GerritHub page */
    public void clickGerritHubVerifiedReview() {
        waitGerritHubReviewVerified();
        gerritHubReviewVerified.click();
    }

    /** wait post button on GerritHub page */
    public void waitGerritHubReviewPostButton() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubReviewPost));
    }

    /** click on Post review on GerritHub page */
    public void clickGerritHubReviewPostButton() {
        waitGerritHubReviewPostButton();
        gerritHubReviewPost.click();
    }

    /** wait merge change button on GerritHub page */
    public void waitGerritHubMergeChangeButton() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubMergeChangeButton));
    }

    /** click on Merge Change button on GerritHub page */
    public void clickGerritHubMergeChangeButton() {
        waitGerritHubMergeChangeButton();
        gerritHubMergeChangeButton.click();
    }

    /** wait in history row 1 on GerritHub page */
    public void waitGerritHubHistoryRow1() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubHistoryRow1));
    }

    /** wait in history row 2 on GerritHub page */
    public void waitGerritHubHistoryRow2() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubHistoryRow2));
    }

    /** wait in history row 3 on GerritHub page */
    public void waitGerritHubHistoryRow3() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubHistoryRow3));
    }

    /** wait GitHub link with change on GerritHub page */
    public void waitGerritHubGithubChangeLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(gerritHubGithubChangeLink));
    }

    /** click on GitHub link with change on GerritHub page */
    public void clickGerritHubGithubChangeLink() {
        waitGerritHubGithubChangeLink();
        gerritHubGithubChangeLink.click();
    }
}