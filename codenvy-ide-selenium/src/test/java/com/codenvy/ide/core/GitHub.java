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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.Utils;

import org.everrest.core.impl.provider.json.JsonValue;
import org.exoplatform.ide.commons.ParsingResponseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** The Object containing GitHubAuthPage page`s webelenents */
public class GitHub extends AbstractTestModule

{
    /** @param ide */
    public GitHub(com.codenvy.ide.IDE ide) {
        super(ide);
    }


    private class GitHubApiUtils {


        private String apiUrlToUser    = "https://api.github.com/user";
        private String apiPrefixToKeys = apiUrlToUser + "/users/";
        private String owner;
        private String keyListUrl;
        private String delkeyListUrl = "https://api.github.com/user/keys";
        private String tockenUrl     = "https://api.github.com/authorizations";


        GitHubApiUtils() {
            owner = BaseTest.USER_NAME.substring(0, BaseTest.USER_NAME.indexOf("@"));
            keyListUrl = "https://api.github.com/users/" + owner + "/keys";
        }

        /**
         * return list public keys from git hub test repo
         *
         * @return list of pudlic ssh keys
         * @throws IOException
         * @throws ParsingResponseException
         */
        private List<String> getPublicKeysWithGithubApi() throws IOException, ParsingResponseException {
            ///users/:user/keys
            List<String> keyList = new ArrayList<String>();
            BufferedReader br = null;
            StringBuilder responceKeys = new StringBuilder();
            String line = null;
            URL url = new URL(keyListUrl);
            HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
            hpcon.setRequestMethod("GET");
            if (hpcon.getResponseCode() != 200) {
                throw new RuntimeException("Can not create connection with Github or service is unavailable");
            }
            br = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));

            while ((line = br.readLine()) != null) {
                responceKeys.append(line);
            }
            System.out.println("********API_GITHUB****received**Keys**************:" + responceKeys.toString());
            hpcon.getInputStream().close();
            hpcon.disconnect();
            Iterator<JsonValue> iter = org.exoplatform.ide.commons.JsonHelper.parseJson(responceKeys.toString()).getElements();
            while (iter.hasNext()) {
                keyList.add(iter.next().getElement("id").getStringValue());
            }
            return keyList;
        }

        /**
         * check available public keys on test repo and delete if exist
         *
         * @throws IOException
         * @throws ParsingResponseException
         */
        private void checkAndDeletePublicKeys() throws IOException, ParsingResponseException {
            List<String> receivedKeys = getPublicKeysWithGithubApi();
            if (receivedKeys.size() > 0) {
                for (String receivedKey : receivedKeys) {
                    URL url = new URL(delkeyListUrl + "/" + receivedKey);
                    HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
                    String userpass = BaseTest.USER_NAME + ":" + BaseTest.USER_PASSWORD;
                    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
                    hpcon.setRequestProperty("Authorization", basicAuth);
                    hpcon.setRequestMethod("DELETE");
                    System.out.println("********API_GITHUB-deleted-ssh-keys********************" + hpcon.getResponseCode());
                    hpcon.disconnect();
                }

            }
        }

        /**
         * check exist tokens on github side in the ide test - repository
         *
         * @return List of tokens from test repository
         * @throws IOException
         * @throws ParsingResponseException
         */
        private List<String> checkPresentGithubTocken() throws IOException, ParsingResponseException {
            StringBuilder autorizedApps = new StringBuilder();
            BufferedReader br = null;
            String line = null;
            List<String> keyList = new ArrayList<String>();
            URL url = new URL(tockenUrl);
            HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
            String userpass = BaseTest.USER_NAME + ":" + BaseTest.USER_PASSWORD;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            hpcon.setRequestProperty("Authorization", basicAuth);
            hpcon.setRequestMethod("GET");
            if (hpcon.getResponseCode() != 200) {
                throw new RuntimeException("Can not create connection with Github or service is unavailable");
            }

            br = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));

            while ((line = br.readLine()) != null) {
                autorizedApps.append(line);
            }

            Iterator<JsonValue> iter = org.exoplatform.ide.commons.JsonHelper.parseJson(autorizedApps.toString()).getElements();
            while (iter.hasNext()) {
                keyList.add(iter.next().getElement("id").getStringValue());
            }
            return keyList;
        }


        /**
         * @param nameApp
         *         name of the authorized application on git hub side with oAuch
         * @return true if authorized application exist
         */
        private void checkExistAndDeleteAutorizeAppByName(String nameApp) throws IOException, ParsingResponseException {
            boolean ifExist = false;
            StringBuilder autorizedApps = new StringBuilder();
            BufferedReader br = null;
            String line = null;
            List<String> keyList = new ArrayList<String>();
            URL url = new URL(tockenUrl);
            HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
            String userpass = BaseTest.USER_NAME + ":" + BaseTest.USER_PASSWORD;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            hpcon.setRequestProperty("Authorization", basicAuth);
            hpcon.setRequestMethod("GET");
            if (hpcon.getResponseCode() != 200) {
                throw new RuntimeException("Can not create connection with Github or service is unavailable");
            }

            br = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));

            while ((line = br.readLine()) != null) {
                autorizedApps.append(line);
            }

            Iterator<JsonValue> iter = org.exoplatform.ide.commons.JsonHelper.parseJson(autorizedApps.toString()).getElements();
            Iterator<JsonValue> iter2 = org.exoplatform.ide.commons.JsonHelper.parseJson(autorizedApps.toString()).getElements();
            while (iter.hasNext()) {
                JsonValue val = iter.next().getElement("app");
                String id = iter2.next().getElement("id").getStringValue();
                if (val.getElement("name").toString().replace("\"", "").equals(nameApp)) {
                    deleteTokenById(id);
                }
            }
        }


        /**
         * @param nameApp
         *         check exist authorized application on github test repository
         * @return true if authorized application exist
         */
        private boolean checkExistAutorizeAppByName(String nameApp) throws IOException, ParsingResponseException {
            boolean ifExist = false;
            StringBuilder autorizedApps = new StringBuilder();
            BufferedReader br = null;
            String line = null;
            List<String> keyList = new ArrayList<String>();
            URL url = new URL(tockenUrl);
            HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
            String userpass = BaseTest.USER_NAME + ":" + BaseTest.USER_PASSWORD;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            hpcon.setRequestProperty("Authorization", basicAuth);
            hpcon.setRequestMethod("GET");
            if (hpcon.getResponseCode() != 200) {
                throw new RuntimeException("Can not create connection with Github or service is unavailable");
            }

            br = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));

            while ((line = br.readLine()) != null) {
                autorizedApps.append(line);
            }

            Iterator<JsonValue> iter = org.exoplatform.ide.commons.JsonHelper.parseJson(autorizedApps.toString()).getElements();
            Iterator<JsonValue> iter2 = org.exoplatform.ide.commons.JsonHelper.parseJson(autorizedApps.toString()).getElements();
            while (iter.hasNext()) {
                JsonValue val = iter.next().getElement("app");
                if (val.getElement("name").toString().replace("\"", "").equals(nameApp)) {
                    ifExist = true;
                }
            }
            return ifExist;
        }


        /**
         * check exist tockens on github test repo. Delete if exist
         *
         * @throws IOException
         * @throws ParsingResponseException
         */
        public void checkAhdDeleteTockensApp() throws IOException, ParsingResponseException {

            List<String> getTockens = checkPresentGithubTocken();
            if (getTockens.size() > 0) {
                for (String getTocken : getTockens) {
                    URL url = new URL(tockenUrl + "/" + getTocken);
                    HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
                    String userpass = BaseTest.USER_NAME + ":" + BaseTest.USER_PASSWORD;
                    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
                    hpcon.setRequestProperty("Authorization", basicAuth);
                    hpcon.setRequestMethod("DELETE");
                    System.out.println("********API_GITHUB-deleted-tocken********************" + hpcon.getResponseCode());
                    hpcon.disconnect();
                }
            }
        }


        /**
         * delete authorized application on github side by id
         *
         * @throws IOException
         * @throws ParsingResponseException
         */
        public void deleteTokenById(String id) throws IOException, ParsingResponseException {

            URL url = new URL(tockenUrl + "/" + id);
            HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
            String userpass = BaseTest.USER_NAME + ":" + BaseTest.USER_PASSWORD;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            hpcon.setRequestProperty("Authorization", basicAuth);
            hpcon.setRequestMethod("DELETE");
            System.out.println("********API_GITHUB-deleted-tocken-by-id********************" + hpcon.getResponseCode());
            hpcon.disconnect();
        }


        /** remove github ssh key into IDE using REST API */
        private void removeGithubKeyOnIDE() {
            /** remove SSH key for github.com host in the IDE */
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME +
                                  "/ssh-keys/remove?host=github.com&callback=__gwt_jsonp__.P12.onSuccess");
                Utils.getConnection(url);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + connection.getResponseCode());
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
        }


        /**
         * method delete branch from idefetchtest test repository
         * using GitHUb API
         * create for testing fetch feature in the IDE
         *
         * @param name
         *         name the deleted branch
         * @throws IOException
         */
        public void deleteBranch(String name) throws Exception {
            String userpass = owner + ":" + BaseTest.USER_PASSWORD;
            ;
            String gitBranchurl = "https://api.github.com/repos/exoinvitemain/testRepo/git/refs/heads/" + name;
            URL url = new URL(gitBranchurl);
            HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            hpcon.setRequestProperty("Authorization", basicAuth);
            hpcon.setRequestMethod("DELETE");
            if (hpcon.getResponseCode() != 204) {
                throw new RuntimeException("Something went wrong on github side. Cannot remove the requested branch");
            }
            hpcon.disconnect();
        }

        /**
         * get latest commit from test repository
         * using for in the tests for testing fetch operations
         *
         * @throws IOException
         *         max
         */
        public String getshLatestCommit() throws IOException, ParsingResponseException {
            String userpass = owner + ":" + BaseTest.USER_PASSWORD;
            String gitBranchurl = "https://api.github.com/repos/exoinvitemain/testRepo/git/refs";
            String lastCommitId = null;
            BufferedReader br = null;
            URL url = new URL(gitBranchurl);
            String line = null;
            StringBuilder autorizedApps = new StringBuilder();
            HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
            hpcon.setRequestMethod("GET");
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            hpcon.setRequestProperty("Authorization", basicAuth);
            if (hpcon.getResponseCode() != 200) {
                throw new RuntimeException("Can not create connection with Github or service is unavailable");
            }

            br = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));

            while ((line = br.readLine()) != null) {
                autorizedApps.append(line);
            }

            InputStream inputStream = hpcon.getInputStream();
            inputStream.close();
            hpcon.disconnect();
            Iterator<JsonValue> iter = org.exoplatform.ide.commons.JsonHelper.parseJson(autorizedApps.toString()).getElements();
            JsonValue jsonWithIdCommit = null;

            while (iter.hasNext()) {
                JsonValue val = iter.next();
                if (val.getElement("ref").getStringValue().contains("refs/heads/master")) {
                    jsonWithIdCommit = val.getElement("object");
                }

            }
            return jsonWithIdCommit.getElement("sha").toString();
        }


        /**
         * method add new  branch into idefetchtest test repository
         *
         * @param newName
         *         the name of created branch
         * @throws IOException
         */
        public void addNewBranch(String newName) throws IOException, ParsingResponseException {
            String userpass = owner + ":" + BaseTest.USER_PASSWORD;
            String gitBranchurl = "https://api.github.com/repos/exoinvitemain/testRepo/git/refs";
            URL url = new URL(gitBranchurl);
            HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            hpcon.setRequestProperty("Authorization", basicAuth);
            hpcon.setAllowUserInteraction(false);
            hpcon.setDoOutput(true);
            hpcon.setDoInput(true);
            hpcon.setRequestMethod("POST");
            OutputStream output = hpcon.getOutputStream();
            output.write(
                    ("{" + "\"ref\": \"refs/heads/" + newName + "\"," + "\"sha\":" + getshLatestCommit() + "}")
                            .getBytes());
            if (hpcon.getResponseCode() != 201) {
                throw new RuntimeException("Something went wrong on github side. Cannot create the requested branch");
            }
            output.flush();
            output.close();
            hpcon.disconnect();


        }
    }

    private interface Locators {

        String LOGIN_FIELD = "login_field";

        String PASS_FIELD = "password";

        String SIGN_IN_BTN = "input[value='Sign in']";

        String ACCOUNT_SETTINGS = "account_settings";

        String APPLICATIONS_BTN = "//*[@id='site-container']//a[contains(.,'Applications')]";

        String REVOKE_BTN = "//a[contains(.,'envy')]/..//a[@data-method='delete']/span";

        String AUTHORIZE_BUTTON = "//button[@class='button primary' and @type='submit']";

        String LOGOUT_BTN = "logout";

        String SSH_KEYS_BTN = "//*[@id='site-container']//a[contains(.,'SSH Keys')]";

        String DELETE_SSH_KEY_BTN = "//li[contains(.,'%s')]/a[@data-method='delete']";

        String CONFIRM_PASS_INPUT_ID = "//input[@id='confirm-password']";

        String CONFIRM_PASS_BUTTON = "//button[text()='Confirm password']";

        String APPLICATIONS_PAGE = "//h3[text()='Authorized applications']";

        String SSH_PAGE = "//a[@id='add_key_action']";

        String SUDO_PASS_FORM_ID = "sudo_password";
    }

    @FindBy(id = Locators.LOGIN_FIELD)
    public WebElement loginField;

    @FindBy(id = Locators.PASS_FIELD)
    public WebElement passField;

    @FindBy(css = Locators.SIGN_IN_BTN)
    public WebElement signInBtn;

    @FindBy(id = Locators.ACCOUNT_SETTINGS)

    public WebElement accountSettings;

    @FindBy(xpath = Locators.APPLICATIONS_BTN)
    public WebElement applicationsBtn;

    @FindBy(xpath = Locators.REVOKE_BTN)
    public WebElement revokeBtn;

    @FindBy(xpath = Locators.AUTHORIZE_BUTTON)
    public WebElement authorizeBtn;

    @FindBy(id = Locators.LOGOUT_BTN)
    public WebElement logoutBtn;

    @FindBy(xpath = Locators.SSH_KEYS_BTN)
    public WebElement sshKeysBtn;

    @FindBy(xpath = Locators.CONFIRM_PASS_INPUT_ID)
    public WebElement confirmPassInput;

    @FindBy(xpath = Locators.CONFIRM_PASS_BUTTON)
    public WebElement confirmPassBtn;

    @FindBy(id = Locators.SUDO_PASS_FORM_ID)
    public WebElement sudoPassForm;


    /** Wait web elements for login on google. */
    public void waitAuthorizationPageOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {

                return loginField.isDisplayed() && passField.isDisplayed() && signInBtn.isDisplayed();
            }
        });
    }

    /**
     * type login to login field
     *
     * @param login
     */
    public void typeLogin(String login) {
        loginField.sendKeys(login);
    }

    /**
     * type password to password field
     *
     * @param pass
     */
    public void typePass(String pass) {
        passField.sendKeys(pass);
    }

    /** click on submit btn on github auth form */
    public void clickOnSignInButton() {
        signInBtn.click();
    }

    public void openGithub() throws Exception {
        driver().get("https://github.com/login");
        waitAccountSettingsBtn();
    }

    public void openGithubAndLogin(String login, String pass) throws Exception {
        driver().get("https://github.com/login");
        waitAuthorizationPageOpened();
        typeLogin(login);
        typePass(pass);
        clickOnSignInButton();
        waitAccountSettingsBtn();
    }

    /** wait for appearing account settings button */
    public void waitAccountSettingsBtn() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .id(Locators


                                                                                                                .ACCOUNT_SETTINGS)));
    }


    /** wait for appearing appereance supo password form after click on autorize button form if we have not ssh key on github */
    public void waitSudoPassForm() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(sudoPassForm));
    }

    /** type password to sudo field on github */
    public void typeToSudoPassForm(String pass) {
        sudoPassForm.sendKeys(pass);
    }

    /** click on account settings button */
    public void clickOnAccountSettingsButton() {
        accountSettings.click();
    }

    /** wait for appearing applications button in settings menu. */
    public void waitApplicationsBtn() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators.APPLICATIONS_BTN)));
    }

    /** click on applications button in settings menu. */
    public void clickOnApplicationsButton() {
        applicationsBtn.click();
    }

    /** wait for appearing Revoke button in applications menu. */
    public boolean isRevokeBtnPresent() {
        try {
            WebElement element = driver().findElement(By.xpath(Locators.REVOKE_BTN));
            return element.isDisplayed();
        } catch (Exception e) {
            System.err.println("Element not found!");
            return false;
        }
    }

    /** wait for disappearing Revoke button in applications menu. */
    public void waitRevokeBtnDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .xpath(Locators.REVOKE_BTN)));
    }

    /** click on Revoke button in applications menu. */
    public void clickOnRevokeButton() {
        revokeBtn.click();
    }

    /**
     * Open Github, login, open settings and delete token
     *
     * @throws Exception
     */
    public void deleteGithubToken() throws Exception {
        clickOnAccountSettingsButton();
        waitApplicationsBtn();
        clickOnApplicationsButton();
        if (isConfirmPasswordInputPresent() == true) {
            waitConfirmPasswordInput();
            typeConfirmationPassword(BaseTest.USER_PASSWORD);
            clickConfirmPasswordButton();
        }
        waitApplicationsPage();
        if (isRevokeBtnPresent() == true) {
            clickOnRevokeButton();
            waitRevokeBtnDisappear();
            clickOnLogoutBtn();
        }
    }

    /** wait for authorize button. */
    public void waitAuthorizeBtn() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators.AUTHORIZE_BUTTON)));
    }

    /** click on authorize button */
    public void clickOnAuthorizeBtn() {
        waitAuthorizeBtn();
        authorizeBtn.click();
    }

    /** logout from github */
    public void clickOnLogoutBtn() {
        logoutBtn.click();
    }

    /**
     * Open settings, check and delete SSH key
     *
     * @throws Exception
     */
    public void checkAndDeleteSshKey() throws Exception {
        clickOnAccountSettingsButton();
        waitSshKeysBtn();
        clickOnSshKeysButton();
        waitSshPage();
        if (isDeleteSshKeyButtonPeresent() == true) {
            clickOnDeleteSshKeyButton();
            waitDeleteButtonDisappear();
        }
    }

    /** wait for appearing ssh keys button in settings menu. */
    public void waitSshKeysBtn() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators.SSH_KEYS_BTN)));
    }

    /** click on ssh keys button */
    public void clickOnSshKeysButton() {
        sshKeysBtn.click();
    }

    public boolean isDeleteSshKeyButtonPeresent() throws Exception {
        try {
            WebElement element = driver().findElement(By.xpath(String.format(
                    Locators.DELETE_SSH_KEY_BTN, BaseTest.USER_NAME)));
            return element.isDisplayed();
        } catch (Exception e) {
            System.err.println("Element not found!");
            return false;
        }
    }

    /** wait for appearing delete button in ssh keys menu. */
    public void waitDeleteButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.DELETE_SSH_KEY_BTN, BaseTest.USER_NAME))));
    }

    /** wait for disappearing of delete button in ssh keys menu. */
    public void waitDeleteButtonDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                Locators.DELETE_SSH_KEY_BTN, BaseTest.USER_NAME))));
    }

    /** click on delete ssh key */
    public void clickOnDeleteSshKeyButton() {
        WebElement elem =
                driver().findElement(By.xpath(String.format(Locators.DELETE_SSH_KEY_BTN, BaseTest.USER_NAME)));
        elem.click();
    }

    /** wait for confirm button appear */
    public void waitConfirmPasswordInput() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.CONFIRM_PASS_INPUT_ID)));
    }

    public boolean isConfirmPasswordInputPresent() {
        try {
            WebElement element = driver().findElement(By.xpath(Locators.CONFIRM_PASS_INPUT_ID));
            return element.isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    public void typeConfirmationPassword(String pass) {
        confirmPassInput.sendKeys(pass);
    }

    public void clickConfirmPasswordButton() {
        confirmPassBtn.click();
    }

    /** wait for applications page form */
    public void waitApplicationsPage() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.APPLICATIONS_PAGE)));
    }

    /** wait for ssh page form */
    public void waitSshPage() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.SSH_PAGE)));
    }

    /**
     * check tokens on github side, using github API
     * if there are tokens prent - they will be deleted
     *
     * @throws IOException
     * @throws ParsingResponseException
     */
    public void apiCheckAndDeleteTokens() throws IOException, ParsingResponseException {
        GitHubApiUtils gitHubApiUtils = new GitHubApiUtils();
        gitHubApiUtils.checkAhdDeleteTockensApp();
    }

    /** try get public SSH key list from Github and delete all keys if list is not empty */
    public void apiCheckAndDeleteSshKeys() throws IOException, ParsingResponseException {
        GitHubApiUtils gitHubApiUtils = new GitHubApiUtils();
        gitHubApiUtils.checkAndDeletePublicKeys();
    }


    /** try get public SSH key list from Github and delete all keys if list is not empty */
    public void apiRemoveGithubSShOnIDE() throws IOException, ParsingResponseException {
        GitHubApiUtils gitHubApiUtils = new GitHubApiUtils();
        gitHubApiUtils.removeGithubKeyOnIDE();
    }

    /**
     * return true if  github side contains any token
     *
     * @return
     * @throws IOException
     * @throws ParsingResponseException
     */
    public boolean apicheckPresentTockensInNotPresentOnGithub() throws IOException, ParsingResponseException {
        GitHubApiUtils gitHubApiUtils = new GitHubApiUtils();
        return gitHubApiUtils.checkPresentGithubTocken().isEmpty();
    }


    /**
     * add new branch into 'idefetchtest' repository on github
     * using for check fetch feature in the IDE selenium tests
     *
     * @param newBranch
     *         the name of the creating branch
     * @throws IOException
     * @throws ParsingResponseException
     */
    public void addNewBranchOnGithubUsingApi(String newBranch) throws IOException, ParsingResponseException {
        GitHubApiUtils gitHubApiUtils = new GitHubApiUtils();
        gitHubApiUtils.addNewBranch(newBranch);

    }

    /**
     * delete  branch by name into 'idefetchtest' repository on github
     * using for check fetch feature in the IDE selenium tests
     *
     * @param addedBranch
     *         name of the deleted branch
     */
    public void deleteNewBranch(String addedBranch) throws Exception {
        GitHubApiUtils gitHubApiUtils = new GitHubApiUtils();
        gitHubApiUtils.deleteBranch(addedBranch);

    }

    /**
     * @return latest commit for testRepo project
     *         using special for FetchUpdatesAndMergeRemoteBranchIntoLocalTest
     * @throws Exception
     */
    public String LatestCommit() throws Exception {
        GitHubApiUtils gitHubApiUtils = new GitHubApiUtils();
        return gitHubApiUtils.getshLatestCommit();
    }

    /**
     * check exist of the authorized application and delete from github side if exist
     *
     * @param appName
     * @throws IOException
     * @throws ParsingResponseException
     */
    public void checkExistAutorizedAppAndDeleteByName(String appName) throws IOException, ParsingResponseException {
        GitHubApiUtils gitHubApiUtils = new GitHubApiUtils();
        gitHubApiUtils.checkExistAndDeleteAutorizeAppByName(appName);

    }


    /**
     * check exist of the authorized application on github side
     *
     * @param appName
     * @throws IOException
     * @throws ParsingResponseException
     */
    public boolean checkExistAutorizedAppByName(String appName) throws IOException, ParsingResponseException {
        GitHubApiUtils gitHubApiUtils = new GitHubApiUtils();
        return gitHubApiUtils.checkExistAutorizeAppByName(appName);

    }


}