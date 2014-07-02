package com.codenvy.ide.paas.heroku;


import com.codenvy.ide.BaseTest;
import com.codenvy.ide.JsonHelper;
import com.codenvy.ide.paas.PaaSLogout;

import org.exoplatform.ide.commons.ParsingResponseException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * @author Musienko Maxim
 *
 */
public class HerokuAuteficationTest extends BaseTest {

    private        String   outputMess           = "[INFO] Logged in Heroku successfully.";
    private static String   login                = "maxura@ukr.net";
    private static String   password             = "vfrcbv_1978";
    private        String   successLoginMess     = "[INFO] Logged in Heroku successfully.";
    private        String[] mainContentTryAsDemo =
            {"Manage Heroku Applications", "Application", "Delete", "Rename", "Environment", "Info", "Import", "Change"};


    private HerokuApiServices herokuServiseInst = new HerokuApiServices(login, password);

    @BeforeClass
    public static void setup() throws InterruptedException, IOException {
        PaaSLogout.logoutFromHeroku();
        new HerokuApiServices(login, password).removeAllKeys();
    }

    @Before
    public void waitLoadIde() throws InterruptedException {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
    }


    @Test
    public void autentification() throws Exception {
        //switch account
        IDE.HEROKU.switchAccountFromPaasMenu(login, password);
        IDE.OUTPUT.waitForSubTextPresent(outputMess);

        //deploy public key
        IDE.HEROKU.deployPublicKey();
        validateJustDeploydKeyOnHeroku();

        //try as demo
        IDE.HEROKU.launchManageApplication();
        validateDemoApps();
    }

    /**
     * check header of the manage application form table, and check visibility applications. Check buttons Import, Delete, Rename, Change
     * for manage project is present
     */
    private void validateDemoApps() {

        for (String maincont : mainContentTryAsDemo) {
            assertTrue(IDE.HEROKU.manageApplicationForm.getAllTextFromManageApplicationForm().contains(maincont));

        }
        IDE.HEROKU.manageApplicationForm.waitManadgeAppListNotEmpty();
    }

    /**
     * get ssh key list from Heroku side and check just created file
     *
     * @throws IOException
     * @throws ParsingResponseException
     */
    private void validateJustDeploydKeyOnHeroku() throws IOException, ParsingResponseException {
        JsonHelper jsnHlp = new JsonHelper();
        String gettedString = herokuServiseInst.getHerokuKeys();
        //remove unnecessary symbols '[' and ']' in begin and end of the string
        String normalToJsonFormat = gettedString.substring(1, gettedString.length() - 1);
        JsonHelper jsonHlp = new JsonHelper();
        String herokuKey = jsonHlp.getValueByKey(normalToJsonFormat, "public_key");
        assertTrue(herokuKey.startsWith("ssh-rsa "));
        assertTrue(herokuKey.endsWith(USER_NAME));
        //383 it is minimal lengh key with 'ssh-rsa prefix'
        assertTrue(herokuKey.length() > 383);
    }

}
