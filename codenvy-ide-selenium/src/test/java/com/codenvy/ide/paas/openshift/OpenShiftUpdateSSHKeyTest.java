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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Zaryana Dombrovskaya
 */
public class OpenShiftUpdateSSHKeyTest extends BaseTest {

    private String outputMessage = "[INFO] SSH public key is successfully updated on OpenShift.";

    @Before
    public void setUp() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.OPEN_SHIFT.login();
        IDE.OUTPUT.clearPanel();
    }

    @Test
    public void updateSSHKey() throws Exception {
        IDE.OPEN_SHIFT.updateSSHKey();
        IDE.OUTPUT.waitOnMessage(outputMessage);
        //TODO check in OpenShift side
    }

    @After
    public void deleteSSHKey() throws Exception {
        IDE.OPEN_SHIFT.deleteSSHKey();
    }

}
