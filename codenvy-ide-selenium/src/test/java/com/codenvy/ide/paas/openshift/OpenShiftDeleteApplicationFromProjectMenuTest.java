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
package com.codenvy.ide.paas.openshift;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.fail;

/**
 * @author Zaryana Dombrovskaya
 */
public class OpenShiftDeleteApplicationFromProjectMenuTest extends BaseTest {

    private final static String PROJECT_NAME = "deleteos" + getCurrentTimeForProjectName();

    private String outputMessageLogin = "[INFO] Logged in OpenShift successfully.";

    @Before
    public void setUp() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.OPEN_SHIFT.login();
        IDE.OUTPUT.waitOnMessage(outputMessageLogin);
        IDE.OUTPUT.clearPanel();
        IDE.OPEN_SHIFT.updateSSHKey();
        IDE.OUTPUT.clearPanel();
        IDE.OPEN_SHIFT.createProject(PROJECT_NAME);
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.clearPanel();
    }

    @Test
    public void deleteProjectFromProjectMenu() throws Exception{
        IDE.OPEN_SHIFT.deleteApplicationFromProjectMenu();
        IDE.OUTPUT.waitOnMessage(String.format("[INFO] Application %s is successfully deleted.", PROJECT_NAME));
//        IDE.MENU.waitSubCommandDisabled(MenuCommands.Project.PROJECT, MenuCommands.Project.PaaS.PAAS, MenuCommands.Project.PaaS.OPENSHIFT);
    }


    @After
    public void tearDown() throws Exception{
        IDE.OPEN_SHIFT.deleteSSHKey();
        try {
            VirtualFileSystemUtils.delete(PROJECT_NAME);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
    //TODO delete project from OpenShift
    }

    public static String getCurrentTimeForProjectName() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US).format(new Date()).replaceAll("[/:]", "").replaceAll(" ", "").toLowerCase();
    }
}