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
package com.codenvy.ide.operation.logreader;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Evgen Vidolob
 *
 */
public class LogReaderTest extends BaseTest {

    @Test
    public void testLogReaderOpen() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.LOG_READER);
        IDE.LOG_READER.waitOpened();

        String log = IDE.LOG_READER.getLogContent();

        assertNotNull(log);

        if (log.isEmpty()) {
            assertFalse(IDE.LOG_READER.isPreviousButtonEnabled());
            IDE.LOG_READER.waitNextButtonDisabled();
        } else {
            IDE.LOG_READER.waitNextButtonDisabled();
        }

        IDE.LOG_READER.clickNextButton();
        IDE.LOG_READER.closeLogreader();
    }

    @Test
    public void testLogReaderNavigation() throws Exception {

        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.LOG_READER);
        IDE.LOG_READER.waitOpened();
        String log = IDE.LOG_READER.getLogContent();
        IDE.LOG_READER.waitNextButtonDisabled();
        if (IDE.LOG_READER.isPreviousButtonEnabled()) {
            IDE.LOG_READER.clickPrevButton();
            IDE.LOADER.waitClosed();
            assertFalse(log.equals(IDE.LOG_READER.getLogContent()));
            log = IDE.LOG_READER.getLogContent();
            IDE.LOG_READER.waitNextButtonEnabled();
            IDE.LOG_READER.clickNextButton();
            IDE.LOADER.waitClosed();
            assertFalse(log.equals(IDE.LOG_READER.getLogContent()));
        }
    }

}
