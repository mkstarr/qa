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
package com.codenvy.ide.runner;

import static org.junit.Assert.fail;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.codenvy.ide.BaseTest;

public class RubyOnRailsApplicationNotRunTest extends BaseTest {
   
   private static final String PROJECT = RubyOnRailsApplicationNotRunTest.class.getSimpleName();

   @AfterClass
   public static void TearDown() {
       try {
           VirtualFileSystemUtils.delete(PROJECT);
       } catch (Exception e) {
           fail("Can't delete test project");
       }
   }
   
   @Test
   public void rubyOnRailsApplicationNotRunTest() throws Exception {
       new WebDriverWait(driver, 60);
       IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
       IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
       IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
       IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
       IDE.CREATE_PROJECT_FROM_SCRATHC.selectRubyTechnology();
       IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
       IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
       IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple Ruby on Rails application. v1.9");
       IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
       IDE.LOADER.waitClosed();
       IDE.EXPLORER.waitOpened();
       IDE.MENU.waitTopMenuDisabled(MenuCommands.Run.RUN);
   }
}
