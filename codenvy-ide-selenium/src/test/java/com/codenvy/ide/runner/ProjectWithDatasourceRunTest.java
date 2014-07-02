/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

/**
 * @author Musienko Maxim
 *
 */


public class ProjectWithDatasourceRunTest extends BaseTest {


    private interface Locators {

        // Employee page locators
        String EMPLOYEES_TABLE        = "employees-table";
        String EMPLOYEES_LINK         = "//div[@class='menu-item' and contains(., 'Employees')]";
        String EDIT_IMG               = "//td/img[@src='images/edit.png']";
        String DELETE_IMG             = "//td/img[@src='images/delete.png']";
        String EDIT_ADD_USER          = "//td[text()='Cooper']/following-sibling::td/img[@src='images/edit.png']";
        String DELETE_ADDED_USER      = "//td[text()='Cooper']/following-sibling::td/img[@src='images/delete.png']";
        //
        // Department page locators
        String DEPARTMENS_LINK        = "//div[@class='menu-item' and contains(., 'Departments')]";
        String DEPARTMENS_TABLE_CLASS = "departments-table";


        // New Employee page locators
        String NEW_EMPLOEE_LINK       = "//div[@class='menu-item' and contains(., 'New employee')]";
        String ADD_BTN                = "//button[text()='Add']";
        String FIRST_NAME_FIELD_ID    = "eFirstName";
        String LAST_FIELD_ID          = "eLastName";
        String SALARY_FIELD_ID        = "eSalary";
        String DEPARTMENT_LIST_ID     = "eDepartmentId";

    }

    List<String>                mainLocators   = Arrays.asList(Locators.FIRST_NAME_FIELD_ID,
                                                               Locators.LAST_FIELD_ID, Locators.SALARY_FIELD_ID,
                                                               Locators.DEPARTMENT_LIST_ID);

    private static final String PROJECT        = ProjectWithDatasourceRunTest.class.getSimpleName();

    private static String       CURRENT_WINDOW = null;

    private WebDriverWait       wait;

    private final String        mainElements   =
                                                 "Hello Codenvy Today's date is This script was accessed the first time Below the list of the latest 20 hits ";
    private final String        tableText      =
                                                 "ID First Name Last Name Salary Department Start Date2011-06-11 495 Ivana Liller 86000 4 2010-11-10 496 Ibrahim Fullwood 74000 5 2010-11-29 497 Eve Herzberg 80000 6 2012-02-17 498 Lindsey Corpuz 24000 7 2012-03-18 499 Ellie Gunnels 39000 8 2010-07-23 500 Ashlee Strack 64000 9 2010-12-01";
    private final String        deparementsTex =
                                                 "ID Name Manager\n1 HR 1\n2 Finance 2\n3 Research 3\n4 Sales 4\n5 Business Development 5\n6 Operations 6\n7 Marketing 7\n8 Support 8\n9 Call Center 9\n10 Logistics 10\n11 Legal 11\n12 Security 12";


    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);

        } catch (Exception e) {
            fail("Can't delete Project");
        }
    }

    @Test
    public void projectWithDatasourceRunTest() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 20);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaWebApplicationTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC
                                       .selectProjectTemplate(
                                       "Java Web project with Datasource usage");
//TODO REMOVED DUE TO DISABLING JREBEL
//        IDE.CREATE_PROJECT_FROM_SCRATHC.waitForJRebelCheckbox();
//        IDE.CREATE_PROJECT_FROM_SCRATHC.clickOnJRebelCheckbox();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();

        // TODO After fix problem '[ERROR] /codeassistant-storage/storage/update/dock and resolving dependencies should be uncomment
        // IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.RUN_APPLICATION);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitLinkWithParticalLinkText("//app");
        IDE.OUTPUT.clickOnAppLinkWithParticalText("run");

        // switching to application window
        switchToNonCurrentWindow(CURRENT_WINDOW);

        // checking application
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));
        for (String str : mainElements.split(" ")) {
            waitMainTexts(str);
        }
        waitExpectedValue(getTextFromRowTable(2));
        driver.navigate().refresh();
        waitExpectedValue(getTextFromRowTable(3));


    }


    private void waitMainTexts(final String text) {
        new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElement(By.tagName("body")).getText().contains(text);
            }
        });
    }

    String getTextFromRowTable(int numRow) {
        return driver.findElement(By.xpath("//tbody/tr[" + numRow + "]")).getText();
    }


    /**
     * clear fiels on employee page and type new value
     */
    private void editValues(WebElement elem, String newVal) {
        elem.clear();
        elem.sendKeys(newVal);
    }

    /**
     * wait correctly values in columns 'id' and 'time' of the demo app
     * 
     * @param rowWithWalue
     */
    private void waitExpectedValue(String rowWithWalue) {
        waitExpectedID(rowWithWalue);
        waitExpectedTime(rowWithWalue);
    }


    /**
     * wait pattern for id become as pattern
     * 
     * @param val
     */
    private void waitExpectedID(final String val)
    {
        (new WebDriverWait(IDE.driver(), 10)).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver d) {
                return validateId(val);
            }
        });
    }

    /**
     * wait pattern for id become as pattern
     * 
     * @param val
     */
    private void waitExpectedTime(final String val)
    {
        (new WebDriverWait(IDE.driver(), 10)).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver d) {
                boolean chk = false;
                for (String datesStrings : getCurrentDate()) {
                    if (val.contains(datesStrings)) {
                        chk = true;
                    }
                    else {
                        chk = false;
                        break;
                    }
                }

                return chk;
            }
        });
    }


    /**
     * return true if first column in demo app contains expected sequence
     * 
     * @return
     */
    boolean validateId(String valFromTable)
    {
        Pattern p = Pattern.compile("\\d{13}.*");
        Matcher m = p.matcher(valFromTable);
        return m.matches();
    }

    /**
     * get current year, day and hour from demo app
     * 
     * @return
     */
    private static String[] getCurrentDate() {
        String dt;
        String[] arr;
        DateFormat dateFormat = new SimpleDateFormat("E MMM dd", Locale.US);
        Date date = new Date();
        dt = dateFormat.format(date) + " " + Calendar.getInstance().get(Calendar.YEAR);
        arr = dt.split(" ");
        return arr;
    }

}
