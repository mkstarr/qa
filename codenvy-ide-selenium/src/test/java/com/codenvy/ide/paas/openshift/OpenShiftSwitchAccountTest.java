package com.codenvy.ide.paas.openshift;

import com.codenvy.ide.BaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Zaryana Dombrovskaya
 *
 */

public class OpenShiftSwitchAccountTest extends BaseTest {

    private String outputMessage = "[INFO] Logged in OpenShift successfully.";

//    public static final ResourceBundle IDE_SETTINGS = ResourceBundle.getBundle("conf/selenium");
//    public static String LOGIN = IDE_SETTINGS.getString("openshift.email");
//    public static String PASSWORD = IDE_SETTINGS.getString("openshift.password");

    @Before
    public void setUp() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
    }

    @Test
    public void switchAccountTest() throws Exception {

        IDE.OPEN_SHIFT.login();
        IDE.OUTPUT.waitOnMessage(outputMessage);
        IDE.OPEN_SHIFT.manageApplicationsWindow.checkEmailAndNameSpace();

    }
}
