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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Ann Zhuleva */
public class ProjectExplorer extends AbstractTestModule {
    /** @param ide */
    public ProjectExplorer(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_LOCATOR = "//div[@view-id='ideTinyProjectExplorerView']";

        String TREE_PREFIX = "navigation-";

        String TREE_GRID_ID = "ideProjectExplorerItemTreeGrid";

        String ITEM_BY_NAME_IN_PROJECT_EXPLORER_TREE = "//td/div[@class='ide-Tree-label' and text()='%s']";


        String PROJECTS_LIST_GRID_ID = "ideProjectExplorerProjectsListGrid";

        String ROOT_ITEM_SELECTOR = "div#" + TREE_GRID_ID + " div.ide-Tree-label:first-of-type";

        String PROJECT_ROW_LOCATOR = "//table[@id=\"" + PROJECTS_LIST_GRID_ID + "\"]//tr[contains(., \"%s\")]//div";

        String PROJECT_ROW_SELECTOR = "table#" + PROJECTS_LIST_GRID_ID + ">tbody:first-of-type tr";

        String OPEN_CLOSE_BUTTON_LOCATOR = "//div[@id='%s']/table/tbody/tr/td[1]/img";

        String PROJECT_LIST_GRID_ITEM =
                "//table[@id='ideProjectExplorerProjectsListGrid']//div[@style and text()='%s']";

        String CLOSE_EXPLORER_BUTTON = "//div[@button-name='close-tab' and @tab-title='%s']";

        String BORDER_PREFIX = "//div[@component='Border' and contains(@style, '182, 204, 232')]";

        String HIGHLITER_BORDER = VIEW_LOCATOR + BORDER_PREFIX;

        String ITEM_WITH_GIT_ICON =
                "//div[@id='ideProjectExplorerItemTreeGrid']//div[@class='ide-Tree-label' and text()='%s']/../..//img[contains(@style,"
                +
                "'%s')]";

        String LINK_WITH_EDITOR_BUTTON = "//div[@view-id='ideTinyProjectExplorerView']//div[@title='Link with Editor']";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(id = Locators.TREE_GRID_ID)
    private WebElement treeGrid;

    @FindBy(id = Locators.PROJECTS_LIST_GRID_ID)
    private WebElement projectsListGrid;

    @FindBy(css = Locators.ROOT_ITEM_SELECTOR)
    private WebElement rootItem;

    @FindBy(xpath = Locators.LINK_WITH_EDITOR_BUTTON)
    private WebElement linkWithEditorBtn;

    /** @throws InterruptedException */
    public void waitOpened() throws InterruptedException {
        new WebDriverWait(driver(), 180).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                     .xpath(Locators
                                                                                                                    .VIEW_LOCATOR)));
    }


    /**
     * wait while project explorer will be present in DOM the IDE it mean the project explorer can be invisible for user
     *
     * @throws InterruptedException
     */
    public void waitInDomPresent() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Locators.VIEW_LOCATOR)));
    }


    /**
     * wait project explorer closed
     *
     * @throws InterruptedException
     */
    public void waitClosed() throws InterruptedException {
        new WebDriverWait(driver(), 160).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                       .xpath(Locators.VIEW_LOCATOR)));
    }

    /**
     * Returns the active state of the view.
     *
     * @return {@link Boolean} view's active state
     */
    public boolean isActive() {
        return IDE().PERSPECTIVE.isViewActive(view);
    }

    /**
     * Generate item id
     *
     * @param path
     *         item's name
     * @return id of item
     */
    public String getItemId(String path) throws Exception {
        path = (path.startsWith(BaseTest.REST_URL)) ? path.replace(BaseTest.REST_URL, "") : path;
        String itemId = (path.startsWith("/")) ? path : "/" + path;
        itemId = Utils.md5(itemId);
        return Locators.TREE_PREFIX + itemId;
    }

    /**
     * wait content in Project tree
     *
     * @param path
     * @throws Exception
     */
    public void waitForItem(final String path) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.id(getItemId(path))));
        IDE().PROGRESS_BAR.waitProgressBarControlClose();
    }

    /**
     * wait item with specified name in opened project
     *
     * @param name
     */
    public void waitForItemInOpenedProjectByName(final String name) {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format(Locators.ITEM_BY_NAME_IN_PROJECT_EXPLORER_TREE, name))));
    }

    /**
     * check for elements present in tree or not present if present return true otherwise false
     *
     * @param item
     * @return
     */
    public boolean isItemPresnt(String item) {
        try {
            return driver().findElement(By.id(getItemId(item))).isDisplayed();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * wait content in project (name file or folder)
     *
     * @param gridItem
     * @throws Exception
     */
    public void waitForItemInProjectList(final String gridItem) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.PROJECT_LIST_GRID_ITEM,
                gridItem))));
    }

    /**
     * wait content in project (name file or folder) is not present
     *
     * @param gridItem
     * @throws Exception
     */
    public void waitForItemInProjectListNotPresent(final String gridItem) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                Locators.PROJECT_LIST_GRID_ITEM,
                gridItem))));
    }

    /**
     * wait disappear item in project tree
     *
     * @param path
     * @throws Exception
     */
    public void waitForItemNotPresent(final String path) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.id(getItemId(path))));
    }

    /**
     * wait disappear progressor image on select folder
     *
     * @param path
     * @throws Exception
     */
    public void waitUpdateContentInFolder(final String path) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    String stateImgProgessor = getImageAttributeFromContent(path);
                    return stateImgProgessor.startsWith("url(\"data:image/png;");
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }

    public void waitForItemNotVisible(final String path) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.id(getItemId(path))));
    }

    /**
     * Select item in project explorer view.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void selectItem(String path) throws Exception {
        driver().findElement(By.xpath("//div[@id='" + getItemId(path) + "']//div[@class='ide-Tree-label']")).click();
        waitHiglightBorderPresent();
    }

    /**
     * Select item in project explorer view by right mouse click.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void selectItemByRightClick(String path) throws Exception {
        WebElement item = driver().findElement(By.id(getItemId(path)));
        new Actions(driver()).contextClick(item).build().perform();
    }

    /**
     * Select item in project explorer view by right mouse click.
     *
     * @param name
     *         item's path
     * @throws Exception
     */
    public void selectItemRightbyNameItem(String name) throws Exception {
        WebElement item =
                driver().findElement(By.xpath("//div[@id='ideProjectExplorerItemTreeGrid']//div[@class='ide-Tree-label' and text()='"
                                              + name + "']/ancestor::table"));
        new Actions(driver()).contextClick(item).perform();
    }

    /**
     * if we need select root element second param. should be true
     *
     * @param path
     * @param root
     * @throws Exception
     */
    public void selectItemByRightClick(String path, boolean root) throws Exception {
        if (root) {
            WebElement item = driver().findElement(By.xpath("//div[@id='ideProjectExplorerItemTreeGrid']//table"));
            new Actions(driver()).contextClick(item).build().perform();
        } else {
            WebElement item = driver().findElement(By.id(getItemId(path)));
            new Actions(driver()).contextClick(item).build().perform();
        }
    }

    /**
     * Open item (make double click) in Project explorer tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void openItem(String path) throws Exception {
        WebElement item = driver().findElement(By.id(getItemId(path)));
        item.click();
        new Actions(driver()).doubleClick(item).build().perform();
    }

    /**
     * wait item present in project explorer tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void waitItemPresent(String path) throws Exception {
        new WebDriverWait(driver(), 35).until(ExpectedConditions.visibilityOfElementLocated(By.id(getItemId(path))));
    }

    /**
     * wait item not present in project explorer tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void waitItemNotPresent(String path) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.id(getItemId(path))));
    }

    /**
     * wait item visibility state in project explorer tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void waitItemVisible(String path) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.id(getItemId(path))));
    }

    /**
     * wait item invisibility state in project explorer tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void waitItemNotVisible(String path) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.id(getItemId(path))));
    }

    /**
     * Press right arrow for expand item
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void expandItem(String path) throws Exception {
        WebElement item = driver().findElement(By.id(getItemId(path)));
        item.click();
        item.sendKeys(Keys.ARROW_RIGHT);
    }

    /**
     * Click open/close(+/-) button of the pointed item.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void clickOpenCloseButton(String path) throws Exception {
        WebElement button =
                driver().findElement(By.xpath(String.format(Locators.OPEN_CLOSE_BUTTON_LOCATOR, getItemId(path))));
        button.click();
    }

    /**
     * Returns current folder's name (root node in Project Explorer).
     *
     * @return {@link String} name of the current project
     */
    public String getCurrentProject() {
        if (treeGrid == null || !treeGrid.isDisplayed() || rootItem == null || !rootItem.isDisplayed()) {
            return null;
        }
        return rootItem.getText();
    }

    public void typeKeys(String keys) {
        new Actions(driver()).sendKeys(treeGrid, keys).build().perform();
    }

    /**
     * send your keys commands to item in project explorer
     *
     * @param keys
     * @param item
     * @throws Exception
     */
    public void typeKeysToItem(String item, String keys) throws Exception {
        WebElement elem =
                driver().findElement(By.xpath("//*[@id='" + getItemId(item) + "']//div[@class='ide-Tree-label']"));
        elem.sendKeys(keys);
    }

    /**
     * click on tab with name of the project
     *
     * @param nameProject
     */
    public void selectProjectTab(String nameProject) {

        IDE().PERSPECTIVE.selectTabsOnExplorer(nameProject);
    }

    /** wait the visibility state of the projects list grid. */
    public void waitProjectsListGridVisible() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .id(Locators.PROJECTS_LIST_GRID_ID)));
    }

    /** wait the invisibility state of the projects list grid. */
    public void waitProjectsListGridNotVisible() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .id(Locators.PROJECTS_LIST_GRID_ID)));
    }

    /**
     * Get the number of projects in grid.
     *
     * @return count of projects
     */
    public int getProjectsCountInProjectsListGrid() {
        return driver().findElements(By.cssSelector(Locators.PROJECT_ROW_SELECTOR)).size();
    }

    /**
     * Select the row with project by the pointed name.
     *
     * @param name
     *         project name
     */
    public void selectProjectByNameInProjectsListGrid(String name) {
        WebElement projectRow = driver().findElement(By.xpath(String.format(Locators.PROJECT_ROW_LOCATOR, name)));
        projectRow.click();
    }

    /** Open context menu. */
    public void openContextMenu() {
        new Actions(driver()).contextClick(view);
    }

    /**
     * get image attribute and return string from current folder
     *
     * @throws Exception
     */
    public String getImageAttributeFromContent(String path) throws Exception {
        WebElement imgElem =
                driver().findElement(By.xpath("//div[@id='" + getItemId(path) + "']" + "/table/tbody/tr/td[2]//img"));
        return imgElem.getCssValue("background");
    }

    /**
     * close ProjectExplorer
     *
     * @param projectName
     * @throws Exception
     */
    public void clickCloseProjectExplorer(String projectName) throws Exception {
        WebElement closeBtn =
                driver().findElement(By.xpath(String.format(Locators.CLOSE_EXPLORER_BUTTON, projectName)));
        closeBtn.click();
    }

    /** Wait true if highlight border present */
    public void waitHiglightBorderPresent() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators.HIGHLITER_BORDER)));
    }

    /**
     * wait item with specified git icon
     *
     * @param nodeName
     *         item name
     * @param iconHash
     *         icon hash (base46)
     */
    public void waitElementWithGitIcon(final String nodeName, final String iconHash) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                String.format(Locators.ITEM_WITH_GIT_ICON,
                              nodeName, iconHash))));
    }

    /**
     * wait item without specified git icon
     *
     * @param nodeName
     *         item name
     * @param iconHash
     *         icon hash (base46)
     */
    public void waitElementWithoutGitIcon(final String nodeName, final String iconHash) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
                String.format(Locators.ITEM_WITH_GIT_ICON,
                              nodeName, iconHash))));
    }

    /** click on Link with Editor button */
    public void clickOnLinkWithEditorButton() {
        linkWithEditorBtn.click();
    }

}
