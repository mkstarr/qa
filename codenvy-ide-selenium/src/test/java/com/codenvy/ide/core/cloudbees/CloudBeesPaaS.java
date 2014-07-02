package com.codenvy.ide.core.cloudbees;

import com.codenvy.ide.IDE;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.support.PageFactory;

/** @author Musienko Maxim */
public class CloudBeesPaaS extends AbstractTestModule {
    public static LoginForm loginForm;

    public CloudBeesPaaS(IDE ide) {
        super(ide);
        PageFactory.initElements(driver(), loginForm = new LoginForm(this.IDE()));
    }

    public void switchAccountFromPaasMenu(String login, String password) {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.CloudBees.CLOUD_BEES, MenuCommands.PaaS.CloudBees.SWITCH_ACCOUNT);
        loginForm.waitLoginForm();
        loginForm.typeLogin(login);
        loginForm.typePassword(password);
        loginForm.waitEnabledLoginBtn();
        loginForm.clickLoginBtn();
        loginForm.waitLoginFormDisappear();
    }

}
