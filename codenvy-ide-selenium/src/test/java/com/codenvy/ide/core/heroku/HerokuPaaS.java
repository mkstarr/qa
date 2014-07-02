package com.codenvy.ide.core.heroku;

import com.codenvy.ide.IDE;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.core.AbstractTestModule;
import com.codenvy.ide.core.WarningDialog;
import com.codenvy.ide.core.exceptions.PaaSException;
import com.google.common.base.Predicate;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** @author Musienko Maxim */
public class HerokuPaaS extends AbstractTestModule {

    public static LoginForm loginForm;

    public static ManageApplicationForm manageApplicationForm;

    public static ProjectForm projectForm;

    public static CreateAppForm createAppForm;

    private static WarningDialog warningDialog;


    private static final int TIMEOUT = (int)TestConstants.PAGE_LOAD_PERIOD / 20;

    public HerokuPaaS(IDE ide) {
        super(ide);
        PageFactory.initElements(driver(), loginForm = new LoginForm(this.IDE()));
        PageFactory.initElements(driver(), manageApplicationForm = new ManageApplicationForm(this.IDE()));
        PageFactory.initElements(driver(), projectForm = new ProjectForm(this.IDE()));
        PageFactory.initElements(driver(), createAppForm = new CreateAppForm(this.IDE()));
    }

    /**
     * call switch account IDE menu (PaaS-> Heroku -> Switch Account), wait login form, type credentials, click login button and wait
     * closing the form
     *
     * @param login
     *         your heroku login
     * @param password
     *         youe heroku password
     */
    public void switchAccountFromPaasMenu(String login, String password) {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.Heroku.HEROKU, MenuCommands.PaaS.Heroku.SWITCH_ACCOUNT);
        loginForm.waitLoginForm();
        loginForm.typeLogin(login);
        loginForm.typePassword(password);
        loginForm.waitEnabledLoginBtn();
        loginForm.clickLoginBtn();
        loginForm.waitLoginFormDisappear();
    }

    /**
     * call  IDE menu (PaaS-> Heroku -> Deploy public key), check  text in output panel.
     * go to Window -> Preferences menu, select customize Key menu and check what heroku key has been added
     */
    public void deployPublicKey() throws InterruptedException {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.Heroku.HEROKU, MenuCommands.PaaS.Heroku.DEPLOY_PUBLIC_KEY);
        IDE().OUTPUT.waitForSubTextPresent("[INFO] Public keys are successfully deployed on Heroku.");
        IDE().MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE().PREFERENCES.waitPreferencesOpen();
        IDE().PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.SSH_KEY);
        IDE().LOADER.waitClosed();
        IDE().SSH.waitSshView();
        IDE().SSH.waitAppearContentInSshListGrig("heroku.com");
        IDE().SSH.waitAppearContentInSshListGrig("View");
        IDE().SSH.waitAppearContentInSshListGrig("Delete");
        IDE().PREFERENCES.clickOnCloseFormBtn();
        IDE().PREFERENCES.waitPreferencesClose();
    }

    /**
     * wait login form after invocation, enters user credentials, clicks on login button, waits closing the form
     *
     * @param login
     *         user login to Heroku
     * @param password
     *         user password to Heroku
     */
    public void loginToAccount(String login, String password) {
        loginForm.waitLoginForm();
        loginForm.typeLogin(login);
        loginForm.typePassword(password);
        loginForm.waitEnabledLoginBtn();
        loginForm.clickLoginBtn();
        loginForm.waitLoginFormDisappear();
    }

    /** launch Heroku project form from Project->PaaS->Heroku. Wait while form redraw */
    public void launchHerokuProjectForm() {
        IDE().MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PaaS.PAAS, MenuCommands.Project.PaaS.HEROKU);
        projectForm.waitAppearanceHerokuProjectForm();
        projectForm.waitWhileAppNameNotEmpty();
        projectForm.waitWhileStackNameNotEmpty();
    }

    /** close Heroku form with click on close button on form. Wait while form disappears */
    public void closeHerokuProjectForm() {
        projectForm.clickProjectHerokuCloseBtn();
        projectForm.waitDisappearHerokuProjectForm();
    }


    /** call  IDE menu (PaaS-> Heroku -> Application), check  text in output panel. */
    public void launchManageApplication() {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.Heroku.HEROKU, MenuCommands.PaaS.Heroku.APPLICATION);
        manageApplicationForm.waitManageApplicationForm();
    }

    /** close Manage Application form and check closing */
    public void waitAndCloseManageApplicationForm() throws InterruptedException {
        IDE().LOADER.waitClosed();
        warningDialog.waitEventOrWarningDialog(new Predicate() {
            public boolean apply(Object input) {
                return ExpectedConditions.visibilityOf(manageApplicationForm.getManageForm()).apply(driver()) != null;
            }
        }, TIMEOUT, PaaSException.class);

        manageApplicationForm.waitManageApplicationForm();
        manageApplicationForm.clickCloseBtn();
        manageApplicationForm.waitManageApplicationFormClose();
    }

    /**
     * call create app form from PaaS-> Heroku -> Create Application, wait create app form and click create button
     * application is creating by defaults name repo and name of the application
     */
    public void createApplicationFromPaasMenu() throws InterruptedException {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.Heroku.HEROKU, MenuCommands.PaaS.Heroku.CREATE_APPLICATION);
        createAppForm.waitAppereanceCreateAppForm();
        createAppForm.clickCreateBtn();
        createAppForm.waitDisappearCreateAppForm();
        IDE().LOADER.waitClosed();
    }


    //TODO for feature tests
//    public void createApplicationFromPaasMenu(String nameApp, String repoName) {
//
//    }


}
