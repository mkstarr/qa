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

package com.codenvy.ide.core.openshift;

import com.codenvy.ide.IDE;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.core.AbstractTestModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;

/** @author Zaryana Dombrovskaya */

public class ManageApplicationsWindow extends AbstractTestModule {

    private        String emailValue     = "zaryana.dombrovskaya@gmail.com";           //change in future
    private        String namespaceValue = "zaryana2";                             //change in future
    private static int    TIMEOUT        = TestConstants.PAGE_LOAD_PERIOD / 100;

    ManageApplicationsWindow(IDE ide) {
        super(ide);
    }

    interface Locators {
        String APPLICATION_WINDOW_ID   = "ideUserInfoView-window";
        String LOGIN_EMAIL             = "input[name='ideUserInfoViewLoginField']";
        String NAMESPACE               = "input[name='ideUserInfoViewDomainField']";
        String APPLICATIONS_GRID_XPATH = "//*[@id='ide.OpenShift.ApplicationInfo.ListGrid']/tbody[1]";
        String NAME = APPLICATIONS_GRID_XPATH + "/tr[1]/td[2]/div";
        String GIT_URL = APPLICATIONS_GRID_XPATH + "/tr[4]/td[2]/div";
        String PUBLIC_URL = APPLICATIONS_GRID_XPATH + "/tr[3]/td[2]/div";
        String TYPE = APPLICATIONS_GRID_XPATH + "/tr[2]/td[2]/div";
        String CLOSE_BUTTON_ID = "ideUserInfoViewOkButton";
        String LIST_ID = "ideApplicationGrid";
        String ADD_CARTRIDGE_BUTTON = "ideAddCartridgeButton";
        String ADD_CARTRIDGE_WINDOW = "ideAddCartridgeView-window";
        String LIST_OF_CARTRIDGE = "//select";
        String ADD_CARTRIDGE_BUTTON_IN_ADD_CARTRIDGE_WINDOW = "ideAddCartridgeButtonId";
        String CARTRIDGES_TABLE = "ideOpenShiftCartridgeGrid";
        String ASK_DIALOG_CARTRIDGE_DELETE = "ideAskModalView-window";
        String YES_IN_ASK_DIALOG_CARTRIDGE_DELETE = "YesButton";
        String CARTRIDGE_ROW = "//table[@id='ideOpenShiftCartridgeGrid']//div[text()='%s']";
        String CARTRIDGE_DELETE_BUTTON = "//table[@id='ideOpenShiftCartridgeGrid']//div[text()='%s']/../../..//td[6]/div";
    }

    @FindBy(id = Locators.LIST_ID)
    WebElement tree;

    @FindBy(id = Locators.APPLICATION_WINDOW_ID)
    WebElement manageApplicationsWindow;

    @FindBy(css = Locators.LOGIN_EMAIL)
    WebElement loginEmail;

    @FindBy(css = Locators.NAMESPACE)
    WebElement namespace;

    @FindBy(xpath = Locators.NAME)
    WebElement applicationName;

    @FindBy(xpath = Locators.GIT_URL)
    WebElement applicationGitUrl;

    @FindBy(xpath = Locators.PUBLIC_URL)
    WebElement applicationPublicUrl;

    @FindBy(xpath = Locators.TYPE)
    WebElement applicationType;

    @FindBy(id = Locators.CLOSE_BUTTON_ID)
    WebElement closeButton;

    @FindBy(id = Locators.ADD_CARTRIDGE_BUTTON)
    WebElement addCartridgeButton;

    @FindBy(id = Locators.ADD_CARTRIDGE_WINDOW)
    WebElement addCartridgeWindow;

    @FindBy(xpath = Locators.LIST_OF_CARTRIDGE)
    WebElement listOfCartridge;

    @FindBy(id = Locators.ADD_CARTRIDGE_BUTTON_IN_ADD_CARTRIDGE_WINDOW)
    WebElement addCartridgeButtonInAddCartridgeWindow;

    @FindBy(id = Locators.CARTRIDGES_TABLE)
    WebElement cartridgeTree;

    @FindBy(id = Locators.ASK_DIALOG_CARTRIDGE_DELETE)
    WebElement deleteCartridgeDialog;

    @FindBy(id = Locators.YES_IN_ASK_DIALOG_CARTRIDGE_DELETE)
    WebElement yesINAskDialogCartridgeDelete;

    public void waitProjectByName(final String name) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return tree.findElement(By.xpath("tbody//td/div[text()='" + name + "']")).isDisplayed();
            }
        });
    }

    public void selectProjectByName(String name) {
        tree.findElement(By.xpath("tbody//td/div[text()='" + name + "']")).click();
    }

    public void waitApplicationsManagerWindow() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(manageApplicationsWindow));
    }


    /** Wait for login email value */
    public void waitLoginEmail() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(loginEmail));
    }

    public String getEmail() {
        waitLoginEmail();
        return loginEmail.getAttribute("value");
    }


    /** wait for namespace value */
    public void waitNamespace() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(namespace));
    }


    public String getNamespace() {
        waitNamespace();
        return namespace.getAttribute("value");
    }


    public void waitApplicationName() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(applicationName));
    }

    /** get Application NAME */
    public String getApplicationName() {
        waitApplicationName();
        return applicationName.getText();
    }


    public void waitApplicationPublicUrl() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(applicationPublicUrl));
    }

    /** get Application PUBLIC URL */
    public String getApplicationPublicUrl() {
        waitApplicationPublicUrl();
        return applicationPublicUrl.getText();
    }

    public void waitApplicationType() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(applicationType));
    }

    /** get Application TYPE */
    public String getApplicationType() {
        waitApplicationType();
        return applicationType.getText();
    }

    public void waitApplicationGitUrl() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(applicationGitUrl));
    }

    /** get Application STATUS */
    public String getApplicationGitUrl() {
        waitApplicationGitUrl();
        return applicationGitUrl.getText();
    }

    public void checkEmailAndNameSpace() {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT, MenuCommands.PaaS.OpenShift.APPLICATIONS);
        waitApplicationsManagerWindow();
        assertTrue(getEmail().equals(emailValue));
        assertTrue(getNamespace().equals(namespaceValue));
    }

    public void waitCloseApplicationMenu() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(closeButton));
    }


    public void clickCloseApplicationMenu() {
        waitCloseApplicationMenu();
        closeButton.click();
    }

    /** click on "Add cartridge" button in PaaS -> OpenShift -> Applications */
    public void clickAddCartridgeButton() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(addCartridgeButton));
        addCartridgeButton.click();
    }


    /** wait dialog with cartridge list in combobox in dialog */
    public void waitAddCartridgeWindow() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(addCartridgeWindow));
    }

    /** click on combobox with cartridge list in dialog */
    public void clickOnListOfCartridge() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(listOfCartridge));
        listOfCartridge.click();

    }

    /** select cartridge from combobox in dialog */
    public void selectCartridge(String cartridgeName) {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(listOfCartridge.findElement(By.xpath("option[text()='" + cartridgeName + "']")))).click();
    }

    /** click "Add cartridge" button in dialog */
    public void clickAddCartridgeButttonInAddCartridgeWindow() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(addCartridgeButtonInAddCartridgeWindow));
        addCartridgeButtonInAddCartridgeWindow.click();
    }

    /** wait cartridges table in PaaS -> OpenShift -> Applications */
    public void waitCartridgesTable() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(cartridgeTree));
    }

    /** wait cartridge appear table in PaaS -> OpenShift -> Applications */
    public void waitCartridgeAppearInTable(String cartridgeName) {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.CARTRIDGE_ROW, cartridgeName))));
    }

    /** wait cartridge disappear in table in PaaS -> OpenShift -> Applications */
    public void waitCartridgeDisappearInTable(String cartridgeName) {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(Locators.CARTRIDGE_ROW, cartridgeName))));
    }

    /** wait ask dialog cartridge list in dialog */
    public void waitAskDialofDeleteCartridge() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(deleteCartridgeDialog));
    }

    /** click on yes in ask dialog delete cartridge */
    public void clickYesInAskDeleteCartridgeDialog() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(yesINAskDialogCartridgeDelete));
        yesINAskDialogCartridgeDelete.click();
    }

    /** delete cartridge from table in PaaS -> OpenShift -> Applications */
    public void clickDeleteCartridge(String cartridgeName) {
        cartridgeTree.findElement(By.xpath(String.format(Locators.CARTRIDGE_DELETE_BUTTON, cartridgeName))).click();
        waitAskDialofDeleteCartridge();
        clickYesInAskDeleteCartridgeDialog();
        waitCartridgeDisappearInTable(cartridgeName);

    }

}
