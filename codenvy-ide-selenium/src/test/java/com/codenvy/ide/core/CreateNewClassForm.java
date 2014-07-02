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
package com.codenvy.ide.core;

import com.codenvy.ide.core.AskForValueDialog.Locator;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

//import com.codenvy.ide.core.project.PackageExplorer.Locators;

/**
 * @author Musienko Maxim
 *
 */
public class CreateNewClassForm extends AbstractTestModule {

    public CreateNewClassForm(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final String CREATE_NEW_CLASS_ID = "ideCreateJavaClass-window";

    private final String CLOSE_WINDOW = "//div[@id='ideCreateJavaClass-window']//img[@title='Close']";

    private final String SOURCE_FOLDER_SELECTION = "//div[@id='ideCreateJavaClass-window']//div[3]/select";

    private final String PACKAGE_SELECTION = "//div[@id='ideCreateJavaClass-window']//div[5]/select";

    private final String CLASS_NAME_FIELD_SET = "//div[@id='ideCreateJavaClass-window']//div/input";

    private final String KIND_SELECTION = "//div[@id='ideCreateJavaClass-window']//div[9]/select";

    private final String DISABLED_CREATE_BTN =
            "//div[@id='ideCreateJavaClass-window']//div//td[text()='Create']//ancestor::div[@button-enabled='false']";

    private final String ENABLED_CREATE_BTN =
            "//div[@id='ideCreateJavaClass-window']//div//td[text()='Create']//ancestor::div[@button-enabled='true']";

    private final String CANCEL_BTN = "//div[@id='ideCreateJavaClass-window']//div//td[text()='Cancel']";

    // Basic Webelements
    @FindBy(id = CREATE_NEW_CLASS_ID)
    private WebElement view;

    @FindBy(xpath = SOURCE_FOLDER_SELECTION)
    private WebElement sorceFolderItems;

    @FindBy(xpath = PACKAGE_SELECTION)
    private WebElement packageItems;

    @FindBy(xpath = KIND_SELECTION)
    private WebElement kindSelection;

    @FindBy(xpath = CLASS_NAME_FIELD_SET)
    private WebElement classNameFieldSet;

    @FindBy(xpath = DISABLED_CREATE_BTN)
    private WebElement disabledCreateBtn;

    @FindBy(xpath = ENABLED_CREATE_BTN)
    private WebElement enabledCreateBtn;

    @FindBy(xpath = CANCEL_BTN)
    private WebElement cancelBtn;

    /** wait while man elements is displayed */
    public void waitCreateFormIsPresent() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {

                    return view.isDisplayed() && classNameFieldSet.isDisplayed() && kindSelection.isDisplayed()
                           && packageItems.isDisplayed() && sorceFolderItems.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** wait while form is disappear */
    public void waitFormIsClosed() {
        new WebDriverWait(driver(), 15)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locator.VIEW_LOCATOR)));

    }

    /**
     * wait appearance items in source folder List
     *
     * @param nameOfItem
     */

    public void waitItemIsPresentInSourceFolderList(final String nameOfItem) {

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    List<WebElement> selectItems = sorceFolderItems.findElements(By.tagName("option"));
                    boolean findItems = false;

                    for (WebElement item : selectItems) {
                        if (item.getAttribute("value").contains(nameOfItem)) {
                            findItems = true;
                            break;
                        }

                    }
                    return findItems;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * wait appearance items in package List
     *
     * @param nameOfItem
     */
    public void waitItemIsPresentInPackageList(final String nameOfItem) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    List<WebElement> selectItems = packageItems.findElements(By.tagName("option"));
                    boolean findItems = false;
                    for (WebElement item : selectItems) {
                        if (item.getAttribute("value").contains(nameOfItem)) {

                            findItems = true;
                            break;
                        }
                    }
                    return findItems;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });

    }

    /**
     * wait appearance items in package List
     *
     * @param nameOfItem
     */
    public void waitItemIsPresentInKindList(final String nameOfItem) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    List<WebElement> selectItems = kindSelection.findElements(By.tagName("option"));
                    boolean findItems = false;
                    for (WebElement item : selectItems) {
                        if (item.getAttribute("value").contains(nameOfItem)) {
                            findItems = true;
                            break;
                        }
                    }
                    return findItems;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** wait while create Btn is disabled */
    public void waitCreateButtonIsDisabled() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(disabledCreateBtn));
    }

    /** wait while create Btn is enabled */
    public void waitCreateButtonIsEnabled() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(enabledCreateBtn));
    }

    /**
     * type class name
     *
     * @param name
     */
    public void typeClassName(String name) {
        classNameFieldSet.sendKeys(name);
    }

    /** click on create btn */
    public void clickCreateBtn() {
        enabledCreateBtn.click();
    }

    /** select Item Kind */
    public void selectClassKind(String kindName) {
        List<WebElement> list = kindSelection.findElements(By.tagName("option"));
        for (WebElement option : list) {
            if (option.getText().equals(kindName)) {
                option.click();
                break;
            }
        }
    }

    /** select package */
    public void selectPackage(String packageName) {
        List<WebElement> list = packageItems.findElements(By.tagName("option"));
        for (WebElement option : list) {
            if (option.getText().equals(packageName)) {
                option.click();
                break;
            }
        }
    }

    /** select source folder */
    public void selectSourceFolder(String folderName) {
        List<WebElement> list = sorceFolderItems.findElements(By.tagName("option"));
        for (WebElement option : list) {
            if (option.getText().equals(folderName)) {
                option.click();
                break;
            }
        }
    }

    /**
     * wait for current source folder value
     * @param value value
     */
    public void waitSourceFolderValue(final String value) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement element = driver.findElement(By.xpath(SOURCE_FOLDER_SELECTION));
                return element.getAttribute("value").contains(value);
            }
        });
    }

    /**
     * wait for current package value
     * @param value value
     */
    public void waitPackageValue(final String value) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement element = driver.findElement(By.xpath(PACKAGE_SELECTION));
                return element.getAttribute("value").contains(value);
            }
        });
    }

}
