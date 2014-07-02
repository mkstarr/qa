package com.codenvy.ide.core.heroku;

import com.codenvy.ide.IDE;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class LoginForm extends AbstractTestModule {


    interface LoginFormLocators {
        String ID_FORM = "div[view-id='ideLoginView']";

        String EMAIL_FIELD = "input[name='ideLoginViewEmailField']";

        String PASSWORD_FIELD = "input[name='ideLoginViewPasswordField']";

        String ENABLED_LOGIN_BUTTON = "div#ideLoginViewLoginButton[button-enabled='true']";

        String DISABLED_LOGIN_BUTTON = "div#ideLoginViewLoginButton[button-enabled='false']";

        String CANSEL_BUTTON = "ideLoginViewCancelButton";

        String TRY_AS_DEMO_BTN = "ideLoginDemoViewLoginButton";
    }


    //Login Form Web elements
    @FindBy(css = LoginFormLocators.ID_FORM)
    private WebElement loginForm;

    @FindBy(css = LoginFormLocators.EMAIL_FIELD)
    private WebElement emailField;

    @FindBy(css = LoginFormLocators.PASSWORD_FIELD)
    private WebElement passwordField;

    @FindBy(css = LoginFormLocators.ENABLED_LOGIN_BUTTON)
    private WebElement enabledLoginBtn;

    @FindBy(css = LoginFormLocators.DISABLED_LOGIN_BUTTON)
    private WebElement disabledLoginBtn;

    @FindBy(id = LoginFormLocators.CANSEL_BUTTON)
    private WebElement cancelLoginBtn;

    @FindBy(id = LoginFormLocators.TRY_AS_DEMO_BTN)
    private WebElement tryAsDemoBtn;


    LoginForm(IDE ide) {
        super(ide);
    }


    /** wait appearance Heroku login form */
    public void waitLoginForm() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(loginForm));
    }

    /** wait disappearence Heroku login form */
    public void waitLoginFormDisappear() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id(LoginFormLocators.ID_FORM)));
    }


    /** wait enabled state of the login button */
    public void waitEnabledLoginBtn() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(enabledLoginBtn));
    }

    /** wait disabled state of the login button */
    public void waitDisabledLoginBtn() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(disabledLoginBtn));
    }

    /** click on login button on login Heroku form */
    public void clickLoginBtn() {
        enabledLoginBtn.click();
        waitLoginFormDisappear();
    }

    /** click on cancel btn */
    public void clickOnCancelBtn() {
        cancelLoginBtn.click();
    }

    /** click on try as demo btn */
    public void clickTryAsDemoBtn() {
        tryAsDemoBtn.click();
    }


    /**
     * type a user in login field
     *
     * @param email
     */
    public void typeLogin(String email) {

        emailField.sendKeys(email);
    }

    /**
     * type a password in login form field
     *
     * @param password
     */
    public void typePassword(String password) {
        passwordField.sendKeys(password);
    }


}
