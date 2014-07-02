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
package com.codenvy.ide.core.project;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.Utils;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */
public class PackageExplorer extends AbstractTestModule {
    /** @param ide */
    public PackageExplorer(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    // TODO after improvement IDE-2200 change methods for package explorer as it done in project explorer
    private interface Locators {
        String PACKAGE_EXPLORER_ID                                   = "idePackageExplorerTreeGrid";

        String PACKAGE_EXPLORER                                      =
                                                                       "(//div[@id='idePackageExplorerTreeGrid']//div[@role='treeitem'])[last()]";

        String PACKAGE_EXPLORER_ITEM                                 =
                                                                       "//div[@id='idePackageExplorerTreeGrid']//div[@class='ide-Tree-label' and text()='%s']";

        String CLOSE_PACKAGE_EXPLORER_BUTTON                         = "//div[@button-name='close-tab' and @tab-title='Package Explorer']";

        String PACKAGE_EXPLORER_SELECTED_ITEM                        =
                                                                       "//div[@id='idePackageExplorerTreeGrid']//div[@class='gwt-TreeItem "
                                                                           +
                                                                           "gwt-TreeItem-selected']//div[@class='ide-Tree-label' and text()='%s']";

        String PACKAGE_EXPLORER_OPEN_ICON_WITH_SPEC_NAME             =
                                                                       "//div[@id='idePackageExplorerTreeGrid']//div[@class='ide-Tree-label' and text()"
                                                                           +
                                                                           "='%s']//ancestor::tbody/tr/td//img";

        String TREE_PREFIX                                           = "ide.package_explorer.item.";

        String NEW_PACKAGE_ID                                        = "ideCreatePackageView-window";

        String NEW_PACKAGE_INPUT_FIELD                               = "//div[@id='ideCreatePackageView-window']//input[@type='text']";

        String NEW_PACKAGE_CREATE_BUTTON                             =
                                                                       "//div[@id='ideCreatePackageView-window']//td[text()='Create']";

        String CONVENTION_WARNING_MESSAGE_ON_CREATE_NEW_PACKAGE_FORM =
                                                                       "(//div[@id='ideCreatePackageView-window']//div[contains(.,'convention')])[last()]";

        String EMPTY_WARNING_MESSAGE_ON_CREATE_NEW_PACKAGE_FORM      =
                                                                       "(//div[@id='ideCreatePackageView-window']//div[contains(.,'must not be empty')])[last()]";

        String CONTEXT_MENU_ID                                       = "eXoIDEContextMenu";

        String ELEMENT_IN_CONTEXT_MENU_BY_NAME                       = "//div[@id='eXoIDEContextMenu']//nobr[text()='%s']";

        String ELEMENT_IN_CONTEXT_MENU_BY_NAME_FOR_CHECKING_STATE    =
                                                                       "//div[@id='eXoIDEContextMenu']//nobr[text()='%s']/../..";

        String ENABLED_ATTRIBUTE                                     = "item-enabled";

        String LINK_WITH_EDITOR_BUTTON                               =
                                                                       "//div[@view-id='idePackageExplorerView']//div[@title='Link with Editor']";

        String TAB_SCROLLER_RIGHT                                    = "//div[@class='tabPanelScrollerRight']";

        String TAB_SCROLLER_LEFT                                     = "//div[@class='tabPanelScrollerLeft']";


        String SELECT_OFF_NODE_POINT                                 =
                                                                       "//div[@id='idePackageExplorerTreeGrid']//div[@class='ide-Tree-label' and text()"
                                                                           +
                                                                           "='%s']/ancestor::table/parent::div/div//div[text()='%s']";

        String ITEM_WITH_GIT_ICON                                    =
                                                                       "//div[@id='idePackageExplorerTreeGrid']//div[@class='ide-Tree-label' and text()='%s']/../..//img[contains(@style,'%s')]";
    }

    @FindBy(id = Locators.PACKAGE_EXPLORER_ID)
    private WebElement packageExplorerId;

    @FindBy(xpath = Locators.PACKAGE_EXPLORER)
    private WebElement packageExplorer;

    @FindBy(xpath = Locators.CLOSE_PACKAGE_EXPLORER_BUTTON)
    private WebElement closePackageExplorerButton;

    @FindBy(id = Locators.NEW_PACKAGE_ID)
    private WebElement newPackageId;

    @FindBy(xpath = Locators.NEW_PACKAGE_INPUT_FIELD)
    private WebElement newPackageInputField;

    @FindBy(xpath = Locators.NEW_PACKAGE_CREATE_BUTTON)
    private WebElement newPackageCreateButton;

    @FindBy(id = Locators.CONTEXT_MENU_ID)
    private WebElement contextMenuId;

    @FindBy(xpath = Locators.LINK_WITH_EDITOR_BUTTON)
    private WebElement linkWithEditorBtn;

    @FindBy(xpath = Locators.TAB_SCROLLER_RIGHT)
    private WebElement scrollerTabRight;

    @FindBy(xpath = Locators.TAB_SCROLLER_LEFT)
    private WebElement scrollerTabLeft;

    /**
     * Wait and close Package Explorer
     *
     * @throws Exception
     */
    public void waitAndClosePackageExplorer() throws Exception {
        waitPackageExplorerOpened();
        closePackageExplorer();
    }

    /**
     * Close Package Explorer
     * <p/>
     * if prj name too long clicks
     */
    public void closePackageExplorer() throws Exception {
        if (scrollerTabRight.isDisplayed() && scrollerTabRight != null) {
            scrollerTabRight.click();
            // sleep for animation
            Thread.sleep(2000);
            closePackageExplorerButton.click();
        } else {
            closePackageExplorerButton.click();
        }

    }

    /**
     * click on right scroller
     *
     * @throws Exception
     */
    public void clickOnRightScroller() throws Exception {
        scrollerTabRight.click();
    }


    /**
     * click on left scroller
     *
     * @throws Exception
     */
    public void clickOnLeftScroller() throws Exception {
        scrollerTabLeft.click();

    }


    /**
     * Wait for Package Explorer
     *
     * @throws Exception
     */
    public void waitPackageExplorerOpened() throws Exception {
        IDE().PROGRESS_BAR.waitProgressBarControl();
        IDE().PROGRESS_BAR.waitProgressBarControlClose();
        IDE().LOADER.waitClosed();
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(packageExplorer));
    }

    /**
     * Wait for reopening of Package Explorer
     *
     * @throws Exception
     */
    public void waitPackageExplorerReOpened() throws Exception {
        IDE().PROGRESS_BAR.waitProgressBarControlClose();
        IDE().LOADER.waitClosed();
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(packageExplorer));
    }

    /** Wait for closing Package Explorer */
    public void waitPackageExplorerClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                .xpath(Locators


                                                                                                .PACKAGE_EXPLORER)));
    }

    /**
     * wait content in Package Explorer
     *
     * @param path
     * @throws Exception
     */
    public void waitItemPresentByPath(String path) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.id(getItemId(path))));
    }

    /**
     * Select item in project explorer view.
     *
     * @param path item's path
     * @throws Exception
     */
    public void selectItemByPath(String path) throws Exception {
        WebElement node = driver().findElement(By.id(String.format(getItemId(path))));
        node.click();
        waitItemInPackageIsSelected(path.substring(path.lastIndexOf("/") + 1));
    }

    /**
     * open item with double click by path
     *
     * @param path full path to file
     */
    public void openItemWithDoubleClickByPath(String path) throws Exception {
        WebElement node = driver().findElement(By.id(String.format(getItemId(path))));
        node.click();
        new Actions(driver()).doubleClick(node).build().perform();
    }

    /**
     * Generate item id
     *
     * @param path item's name
     * @return id of item
     */
    public String getItemId(String path) throws Exception {
        path = (path.startsWith(BaseTest.REST_URL)) ? path.replace(BaseTest.REST_URL, "") : path;
        String itemId = (path.startsWith("/")) ? path : "/" + path;
        itemId = Utils.md5(itemId);
        System.out.println(Locators.TREE_PREFIX + itemId);
        return Locators.TREE_PREFIX + itemId;
    }

    /** Wait item with specified name in Package Explorer tree */
    public void waitItemInPackageExplorer(String item) {
        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new WebDriverWait(driver(), 40).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                                                                                                                   Locators.PACKAGE_EXPLORER_ITEM,
                                                                                                                   item))));
    }


    /**
     * check for elements present in tree or not present if present return true otherwise false
     *
     * @param item
     * @return
     */
    public boolean isItemPresent(String item) {
        try {
            return driver().findElement(By.xpath(String.format(Locators.PACKAGE_EXPLORER_ITEM, item))).isDisplayed();
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /** Wait item with specified name in Package Explorer tree */
    public void waitItemInPackageIsSelected(String item) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                                                                                                                   Locators.PACKAGE_EXPLORER_SELECTED_ITEM,
                                                                                                                   item))));
    }

    /**
     * open item with specified name by double click
     *
     * @param item
     */
    public void openItemWithDoubleClick(String item) {
        WebElement node = driver().findElement(By.xpath(String.format(Locators.PACKAGE_EXPLORER_ITEM, item)));
        node.click();
        waitItemInPackageIsSelected(item);
        new Actions(driver()).doubleClick(node).build().perform();
        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** open item off node point (for example multi module maven project can have the same names packages) */
    public void openItemOffNodePointWithDoubleClick(String nodePointItem, String selectItem) {
        WebElement nodePoint =
                               driver().findElement(
                                                    By.xpath(String.format(Locators.SELECT_OFF_NODE_POINT, nodePointItem, selectItem)));
        nodePoint.click();
        waitItemInPackageIsSelected(selectItem);
        new Actions(driver()).doubleClick(nodePoint).build().perform();
    }

    /** wait item off node point */
    public void waitItemOffNodePoint(String nodePointItem, String selectItem) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                                                                                                                   Locators.SELECT_OFF_NODE_POINT,
                                                                                                                   nodePointItem,
                                                                                                                   selectItem))));
    }

    /**
     * click on icon '+' with specified name in tree
     *
     * @param item
     */
    public void openItemWithClickOnOpenIcon(String item) {
        driver().findElement(By.xpath(String.format(Locators.PACKAGE_EXPLORER_OPEN_ICON_WITH_SPEC_NAME, item))).click();
    }


    /**
     * right click on item in package explorer with
     *
     * @param item
     */
    public void rightClickOnItem(String item) {
        WebElement node = driver().findElement(By.xpath(String.format(Locators.PACKAGE_EXPLORER_ITEM, item)));
        new Actions(driver()).contextClick(node).build().perform();
        waitItemInPackageIsSelected(item);
    }

    /**
     * select item with specified name
     *
     * @param item
     */
    public void selectItemInPackageExplorer(String item) {
        WebElement node = driver().findElement(By.xpath(String.format(Locators.PACKAGE_EXPLORER_ITEM, item)));
        node.click();
        waitItemInPackageIsSelected(item);
    }

    /** Wait item with specified name in Package Explorer is not present */
    public void waitItemInPackageExplorerIsNotPresent(String item) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                                                                                                                     Locators.PACKAGE_EXPLORER_ITEM,
                                                                                                                     item))));
    }

    /** Wait create new package form */
    public void waitCreateNewPackageForm() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                              .id(Locators.NEW_PACKAGE_ID)));
    }

    /** New package input */
    public void typeNewPackageName(String packageName) {
        newPackageInputField.sendKeys(packageName);
    }

    /**
     * New package create button
     *
     * @throws Exception
     */
    public void clickCreateNewPackageButton() throws Exception {
        newPackageCreateButton.click();
        IDE().LOADER.waitClosed();
    }

    /** wait for convention warning. */
    public void waitConventionWarningInCreatePackageForm() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                              .xpath(Locators


                                                                                              .CONVENTION_WARNING_MESSAGE_ON_CREATE_NEW_PACKAGE_FORM)));
    }

    /** wait for empty name field warning. */
    public void waitEmptyNameFieldWarningInCreatePackageForm() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                              .xpath(Locators.EMPTY_WARNING_MESSAGE_ON_CREATE_NEW_PACKAGE_FORM)));
    }

    /**
     * context menu on item with specified name
     *
     * @param item
     */
    public void openContextMenuOnSelectedItemInPackageExplorer(String item) {
        WebElement node = driver().findElement(By.xpath(String.format(Locators.PACKAGE_EXPLORER_ITEM, item)));
        node.click();
        waitItemInPackageIsSelected(item);
        Actions act = new Actions(driver());
        Action rClick = act.contextClick(node).build();
        rClick.perform();
        waitContextMenu();
    }

    /** wait for context menu. */
    public void waitContextMenu() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                              .id(Locators.CONTEXT_MENU_ID)));
    }

    /** wait for context menu disappear. */
    public void waitContextMenuDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                .id(Locators.CONTEXT_MENU_ID)));
    }

    /**
     * click on element from context menu by name
     *
     * @param item
     */
    public void clickOnItemInContextMenu(String item) {
        driver().findElement(By.xpath(String.format(Locators.ELEMENT_IN_CONTEXT_MENU_BY_NAME, item))).click();
    }

    /**
     * Wait for enabled state of the context menu command.
     *
     * @param itemName command name
     */
    public void waitElementInContextMenuEnabled(final String itemName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement command =
                                         driver().findElement(
                                                              By.xpath(String.format(Locators.ELEMENT_IN_CONTEXT_MENU_BY_NAME_FOR_CHECKING_STATE,
                                                                                     itemName)));
                    return Boolean.parseBoolean(command.getAttribute(Locators.ENABLED_ATTRIBUTE));
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait for disabled state of the context menu command.
     *
     * @param itemName command name
     */
    public void waitElementInContextMenuDisabled(final String itemName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement command =
                                         driver().findElement(
                                                              By.xpath(String.format(Locators.ELEMENT_IN_CONTEXT_MENU_BY_NAME_FOR_CHECKING_STATE,
                                                                                     itemName)));
                    return !(Boolean.parseBoolean(command.getAttribute(Locators.ENABLED_ATTRIBUTE)));
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Type keys to tree grid
     *
     * @param keys
     */

    public void typeKeys(String keys) {
        new Actions(driver()).sendKeys(packageExplorerId, keys).build().perform();
    }

    /** click on Link with Editor button */
    public void clickOnLinkWithEditorButton() {
        linkWithEditorBtn.click();
    }

    /**
     * wait item with specified git icon
     *
     * @param nodeName item name
     * @param iconHash icon hash (base46)
     */
    public void waitElementWithGitIcon(final String nodeName, final String iconHash) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                                                                                              String.format(Locators.ITEM_WITH_GIT_ICON,
                                                                                                            nodeName, iconHash))));
    }

    /**
     * wait item without specified git icon
     *
     * @param nodeName item name
     * @param iconHash icon hash (base46)
     */
    public void waitElementWithoutGitIcon(final String nodeName, final String iconHash) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
                                                                                                String.format(Locators.ITEM_WITH_GIT_ICON,
                                                                                                              nodeName, iconHash))));
    }

    /** wait item without specified git icon */
    public void selectPackageExplorerTab() {
        IDE().PERSPECTIVE.selectTabsOnExplorer("Package Explorer");
    }

    /** click on first element in pkg. explorer */
    public void selectRoot() {
        new WebDriverWait(driver(), 5)
                                      .until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='idePackageExplorerTreeGrid']//table//td[2]")))
                                      .click();
    }
}
