package com.codenvy.ide.paas.cloudbees;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.paas.PaaSLogout;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Musienko Maxim
 *
 */
public class CloudbeesTst extends BaseTest {

    private String login = "maxura@ukr.net";

    private String password = "cBwmt0pgv";

    private String successLoginMess = "[INFO] Logged in CloudBees successfully.";

    @AfterClass
    public static void clearLogin() throws IOException {
        PaaSLogout.logoutFromHeroku();
    }


    @Test
    public void autentification() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.CLOUD_BEES.switchAccountFromPaasMenu(login, password);
        IDE.OUTPUT.waitForSubTextPresent(successLoginMess);
    }
}
