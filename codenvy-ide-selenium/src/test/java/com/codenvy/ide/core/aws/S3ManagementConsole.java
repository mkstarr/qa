/*
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
package com.codenvy.ide.core.aws;

import com.codenvy.ide.IDE;
import com.codenvy.ide.core.AbstractTestModule;

/** @author Dmytro Nochevnov */
public class S3ManagementConsole extends AbstractTestModule {

    interface Locators {
        String S3_MANAGEMENT_CONSOLE_ID = "ideS3ManagerView-window";
        String CLOSE_BUTTON_XPATH       = "//*[@id='ideManageApplicationViewCloseButton']/table";
    }

    S3ManagementConsole(IDE ide) {
        super(ide);
    }

}   
