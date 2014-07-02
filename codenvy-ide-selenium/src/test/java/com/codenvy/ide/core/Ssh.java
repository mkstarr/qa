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

public class Ssh extends AbstractTestModule {
    /** @param ide */
    public Ssh(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {

        String SSH_MANAGER_GENERATE_KEY_BUTTON_ID = "ideSshKeyManagerGenerateButton";

        String SSH_MANAGER_UPLOAD_KEY_BUTTON_ID = "ideSshKeyManagerUploadButton";

        String SSH_MANAGER_FORM_ID = "div[view-id='ideSshKeyManagerView']";

        String HOST_COLUMN = "//table[@id='ideSshKeysGrid']//th[text()='Host']";

        String PUBLIC_KEY_COLUMN = "//table[@id='ideSshKeysGrid']//th[text()='Public Key']";

        String PUBLIC_DELETE_COLUMN = "//table[@id='ideSshKeysGrid']//th[text()='Delete']";

        String ASK_SSH_KEY_FORM = "codenvyAskForValueModalView-window";

        String ASK_SSH_FIELD_FORM = "valueField";

        String ASK_SSH_OKBUTTON = "OkButton";

        String ASK_SSH_CANCELBUTTON = "CancelButton";

        String SSH_KEYS_LIST_TABLE = "//table[@id='ideSshKeysGrid']";

        String SSH_KEYS_LIST_GET_KEY_NUM_ORDER = "//table[@id='ideSshKeysGrid']/tbody/tr[%s]";

        String SSH_KEYS_VIEW_KEY_NUM_GRID = "//table[@id='ideSshKeysGrid']//div[text()='%s']/../..//u[text()='View']";

        String SSH_KEYS_DELETE_KEY_NUM_GRID = "//table[@id='ideSshKeysGrid']//div[text()='%s']/../..//u[text()='Delete']";

        String SSH_PUBLIC_KEY_FORM = "ideSshPublicKeyView-window";

        String SSH_KEY_MANAGER_CLOSE_BTN = "ideSshKeyManagerCloseButton";

        String GENERATE_AND_UPLOAD_TO_GITHUB_BUTTON_ID = "ideSshKeyManagerGenerateGithubKeyButton";

        String UPLOAD_SSH_KEY_CONFIRM_FORM = "//div[@view-id='ideGenerateGitHubSshKeyView']";

        String UPLOAD_SSH_KEY_OK_BUTTON_ID = "ideGenerateGitHubSshKeyViewOkButton";

        String UPLOAD_SSH_KEY_CANCEL_BUTTON_ID = "ideGenerateGitHubSshKeyViewCancelButton";

        String UPLOAD_SSH_KEY_FORM = "//div[@view-id='ideUploadSshKeyView']";

        String UPLOAD_SSH_KEY_HOST_INPUT = "ideUploadSshKeyHostField";

        String UPLOAD_SSH_KEY_PATH_TO_FILE = "ideUploadSshKeyFileNameField";
    }

    @FindBy(css = Locators.SSH_MANAGER_FORM_ID)
    private WebElement ssForm;

    @FindBy(xpath = Locators.HOST_COLUMN)
    private WebElement hostCol;

    @FindBy(xpath = Locators.PUBLIC_KEY_COLUMN)
    private WebElement publicCol;

    @FindBy(xpath = Locators.PUBLIC_DELETE_COLUMN)
    private WebElement deleteCol;

    @FindBy(id = Locators.SSH_MANAGER_GENERATE_KEY_BUTTON_ID)
    private WebElement generateBtn;

    @FindBy(id = Locators.SSH_MANAGER_UPLOAD_KEY_BUTTON_ID)
    private WebElement uploadBtn;

    @FindBy(id = Locators.ASK_SSH_KEY_FORM)
    private WebElement askSshForm;

    @FindBy(name = Locators.ASK_SSH_FIELD_FORM)
    private WebElement askSshField;

    @FindBy(id = Locators.ASK_SSH_OKBUTTON)
    private WebElement askSshokBtn;

    @FindBy(id = Locators.ASK_SSH_CANCELBUTTON)
    private WebElement askSshCancelBtn;

    @FindBy(xpath = Locators.SSH_KEYS_LIST_TABLE)
    private WebElement listKeys;

    @FindBy(id = Locators.SSH_PUBLIC_KEY_FORM)
    private WebElement sshForm;

    @FindBy(id = Locators.SSH_KEY_MANAGER_CLOSE_BTN)
    private WebElement closeSshManagerBtn;

    @FindBy(id = Locators.GENERATE_AND_UPLOAD_TO_GITHUB_BUTTON_ID)
    private WebElement generateAndUploadBtn;

    @FindBy(id = Locators.UPLOAD_SSH_KEY_OK_BUTTON_ID)
    private WebElement uploadSshKeyOkBtn;

    @FindBy(id = Locators.UPLOAD_SSH_KEY_CANCEL_BUTTON_ID)
    private WebElement uploadSshKeyCancelBtn;

    @FindBy(name = Locators.UPLOAD_SSH_KEY_HOST_INPUT)
    private WebElement uploadSshHostName;

    @FindBy(name = Locators.UPLOAD_SSH_KEY_PATH_TO_FILE)
    private WebElement uploadSshPathToFile;


    /** wait appearance Ssh key form */
    public void waitSshView() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return ssForm != null && ssForm.isDisplayed() && hostCol != null && hostCol.isDisplayed()
                           && publicCol != null && publicCol.isDisplayed() && deleteCol != null &&
                           deleteCol.isDisplayed()
                           && generateBtn != null && generateBtn.isDisplayed() && uploadBtn != null &&
                           uploadBtn.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** wait appearance ask form for generate a new ssh key */
    public void waitSshAskForm() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return askSshForm != null && askSshForm.isDisplayed() && askSshField != null
                           && askSshField.isDisplayed() && askSshokBtn != null && askSshokBtn.isDisplayed()
                           && askSshCancelBtn != null && askSshCancelBtn.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** wait ask form for generate a new ssh key closed */
    public void waitSshAskFormClose() {
        (new WebDriverWait(driver(), 30)).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                        .id(Locators


                                                                                                                    .ASK_SSH_KEY_FORM)));
    }

    /** wait ssh grid is not empty */
    public void waitAppearContentInSshListGrig(final String keyName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return listKeys.getText().contains(keyName);
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    public boolean isKeyPresent(String keyName) {
        return listKeys.getText().contains(keyName);
    }

    /** wait ssh grid is not empty */
    public void waitDisappearContentInSshListGrig(final String keyName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return !listKeys.getText().contains(keyName);
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    /** wait closed ssh key manadger form */
    public void waitAppearSshKeyManadger() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return sshForm != null && sshForm.isDisplayed();
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    /** wait appearance ssh key manadger form */
    public void waitCloseSshKeyManadger() {
        (new WebDriverWait(driver(), 30)).until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.SSH_PUBLIC_KEY_FORM)));
    }

    /**
     * write new web host into field the generate ssh key form
     *
     * @param host
     */
    public void typeHostToSshAsk(String host) {
        askSshField.sendKeys(host);
    }

    /** click on ok button in generate ssh key form */
    public void cliclOkBtnSshAsk() {
        askSshokBtn.click();
    }

    /** click on ok button in generate ssh key form */
    public void cliclCancelBtnSshAsk() {
        askSshCancelBtn.click();
    }

    /** click on generate button in ssh keys */
    public void clickGenerateBtn() {
        generateBtn.click();
    }

    /** get all text from Ssh Keys Grid */
    public String getAllKeysList() {
        return listKeys.getText();
    }

    /** get row in ssh table with set number */
    public String getNumRowInKeysList(int num) {
        return driver().findElement(By.xpath(String.format(Locators.SSH_KEYS_LIST_GET_KEY_NUM_ORDER, num))).getText();
    }

    /**
     * Clicks on View link in Ssh Key grid with set num position num start with 1
     *
     * @param keyName
     *         key name
     */
    public void clickViewKeyInGridPosition(String keyName) {
        driver().findElement(By.xpath(String.format(Locators.SSH_KEYS_VIEW_KEY_NUM_GRID, keyName))).click();
    }

    /**
     * Clicks on delete link in Ssh Key grid with set num position num start with 1
     *
     * @param keyName
     *         key name
     */
    public void clickDeleteKeyInGridPosition(String keyName) {
        driver().findElement(By.xpath(String.format(Locators.SSH_KEYS_DELETE_KEY_NUM_GRID, keyName))).click();
    }

    /** get full text from ssh key manager */
    public String getSshKeyHash() {
        return sshForm.findElement(By.tagName("input")).getAttribute("value");
    }

    /** click on close btn ssh key manager */
    public void clickOnCloseSshKeyManager() {
        closeSshManagerBtn.click();
    }

    /** click on generate and upload button */
    public void clickOnGenerateAndUploadButton() {
        generateAndUploadBtn.click();
    }

    /** wait ssh upload key confirm form */
    public void waitSshConfirmUploadForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.UPLOAD_SSH_KEY_CONFIRM_FORM)));
    }

    /** wait ssh upload key confirm form disappear */
    public void waitSshConfirmUploadFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.UPLOAD_SSH_KEY_CONFIRM_FORM)));
    }

    /** click ok on confirm upload ssh key form */
    public void clickOnUploadKeyOkButton() {
        uploadSshKeyOkBtn.click();
        waitSshConfirmUploadFormDisappear();
    }

    /**
     * check is upload ssh key form displayed
     *
     * @return
     */
    public boolean isUploadKeyFormVisible() {
        try {
            WebElement element = driver().findElement(By.xpath(Locators.UPLOAD_SSH_KEY_CONFIRM_FORM));
            return element.isDisplayed();
        } catch (Exception e) {
            System.err.println("Element not found");
            return false;
        }
    }

    /** click Cancel on confirm upload ssh key form */
    public void clickOnUploadKeyCancelButton() {
        uploadSshKeyCancelBtn.click();
        waitSshConfirmUploadFormDisappear();
    }

    /** click on upload ssh key button */
    public void clickOnUploadSshkeyButton() {
        uploadBtn.click();
    }

    /** wait ssh upload key form */
    public void waitSshUploadForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.UPLOAD_SSH_KEY_FORM)));
    }

    /** wait ssh upload key form disappear */
    public void waitSshUploadFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.UPLOAD_SSH_KEY_FORM)));
    }

    /**
     * type host name in upload ssh form
     *
     * @param hostName
     */
    public void typeHostNameInUploadSshForm(String hostName) {
        uploadSshHostName.clear();
        uploadSshHostName.sendKeys(hostName);
    }

    /**
     * type path to file in upload ssh form
     *
     * @param path
     */
    public void typePathToFileInUploadSshForm(String path) {
        //uploadSshPathToFile.clear();
        uploadSshPathToFile.sendKeys(path);
    }

}
