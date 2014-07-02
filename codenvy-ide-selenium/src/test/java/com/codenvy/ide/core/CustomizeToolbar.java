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
package com.codenvy.ide.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/** @author Musienko Maxim */
public class CustomizeToolbar extends AbstractTestModule {
    /** @param ide */
    public CustomizeToolbar(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String CANCEL_BUTTON_ID = "ide.core.customize-tolbar.cancel-button";

        String ADD_BUTTON_ID = "ide.core.customize-toolbar.add-button";

        String DELIMETER_BUTTON_ID = "ide.core.customize-toolbar.delimiter-button";

        String DELETE_BUTTON_ID = "ide.core.customize-toolbar.delete-button";

        String MOVE_UP_BUTTON_ID = "ide.core.customize-toolbar.move-up-button";

        String MOVE_DOWN_BUTTON_ID = "ide.core.customize-toolbar.move-down-button";

        String OK_BUTTON_ID = "ide.core.customize-toolbar.ok-button";

        String DEFAULTS_BUTTON_ID = "ide.core.customize-toolbar.defaults-button";

        String TOOLBAR_ID = "ide.core.customize-toolbar.toolbar-items-list";

        String TOOLBAR_COMMANDLIST_ID = "ide.core.customize-toolbar.commands-list";

        String CUSTOMIZE_TOOLBAR_FORM_CSS = "div[view-id='ide.core.customize-toolbar']";

        String GET_COMMANDS_BY_CSS = "table[id='ide.core.customize-toolbar.commands-list']>tbody tr";

        String GET_TOOLBAR_COMMANDS_BY_CSS = "table[id=" + "'" + TOOLBAR_ID + "'" + "]" + ">tbody tr";

        String SELECTION_ON_TOOLBAR_ELEMENT = "//table[@id=" + "'" + TOOLBAR_ID + "'" + "]" + "/tbody/tr[%s]";

        String SELECT_ON_TOOLBAR_ELEMENT_BY_NAME = "//table[@id=" + "'" + TOOLBAR_ID + "'" + "]"
                                                   + "/tbody/tr/td//div[contains(.,'%s')]";

        String SELECT_ON_COMMANDLIST_ELEMENT_BY_NAME = "//table[@id=" + "'" + TOOLBAR_COMMANDLIST_ID + "'" + "]"
                                                       + "/tbody//tr/td//div[contains(.,'%s')]";

        String SELECT_ON_COMMANDLIST_ELEMENT_BY_NUM = "//table[@id=" + "'" + TOOLBAR_COMMANDLIST_ID + "'" + "]"
                                                      + "/tbody/tr[%s]";

    }

    @FindBy(id = Locators.DELETE_BUTTON_ID)
    private WebElement deleteButton;

    @FindBy(id = Locators.OK_BUTTON_ID)
    private WebElement okButton;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    private WebElement cancelButton;

    @FindBy(id = Locators.DEFAULTS_BUTTON_ID)
    private WebElement defaultButton;

    @FindBy(css = Locators.CUSTOMIZE_TOOLBAR_FORM_CSS)
    private WebElement customizeToolbarForm;

    @FindBy(id = Locators.MOVE_DOWN_BUTTON_ID)
    private WebElement moveDownButton;

    @FindBy(id = Locators.MOVE_UP_BUTTON_ID)
    private WebElement moveUpButton;

    @FindBy(id = Locators.DELIMETER_BUTTON_ID)
    private WebElement addDelimeterButton;

    @FindBy(id = Locators.ADD_BUTTON_ID)
    private WebElement addButton;

    /** wait appearance customizeToolbarForm Form */
    public void waitOpened() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return customizeToolbarForm != null && customizeToolbarForm.isDisplayed();
            }
        });
    }

    /** waits disappearance toolbar-form */
    public void waitClosed() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.id(Locators.CUSTOMIZE_TOOLBAR_FORM_CSS));
                    return false;
                } catch (Exception e) {
                    return true;
                }
            }
        });

    }

    /**
     * Selects an item on the list toolbar by number start 1
     *
     * @param number
     */
    public void selectNumElementOnToolbar(int number) {
        WebElement toolbarMenu =
                driver().findElement(By.xpath(String.format(Locators.SELECTION_ON_TOOLBAR_ELEMENT, number)));
        toolbarMenu.click();
    }

    /**
     * Selects an item on the commandlist  by number start 1
     *
     * @param number
     */

    public void selectNumElementOnCommandListbar(int number) {
        WebElement toolbarMenu =
                driver().findElement(By.xpath(String.format(Locators.SELECT_ON_COMMANDLIST_ELEMENT_BY_NUM, number)));
        toolbarMenu.click();
    }

    /**
     * Selects an item on the list toolbar by name
     *
     * @param name
     */
    public void selectElementOnToolbarByName(String name) {
        WebElement toolbarMenuByName =
                driver().findElement(By.xpath(String.format(Locators.SELECT_ON_TOOLBAR_ELEMENT_BY_NAME, name)));
        toolbarMenuByName.click();
    }

    /**
     * Selects an item on the commandlist by name
     *
     * @param name
     */
    public void selectElementOnCommandlistbarByName(String name) {
        WebElement toolbarMenuByName =
                driver().findElement(By.xpath(String.format(Locators.SELECT_ON_COMMANDLIST_ELEMENT_BY_NAME, name)));
        toolbarMenuByName.click();
    }

    /** click on add button on Customize Toolbar form */
    public void addClick() {
        addButton.click();
    }

    /** click on cancel button on Customize Toolbar form */
    public void cancelClick() {
        cancelButton.click();
    }

    /** click on delete button on Customize Toolbar form */
    public void deleteClick() {
        deleteButton.click();
    }

    /** click on delete button on Customize Toolbar form */
    public void okClick() {
        okButton.click();
    }

    /** click on default button on Customize Toolbar form */
    public void defaultClick() {
        defaultButton.click();
    }

    /** click on move up button on Customize Toolbar form */
    public void moveUpClick() {
        moveUpButton.click();
    }

    /** click on move down button on Customize Toolbar form */
    public void moveDownClick() {
        moveDownButton.click();
    }

    /** click on delimeter button on Customize Toolbar form */
    public void delimiterClick() {
        addDelimeterButton.click();
    }

    /**
     * Return true if command is present in Toolbar list.
     *
     * @param nameMenu
     * @return
     */
    public boolean isToolbarListPresent(String nameMenu) {
        List<String> res = new ArrayList<String>();
        List<WebElement> allCommands = driver().findElements(By.cssSelector(Locators.GET_TOOLBAR_COMMANDS_BY_CSS));

        for (WebElement name : allCommands) {
            res.add(name.getText().trim());
        }
        return res.contains(nameMenu);
    }

    /**
     * Return true if command is present in command list
     *
     * @param nameMenu
     * @return
     */
    public boolean isCommandListPresent(String nameMenu) {
        List<String> res = new ArrayList<String>();
        List<WebElement> allCommands = driver().findElements(By.cssSelector(Locators.GET_COMMANDS_BY_CSS));

        for (WebElement name : allCommands) {

            res.add(name.getText().trim());

        }
        return res.contains(nameMenu);
    }

    /**
     * Return All names of commands on toolbar list
     *
     * @return
     */
    public List<String> getallToolbarList() {
        List<String> toolbarList = new ArrayList<String>();
        List<WebElement> allCommands = driver().findElements(By.cssSelector(Locators.GET_TOOLBAR_COMMANDS_BY_CSS));

        for (WebElement name : allCommands) {
            toolbarList.add(name.getText().trim());
        }
        return toolbarList;
    }

    /**
     * Get all names of elements command list
     *
     * @return
     */
    public List<String> getallCommandList() {
        List<String> commandList = new ArrayList<String>();
        List<WebElement> allCommands = driver().findElements(By.cssSelector(Locators.GET_COMMANDS_BY_CSS));

        for (WebElement name : allCommands) {
            commandList.add(name.getText().trim());
        }
        return commandList;
    }

    public void isDefaultToolbarList() {
        List<String> getSet = getallToolbarList();

        List<String> defaultSet =
                Arrays.asList("Delimiter", "New * [Popup]", "Save", "Save As...", "Delimiter", "Cut Item(s)",
                              "Copy Item(s)",
                              "Paste Item(s)", "Delete...", "Search...", "Refresh Selected Folder", "Delimiter",
                              "Undo Typing",
                              "Redo Typing", "Format", "Delimiter", "Find-Replace...", "Delimiter",
                              "Show / Hide Outline", "Delimiter",
                              "Show / Hide Documentation", "Spacer", "Properties", "Show Preview",
                              "Collaboration");
        for (String chkName : defaultSet) {
            assertTrue(getSet.contains(chkName.toString()));
        }
    }

    public void isDefaultCommandlbarList() {
        List<String> getSet = getallCommandList();

        List<String> defaultSet =
                Arrays.asList("Share", "Factory URL", "Manage Access", "Invite Developers...", "Invite GitHub Collaborators...", "File",
                              "File", "New * [Popup]", "Upload File...", "Upload Zipped Folder...", "Open Local File...",
                              "Open File By Path...", "Open by URL...", "Download File...", "Download Zipped Folder...", "Save",
                              "Save As...", "Save All", "Delete...", "Rename...", "Search...", "Refresh Selected Folder", "File / New",
                              "File / New", "Create Folder...", "New TEXT", "New XML", "New Java Script", "New HTML", "New CSS",
                              "New Package", "New Java Class", "New JSP File", "New Ruby File", "New PHP File", "New Python File",
                              "New YAML File", "Project / New", "Project / New", "Create Project...", "Create Module...",
                              "Import from GitHub...", "Project", "Project", "Open...", "Close", "Properties...", "Open Resource...",
                              "Disable / Enable Collaboration", "Update Dependency", "Hide / Show Syntax Error Highlighting",
                              "Datasource...", "Build", "Build & Publish", "Project / PaaS", "Project / PaaS", "CloudBees", "Appfog",
                              "Heroku", "OpenShift", "Google App Engine", "Edit", "Edit", "Cut Item(s)", "Copy Item(s)", "Paste Item(s)",
                              "Undo Typing", "Redo Typing", "Format", "Organize Imports", "Add Block Comment", "Remove Block Comment",
                              "Find-Replace...", "Show / Hide Line Numbers", "Delete Current Line", "Go to Line...", "Delete", "Select All",
                              "Toggle Comment", "Move Line Up", "Move Line Down", "FoldSelection", "Quick Fix", "Send Code Pointer",
                              "Edit / Source", "Edit / Source", "Generate Getters and Setters", "Generate Constructor using Fields",
                              "Edit / Refactor", "Edit / Refactor", "Rename", "View", "View", "Properties", "Show / Hide Outline",
                              "Show / Hide Documentation", "Go to Folder", "Progress", "Output", "Log", "Show / Hide Hidden Files",
                              "Quick Outline", "Quick Documentation", "Collaboration", "Run", "Run", "Show Preview", "Debug", "Run", "Stop",
                              "Java Logs", "Stop Python Application", "Run Python Application", "Python Logs", "Stop PHP Application",
                              "Run PHP Application", "PHP Logs", "Stop Node.js Application", "Run Node.js Application", "Node.js Logs",
                              "Stop HTML Application", "Run HTML Application", "Run Android Application", "Git", "Git",
                              "Initialize Repository", "Clone Repository...", "Delete Repository...", "Add...", "Reset...", "Remove...",
                              "Commit...", "Branches...", "Merge...", "Show History...", "Status", "Git URL (Read-Only)...",
                              "Reset Index...", "Git / Remote", "Git / Remote", "Push...", "Fetch...", "Pull...", "Remotes...",
                              "PaaS / CloudBees", "PaaS / CloudBees", "CreateApplication", "AppList", "Switch Account", "Create Account",
                              "PaaS / AppFog", "PaaS / AppFog", "CreateApp", "Applications", "SwitchAccount", "PaaS / Heroku",
                              "PaaS / Heroku", "Create application...", "Applications", "Deploy public key", "Switch account...",
                              "PaaS / OpenShift", "PaaS / OpenShift", "Change Namespace...", "Create application...", "Applications...",
                              "Update SSH public key...", "Switch account...", "PaaS / Google App Engine", "PaaS / Google App Engine",
                              "Deploy", "Create", "Login", "Logout", "PaaS / Elastic Beanstalk", "PaaS / Elastic Beanstalk", "CreateApp",
                              "Manage Application", "Switch Account", "EC2 Management", "S3 Management", "Window / Show View",
                              "Window / Show View", "Project Explorer", "Package Explorer", "Window / Navigation", "Window / Navigation",
                              "Next Editor", "Previous Editor", "Window", "Window", "Preferences", "Welcome", "Help", "Help", "About...",
                              "Show Keyboard Shortcuts...");
        for (String chkName : defaultSet) {
            assertTrue(getSet.contains(chkName.toString()));

        }
    }

    /**
     * wait while element appear in specified position in list
     *
     * @param position
     * @param name
     */
    public void waitToolbarElementPresentInPosition(final int position, final String name) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {

                return driver().findElement(By.xpath(String.format(Locators.SELECTION_ON_TOOLBAR_ELEMENT, position)))
                        .getText().contains(name);

            }
        });
    }

    /**
     * wait while menu appear in toolbar list
     *
     * @param nameMenu
     */
    public void waitToolbarElementIsPresent(final String nameMenu) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return isToolbarListPresent(nameMenu);
            }
        });
    }

    /**
     * wait while menu is disappear in list
     *
     * @param nameMenu
     */
    public void waitToolbarElementIsNotPresent(final String nameMenu) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return !isToolbarListPresent(nameMenu);
            }
        });
    }

    /**
     * return name of element which corresponds to a specific number start with 1
     *
     * @param numElem
     * @return
     */
    public String isToolBarElementNumPositionPresent(int numElem) {
        WebElement toolbarMenu =
                driver().findElement(By.xpath(String.format(Locators.SELECTION_ON_TOOLBAR_ELEMENT, numElem)));
        String nameElement = toolbarMenu.getText();
        return nameElement.trim();
    }
}
