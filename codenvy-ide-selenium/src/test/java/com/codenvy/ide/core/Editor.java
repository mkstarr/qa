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

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.ToolbarCommands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 * @author <a href="mailto:dnochevnov@exoplatform.com">Dmytro Nochevnov</a>
 */

public class Editor extends AbstractTestModule {
    /** @param ide */
    public Editor(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    public static final String MODIFIED_MARK = "*";

    private interface Locators {

        String CODE_MIRROR_EDITOR = "//body[@class='editbox']";

        String EDITOR_TABSET_LOCATOR = "//div[@id='editor']";

        String TAB_LOCATOR = "//div[@tab-bar-index='%s']";

        String EDITOR_TAB_LOCATOR = "//div[@panel-id='editor' and @view-id='editor-%s' ]";

        String EDITOR_VIEW_LOCATOR = "//div[@panel-id='editor' and @view-id='%s']";

        String ACTIVE_EDITOR_TAB_LOCATOR = "//div[@panel-id='editor' and @is-active='true']";

        String SELECTED_EDITOR_TAB_LOCATOR = EDITOR_TABSET_LOCATOR
                                             +
                                             "//div[contains(@class, 'gwt-TabLayoutPanelTab-selected') and contains(" +
                                             "., '%s')]";

        String DEBUG_EDITOR_ACTIVE_FILE_URL = "debug-editor-active-file-url";

        String DEBUG_EDITOR_PREVIOUS_ACTIVE_FILE_URL = "debug-editor-previous-active-file-url";

        String DESIGN_BUTTON_XPATH = "//div[@title='Design']//div[text()='Design']";

        String SOURCE_BUTTON = "//div[text()='Source']";

        String TAB_TITLE = "//table[@class='tabTitleTable' and contains(., '%s')]";

        String CLOSE_BUTTON_LOCATOR = "//div[@button-name='close-tab']";

        String VIEW_ID_ATTRIBUTE = "view-id";

        String TITLE_SPAN_LOCATOR = "//span[@title='%s']/..";

        String LINE_NUMBERS_TAB = "//div[@class='CodeMirror-line-numbers']/..";

        String LINE_HIGHLIGHTER_CLASS = "CodeMirror-line-highlighter";

        String HIGHLIGHTER_SELECTOR = "div[view-id=editor-%s] div." + LINE_HIGHLIGHTER_CLASS;

        String DESIGN_EDITOR_PREFIX = "//div[@view-id='editor-%s']";

        String HIGHLITER_BORDER = DESIGN_EDITOR_PREFIX
                                  + "//div[@component= 'Border' and contains(@style, '182, 204, 232')]";

        String IFRAME = "iframe";

        String NUM_ACTIVE_EDITOR = "//div[@class='gwt-TabLayoutPanelContent' and @is-active='true']";


        String TAB_READONLY_MODE =
                "//td[@class='tabTitleText']/span[@title='File opened in read only mode. Use SaveAs command.' and contains(., " +
                "'%s')]/img[@id='fileReadonly']";
    }

    private WebElement editor;

    @FindBy(className = Locators.LINE_HIGHLIGHTER_CLASS)
    private WebElement highlighter;

    @FindBy(tagName = Locators.IFRAME)
    private WebElement iframe;

    @FindBy(xpath = Locators.CODE_MIRROR_EDITOR)
    private WebElement editorCodemirr;

    @FindBy(xpath = Locators.NUM_ACTIVE_EDITOR)
    private WebElement numActiveEditor;

    /**
     * Returns the title of the tab with the pointed index.
     *
     * @param index
     *         tab index
     * @return {@link String} tab's title
     * @throws Exception
     */
    public String getTabTitle(int index) {
        WebElement tab =
                editor.findElement(
                        By.xpath(Locators.EDITOR_TABSET_LOCATOR + String.format(Locators.TAB_LOCATOR, index)));
        return tab.getText().trim();
    }

    /** waiting while switch between ckeditor on codeeditor */
    public void waitIframe() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return iframe != null && iframe.isDisplayed();
            }
        });
    }


    /**
     * wait tab with specified name and read only title and lock icon
     *
     * @param nameTab
     */
    public void waitTadWitReadOnlyMode(String nameTab) {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.TAB_READONLY_MODE,
                                                                                            nameTab))));
    }


    /** waiting while in editor is no content */
    public void waitContentIsClear() {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return IDE().EDITOR.getTextFromCodeEditor().equals("");
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }


    /** waiting while in editor appear specified content */
    public void waitContentIsPresent(final String text) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    IDE().EDITOR.waitActiveFile();
                    return IDE().EDITOR.getTextFromCodeEditor().contains(text);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }


    /** waiting while in editor appear specified content */
    public void waitContentIsEquals(final String text) {
        new WebDriverWait(driver(), 15).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return IDE().EDITOR.getTextFromCodeEditor().equals(text);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }


    /**
     * Click on editor tab to make it active.
     * <p/>
     * Numbering of tabs starts with 0.
     *
     * @param tabIndex
     *         index of tab
     * @throws Exception
     */
    public void selectTab(int tabIndex) throws Exception {
        editor.findElement(
                By.xpath(String.format(Locators.EDITOR_TABSET_LOCATOR + Locators.TAB_LOCATOR + "//span/..", tabIndex)))
              .click();
        // TODO replace with wait for condition
        Thread.sleep(TestConstants.REDRAW_PERIOD);
    }

    /**
     * Select tab the code editor with the specified name
     *
     * @param fileName
     * @throws Exception
     */
    public void selectTab(String fileName) throws Exception {
        WebElement tab =
                editor.findElement(By.xpath(String.format(Locators.EDITOR_TABSET_LOCATOR + Locators.TITLE_SPAN_LOCATOR,
                                                          fileName)));
        tab.click();
        // TODO replace with wait for condition
        Thread.sleep(TestConstants.REDRAW_PERIOD);
        IDE().LOADER.waitClosed();
    }

    /**
     * click on save as button and type in save as field new name of the file
     *
     * @param tabIndex
     * @param name
     * @throws Exception
     */
    public void saveAs(int tabIndex, String name) throws Exception {
        selectTab(tabIndex);

        IDE().MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE_AS);
        IDE().ASK_FOR_VALUE_DIALOG.waitOpened();
        IDE().ASK_FOR_VALUE_DIALOG.setValue(name);
        IDE().ASK_FOR_VALUE_DIALOG.clickOkButton();
        IDE().ASK_FOR_VALUE_DIALOG.waitClosed();
        IDE().LOADER.waitClosed();
    }

    /**
     * click on save as button and type in save as field new name of the file
     *
     * @param name
     * @throws Exception
     */
    public void saveFileFromToolbar(String name) throws Exception {

        IDE().TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE().LOADER.waitClosed();
        waitNoContentModificationMark(name);
    }

    /**
     * Click on Close Tab button. Old name of this method is "clickCloseTabButton(int tabIndex)"
     *
     * @param tabIndex
     *         index of tab, starts at 0
     */
    public void clickCloseEditorButton(int tabIndex) throws Exception {
        WebElement closeButton =
                editor.findElement(
                        By.xpath(Locators.EDITOR_TABSET_LOCATOR + String.format(Locators.TAB_LOCATOR, tabIndex)
                                 + Locators.CLOSE_BUTTON_LOCATOR));
        closeButton.click();
    }

    /**
     * click on close label on tab wit file name
     *
     * @param tabTitle
     * @throws Exception
     */
    public void clickCloseEditorButton(String tabTitle) throws Exception {
        WebElement closeButton =
                editor.findElement(By.xpath(Locators.EDITOR_TABSET_LOCATOR + String.format(Locators.TAB_TITLE, tabTitle)
                                            + Locators.CLOSE_BUTTON_LOCATOR));
        closeButton.click();
    }

    /**
     * Closes file with num tabinfex start with 0
     *
     * @param tabIndex
     */
    public void closeFile(int tabIndex) throws Exception {
        selectTab(tabIndex);
        clickCloseEditorButton(tabIndex);
        waitTabNotPresent(tabIndex);
    }

    /**
     * Close file with name on tab
     *
     * @param fileName
     * @throws Exception
     */
    public void closeFile(String fileName) throws Exception {
        IDE().LOADER.waitClosed();
        selectTab(fileName);
        clickCloseEditorButton(fileName);
        waitTabNotPresent(fileName);
    }

    /**
     * Close tab in editor. Close ask window in case it appear while closing.
     *
     * @param tabIndex
     *         index of tab, starts at 0
     * @throws Exception
     */
    public void closeTabIgnoringChanges(int tabIndex) throws Exception {
        selectTab(tabIndex);
        final String viewId = editor.findElement(By.xpath(Locators.ACTIVE_EDITOR_TAB_LOCATOR)).getAttribute("view-id");
        clickCloseEditorButton(tabIndex);

        /*
         * Closing ask dialogs if them is appears.
         */
        if (IDE().ASK_DIALOG.isOpened()) {
            IDE().ASK_DIALOG.clickNo();
        } else if (IDE().ASK_FOR_VALUE_DIALOG.isOpened()) {
            IDE().ASK_FOR_VALUE_DIALOG.clickNoButton();
        } else {
            fail("Dialog has been not found!");
        }

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(String.format(Locators.EDITOR_VIEW_LOCATOR, viewId)));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Close tab in editor. Close ask window in case it appear while closing.
     *
     * @param tabName
     *         name of tab.
     * @throws Exception
     */
    public void closeTabIgnoringChanges(String tabName) throws Exception {
        selectTab(tabName);
        final String viewId = editor.findElement(By.xpath(Locators.ACTIVE_EDITOR_TAB_LOCATOR)).getAttribute("view-id");
        clickCloseEditorButton(tabName);

        /*
         * Closing ask dialogs if them is appears.
         */
        if (IDE().ASK_DIALOG.isOpened()) {
            IDE().ASK_DIALOG.clickNo();
        } else if (IDE().ASK_FOR_VALUE_DIALOG.isOpened()) {
            IDE().ASK_FOR_VALUE_DIALOG.clickNoButton();
        } else {
            fail("Dialog has been not found!");
        }

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(String.format(Locators.EDITOR_VIEW_LOCATOR, viewId)));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }


    /** wait while into editor disappears text fragment */
    public void waitEditorWillNotContainSpecifiedText(final String text) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return !(getTextFromCodeEditor().contains(text));
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }


    /**
     * @param tabIndex
     *         index of tab, starts at 0
     * @return
     */
    public boolean isFileContentChanged(int tabIndex) {
        final String tabName = getTabTitle(tabIndex);
        return tabName.endsWith(MODIFIED_MARK);
    }

    //
    // public boolean isFileContentChanged(String title)
    // {
    // WebElement tab =
    // editor
    // .findElement(By.xpath(Locators.EDITOR_TABSET_LOCATOR + String.format(Locators.TITLE_SPAN_LOCATOR,
    // title)));
    // return tab.getText().trim().endsWith(MODIFIED_MARK);
    // }

    /**
     * Wait mark of file content modification appear (symbol "*" near title).
     *
     * @param title
     *         file's title
     */
    public void waitFileContentModificationMark(final String title) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement tab =
                            editor.findElement(By.xpath(Locators.EDITOR_TABSET_LOCATOR
                                                        + String.format(Locators.TITLE_SPAN_LOCATOR, title)));
                    return tab.getText().trim().endsWith(MODIFIED_MARK);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** @param title */
    public void waitNoContentModificationMark(final String title) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement tab =
                            editor.findElement(By.xpath(Locators.EDITOR_TABSET_LOCATOR
                                                        + String.format(Locators.TITLE_SPAN_LOCATOR, title)));
                    return !tab.getText().trim().endsWith(MODIFIED_MARK);
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }

    /**
     * Close new file. If saveFile true - save file. If fileName is null - save with default name, else save with fileName name.
     *
     * @param tabIndex
     *         - index of tab in editor panel
     * @param newFileName
     *         - name of new file
     * @throws Exception
     */
    public void saveAndCloseFile(int tabIndex, String newFileName) throws Exception {
        selectTab(tabIndex);
        final String viewId =
                editor.findElement(By.xpath(Locators.ACTIVE_EDITOR_TAB_LOCATOR))
                      .getAttribute(Locators.VIEW_ID_ATTRIBUTE);
        clickCloseEditorButton(tabIndex);

        /*
         * Saving file
         */
        if (IDE().ASK_DIALOG.isOpened()) {
            IDE().ASK_DIALOG.clickYes();
        } else if (IDE().ASK_FOR_VALUE_DIALOG.isOpened()) {
            if (newFileName != null && !newFileName.isEmpty()) {
                IDE().ASK_FOR_VALUE_DIALOG.setValue(newFileName);
            }
            IDE().ASK_FOR_VALUE_DIALOG.clickOkButton();
        } else {
            fail();
        }

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(String.format(Locators.EDITOR_VIEW_LOCATOR, viewId)));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * click on close marker wait save ska dialog, confirm saving
     *
     * @param nameOfFile
     */
    public void closeAndSaveChanges(String nameOfFile) throws Exception {
        selectTab(nameOfFile);
        clickCloseEditorButton(nameOfFile);
        IDE().ASK_DIALOG.waitOpened();
        IDE().ASK_DIALOG.clickYes();
        waitTabNotPresent(nameOfFile);
    }

    public boolean isEditorTabSelected(String tabTitle) {
        try {
            return editor.findElement(By.xpath(String.format(Locators.SELECTED_EDITOR_TAB_LOCATOR, tabTitle))) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the active state of the editor. Index starts from <code>0</code>.
     *
     * @param editorIndex
     *         editor's index
     * @return {@link Boolean} <code>true</code> if active
     */
    public boolean isActive(int editorIndex) {
        WebElement view = editor.findElement(By.xpath(String.format(Locators.EDITOR_TAB_LOCATOR, editorIndex)));
        return IDE().PERSPECTIVE.isViewActive(view);
    }

    public boolean isTabPresentInEditorTabset(String tabTitle) {
        try {
            return editor.findElement(
                    By.xpath(String.format(Locators.EDITOR_TABSET_LOCATOR + Locators.TITLE_SPAN_LOCATOR,
                                           tabTitle))) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isTabPresentInEditorTabset(int tabIndex) {
        try {
            return editor.findElement(By.xpath(Locators.EDITOR_TABSET_LOCATOR
                                               + String.format(Locators.TAB_LOCATOR, tabIndex))) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Delete pointed number of lines in editor.
     *
     * @param count
     *         number of lines to delete
     * @throws Exception
     */
    public void deleteLinesInEditor(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        }
    }

    /** Delete all file content via Ctrl+a, Delete */
    public void deleteFileContent() throws Exception {
        typeTextIntoEditor(Keys.CONTROL.toString() + "a" + Keys.DELETE.toString());
        waitContentIsClear();
    }

    /**
     * Type text to file, opened in tab.
     * <p/>
     * Index of tabs begins from 0.
     * <p/>
     * Sometimes, if you can't type text to editor, try before to click on editor:
     *
     * @param text
     *         (can be used '\n' as line break)
     */
    public void typeTextIntoEditor(String text) throws Exception {
        try {
            selectIFrameWithEditor();
            WebElement editor = driver().switchTo().activeElement();
            editor.sendKeys(text);
        } finally {
            IDE().selectMainFrame();
        }
    }

    /**
     * Move cursor in editor down to pointed number of lines.
     *
     * @param rows
     *         number of lines to move down
     * @throws Exception
     */
    public void moveCursorDown(int rows) throws Exception {
        for (int i = 0; i < rows; i++) {
            typeTextIntoEditor(Keys.ARROW_DOWN.toString());
        }
    }

    /**
     * Move cursor in editor up to pointed number of lines.
     *
     * @param rows
     *         number of lines to move up
     * @throws Exception
     */
    public void moveCursorUp(int rows) throws Exception {
        for (int i = 0; i < rows; i++) {
            typeTextIntoEditor(Keys.ARROW_UP.toString());
        }
    }

    /**
     * Move cursor in editor left to pointed number of symbols.
     *
     * @throws Exception
     */
    public void moveCursorLeft(int symbols) throws Exception {
        for (int i = 0; i < symbols; i++) {
            typeTextIntoEditor(Keys.ARROW_LEFT.toString());
        }
    }

    /**
     * Move cursor in editor right to pointed number of symbols.
     *
     * @throws Exception
     */
    public void moveCursorRight(int symbols) throws Exception {
        for (int i = 0; i < symbols; i++) {
            typeTextIntoEditor(Keys.ARROW_RIGHT.toString());
        }
    }

    /**
     * Get the locator of content panel.
     *
     * @return content panel locator
     */
    public String getContentPanelLocator() {
        return String.format(Locators.EDITOR_TAB_LOCATOR, getNumberTabOfActiveEditor());
    }

    ;

    /** Select iframe, which contains editor from tab with index tabIndex */
    public void selectIFrameWithEditor() throws Exception {
        String iFrameWithEditorLocator = getContentPanelLocator() + "//iframe";
        WebElement editorFrame = driver().findElement(By.xpath(iFrameWithEditorLocator));
        driver().switchTo().frame(editorFrame);
        waitIframe();
        driver().switchTo().frame(iframe);
    }

    /**
     * Mouse click on editor.
     *
     * @throws Exception
     */
    public void clickOnEditor() throws Exception {
        selectIFrameWithEditor();
        driver().switchTo().activeElement().click();
        IDE().selectMainFrame();
    }

    /** Get text from tab number "tabIndex" from editor */
    public String getTextFromCodeEditor() throws Exception {
        selectIFrameWithEditor();
        String text = driver().switchTo().activeElement().getText();
        IDE().selectMainFrame();
        return text;
    }

    /**
     * Wait while tab appears in editor
     *
     * @param tabIndex
     *         - index of tab, starts at 0
     * @throws Exception
     */
    public void waitTabPresent(int tabIndex) throws Exception {
        final String tab = Locators.EDITOR_TABSET_LOCATOR + String.format(Locators.TAB_LOCATOR, tabIndex);

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {

                try {
                    return input.findElement(By.xpath(tab)) != null;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait while tab appears in editor
     *
     * @param tabName
     * @throws Exception
     */
    public void waitTabPresent(String tabName) throws Exception {
        final String tab = String.format(Locators.EDITOR_TABSET_LOCATOR + Locators.TITLE_SPAN_LOCATOR, tabName);

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return input.findElement(By.xpath(tab)) != null;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait while tab appears in editor
     *
     * @throws Exception
     */
    public void waitActiveFile() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    // TODO try to avoid and wait completely opened
                    Thread.sleep(3000);
                    selectIFrameWithEditor();
                    return true;
                } catch (NoSuchElementException e) {
                    return false;
                } catch (Exception e) {
                    return false;
                } finally {
                    IDE().selectMainFrame();
                }
            }
        });
    }

    /**
     * Wait while tab disappears in editor
     *
     * @param tabIndex
     *         - index of tab, starts at 0
     * @throws Exception
     */
    public void waitTabNotPresent(int tabIndex) throws Exception {
        final String tab = Locators.EDITOR_TABSET_LOCATOR + String.format(Locators.TAB_LOCATOR, tabIndex);

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(tab));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    public void waitTabNotPresent(String fileName) throws Exception {
        final String tab = String.format(Locators.EDITOR_TABSET_LOCATOR + Locators.TITLE_SPAN_LOCATOR, fileName);

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(tab));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Check is file in tabIndex tab opened with Code Editor.
     *
     * @param tabIndex
     * @throws Exception
     */
    public void checkCodeEditorOpened(int tabIndex) throws Exception {
        String locator =
                "//div[@panel-id='editor'and @tab-index='" + tabIndex + "']//div[@class='CodeMirror-wrapping']/iframe";
        assertTrue(driver().findElement(By.xpath(locator)).isDisplayed());
    }

    /** wait line numbers visible */
    public void waitLineNumbersVisible() {
        String iFrameWithEditorLocator = getContentPanelLocator() + "//iframe";
        WebElement editorFrame = driver().findElement(By.xpath(iFrameWithEditorLocator));
        driver().switchTo().frame(editorFrame);

        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators


                                                                                                                   .LINE_NUMBERS_TAB)));

        IDE().selectMainFrame();
    }

    /** wait line numbers not visible */
    public void waitLineNumbersNotVisible() {
        String iFrameWithEditorLocator = getContentPanelLocator() + "//iframe";
        WebElement editorFrame = driver().findElement(By.xpath(iFrameWithEditorLocator));
        driver().switchTo().frame(editorFrame);

        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .xpath(Locators.LINE_NUMBERS_TAB)));

        IDE().selectMainFrame();
    }

    /**
     * Click on Source button at the bottom of editor.
     *
     * @throws Exception
     */
    public void clickSourceButton() throws Exception {
        editor.findElement(By.xpath(Locators.SOURCE_BUTTON)).click();
    }

    /**
     * Click on Design button at the bottom of editor.
     *
     * @throws Exception
     */
    public void clickDesignButton() throws Exception {
        driver().findElement(By.xpath(Locators.DESIGN_BUTTON_XPATH)).click();
        Thread.sleep(500);
        driver().findElement(By.xpath(Locators.DESIGN_BUTTON_XPATH)).click();
        Thread.sleep(500);
        driver().findElement(By.xpath(Locators.DESIGN_BUTTON_XPATH)).click();
    }

    public boolean isHighlighterPresent() {
        return highlighter != null && highlighter.isDisplayed();
    }

    /**
     * @param tabIndex
     *         editor tab with highlighter
     * @return {@link WebElement} highlighter
     */
    public WebElement getHighlighter(int tabIndex) {
        try {
            return editor.findElement(By.cssSelector(String.format(Locators.HIGHLIGHTER_SELECTOR, tabIndex)));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public void waitHighlighterInEditor(int numEditor) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.HIGHLITER_BORDER,
                numEditor))));
    }

    /**
     * Open editor's context menu
     *
     * @throws Exception
     */
    public void openContextMenu() throws Exception {
        selectIFrameWithEditor();

        new Actions(driver()).contextClick(editorCodemirr).perform();
        IDE().selectMainFrame();
    }

    /**
     * Getting of number current active tab of the code editor
     *
     * @return
     */
    public int getNumberTabOfActiveEditor() {
        return Integer.parseInt(numActiveEditor.getAttribute("view-id").replace("editor-", ""));
    }

    /**
     * if file changed close ask menu than close file if file not change close file without checking ask dialog
     *
     * @param tabIndex
     * @throws Exception
     */
    public void forcedClosureFile(int tabIndex) throws Exception {
        selectTab(tabIndex);
        final String viewId = editor.findElement(By.xpath(Locators.ACTIVE_EDITOR_TAB_LOCATOR)).getAttribute("view-id");
        clickCloseEditorButton(tabIndex);
        /*
         * Closing ask dialogs if them is appears.
         */
        if (IDE().ASK_DIALOG.isOpened()) {
            IDE().ASK_DIALOG.clickNo();
        } else if (IDE().ASK_FOR_VALUE_DIALOG.isOpened()) {
            IDE().ASK_FOR_VALUE_DIALOG.clickNoButton();
        } else

        {
            new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver input)

                {
                    try {
                        input.findElement(By.xpath(String.format(Locators.EDITOR_VIEW_LOCATOR, viewId)));
                        return false;
                    } catch (NoSuchElementException e) {
                        return true;
                    }
                }
            });
        }
    }


    /**
     * if file changed close ask menu than close file if file not change close file without checking ask dialog
     *
     * @param fileName
     * @throws Exception
     */
    public void forcedClosureFile(String fileName) throws Exception {
        selectTab(fileName);
        final String viewId = editor.findElement(By.xpath(Locators.ACTIVE_EDITOR_TAB_LOCATOR)).getAttribute("view-id");
        clickCloseEditorButton(fileName);
        /*
         * Closing ask dialogs if them is appears.
         */
        if (IDE().ASK_DIALOG.isOpened()) {
            IDE().ASK_DIALOG.clickNo();
        } else if (IDE().ASK_FOR_VALUE_DIALOG.isOpened()) {
            IDE().ASK_FOR_VALUE_DIALOG.clickNoButton();
        } else

        {
            new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver input)

                {
                    try {
                        input.findElement(By.xpath(String.format(Locators.EDITOR_VIEW_LOCATOR, viewId)));
                        return false;
                    } catch (NoSuchElementException e) {
                        return true;
                    }
                }
            });
        }
    }
}
