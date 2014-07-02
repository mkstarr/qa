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

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author <a href="mailto:azatsarynnyy@exoplatform.org">Artem Zatsarynnyy</a>
 *
 */
public class Build extends AbstractTestModule {
    /** @param ide */
    public Build(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    public interface Messages {
        String BUILDING_PROJECT = "Building project";

        String BUILD_IN_PROGRESS = "You can not start the build of two or more projects at the same time";

        String BUILD_SUCCESS = "Result: Successful";
        
        String URL_TO_ARTIFACT = "You can download your artifact :http://";

        String DEPENDENCY_MESS = "Dependency for your pom:";
    }

    private interface Locators {

        String VIEW_ID = "ide.builder.build.view";

        String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        String BUILD_PANEL_ID = "ide.builder.buildOutput";
        
        String BUILD_PANEL_ALL_TEXT = "//div[@view-id='ide.builder.build.view']";

        String CLEAR_BUTTON_SELECTOR = "div[view-id='" + VIEW_ID + "'] div[title='Clear Output']>img";

        String LINK_ON_BUILT_RESULT = "//pre[@id='ide.builder.buildOutput']//a[text()='here']";

        String BUILDER_OUTPUT_TAB = "//div[@id='operation']//td[@class='tabTitleText' and text()='Build project']";

        String OPERATION_FORM = "//div[@id='operation']/ancestor::div[contains(@style, 'height: 300')]";

        String CLOSE_BUILD_PANEL = "//div[@tab-title='Build project']";
        
        String BUILD_PANEL_TITLE_XPATH = "//*[@class='tabTitleText'][contains(text(), 'Build project')]";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(xpath = Locators.LINK_ON_BUILT_RESULT)
    private WebElement buildResultLink;

    @FindBy(id = Locators.BUILD_PANEL_ID)
    private WebElement buildPanel;

    @FindBy(css = Locators.CLEAR_BUTTON_SELECTOR)
    private WebElement clearButton;

    @FindBy(xpath = Locators.BUILDER_OUTPUT_TAB)
    private WebElement builderOutputTab;

    @FindBy(xpath = Locators.OPERATION_FORM)
    private WebElement operationForm;

    @FindBy(xpath = Locators.CLOSE_BUILD_PANEL)
    private WebElement closeBuild;
    
    @FindBy(xpath = Locators.BUILD_PANEL_TITLE_XPATH)
    private WebElement buildPanelTitle;

    /**
     * Wait build project view opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return view != null && view.isDisplayed();
            }
        });
    }

    /**
     * Wait build output panel project opened.
     *
     * @throws Exception
     */
    public void waitBuildResultLink() throws Exception {
        new WebDriverWait(driver(), 120).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return buildResultLink != null && buildResultLink.isDisplayed();
            }
        });
    }

    
    /**
     * Wait build output panel project opened.
     *
     * @throws Exception
     */
    public void waitBuildOutputPanelOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return buildPanel != null && buildPanel.isDisplayed() && operationForm.isDisplayed()
                       && operationForm != null;
            }
        });
    }

    /**
     * Wait build project view closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return view == null || !view.isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /** Check is build project view opened. */
    public boolean isOpened() {
        try {
            return view != null && view.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Check is build tab is present, but build panel isn't opened. */
    public boolean isTabPresentButNotOpened() {
        try {
            return !isOpened() && buildPanelTitle != null && buildPanelTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    
    /**
     * Get output message.
     *
     * @return {@link String} text of the message
     */
    public String getOutputMessage() {
        return buildPanel.getText();
    }

    /** Click clear build output button. */
    public void clickClearButton() {
        clearButton.click();
    }

    /** wait for message in builder output */
    public void waitBuilderMessage(final String mess) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                String message = buildPanel.getText();
                return message.contains(mess);
            }
        });
    }

    /** wait for message in builder output
     * Default timeout = 30 sec.
     */
    public void waitBuilderContainText(final String mess) {
        waitBuilderContainText(mess, 30);
    }

    /** wait for message in builder output */
    public void waitBuilderContainText(final String mess, int timeoutInSec) {
        new WebDriverWait(driver(), timeoutInSec).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(By.xpath(Locators.BUILD_PANEL_ALL_TEXT)).getText().contains(mess);
            }
        });
    }
    
    /** select builder output tab */
    public void selectBuilderOutputTab() {
        builderOutputTab.click();
    }

    public void closeBuildPanel() {
        closeBuild.click();
    }
    
    /**
     * Clear opened panel
     */
    public void clearPanel() {
        if (isOpened()) {
            selectBuilderOutputTab();
            clickClearButton();
        }
    }
}
