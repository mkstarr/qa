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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 */
public class Preview extends AbstractTestModule {

    /** @param ide */
    public Preview(com.codenvy.ide.IDE ide) {
        super(ide);
    }


    private static final String GADGET_PREVIEW = "//div[@view-id='gadgetpreview']";

    private static final String HTML_PREVIEW = "//div[@view-id='idePreviewHTMLView']";

    // XXX: only for groovy template and google gadgets preview
    private static final String PREVIEW_FRAME_ID = "eXo-IDE-preview-frame";

    private static final String PREVIEW_HTML_FRAME_ID = "//iframe[@class='gwt-Frame']";

    private static final String GADGET_PREVIEW_IFRAME = GADGET_PREVIEW + "//iframe";

    private static final String VIEW_TITLE = "Preview";

    private static final String PREVIEW_TABLE =
            "//table[@cellspacing='1' and @cellpadding='1' and @style]/tbody/tr[%s]/td[%s]";

    private static final String GADGET_PREVIEW_TITLE = "gadgets-gadget-title";

    private static final String OPERATION_FORM =
            "//div[@id='operation']/ancestor::div[contains(@style, 'height: 300')]";

    @FindBy(xpath = HTML_PREVIEW)
    private WebElement htmlPreview;

    @FindBy(xpath = PREVIEW_HTML_FRAME_ID)
    private WebElement htmlIframePreview;

    @FindBy(xpath = GADGET_PREVIEW)
    private WebElement gadgetPreview;

    @FindBy(xpath = GADGET_PREVIEW_IFRAME)
    private WebElement gadgetIframe;

    @FindBy(className = GADGET_PREVIEW_TITLE)
    private WebElement gadgetPreviewTitle;

    @FindBy(tagName = "body")
    private WebElement body;

    @FindBy(xpath = OPERATION_FORM)
    private WebElement operationForm;

    /**
     * Wait for HTML preview view opened.
     *
     * @throws Exception
     */
    public void waitHtmlPreviewOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return (operationForm != null && operationForm.isDisplayed() && htmlPreview != null && htmlPreview
                            .isDisplayed());
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait for HTML preview view closed.
     *
     * @throws Exception
     */
    public void waitHtmlPreviewClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return htmlPreview == null || !htmlPreview.isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Wait for Gadget preview view opened.
     *
     * @throws Exception
     */
    public void waitGadgetPreviewOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return (operationForm != null && operationForm.isDisplayed() && gadgetPreview != null &&
                            gadgetPreview
                                    .isDisplayed());
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }


    /** get all context into preview and check not empty */
    public void waitWhilePreviewIsNotEmpty() {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    selectGadgetPreviewIframe();
                    return getPreviewContent().length() > 0;
                } catch (Exception e) {
                    return false;
                } finally {
                    IDE().selectMainFrame();
                }
            }
        });

    }

    /**
     * Wait for Gadget preview view closed.
     *
     * @throws Exception
     */
    public void waitGadgetPreviewClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return gadgetPreview == null || !gadgetPreview.isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }


    public void waitForCloseButton() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    WebElement closeButton = IDE().PERSPECTIVE.getCloseViewButton(VIEW_TITLE);
                    return closeButton != null && closeButton.isDisplayed() && closeButton.isEnabled();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * wait is table in preview
     *
     * @param row
     * @param cell
     * @return
     */
    public void waitTablePresent(int row, int cell) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                PREVIEW_TABLE, row, cell))));
    }

    /**
     * Returns opened state of HTML preview view.
     *
     * @return {@link Boolean} opened state of HTML preview
     */
    public boolean isHtmlPreviewOpened() {
        try {
            return operationForm != null && operationForm.isDisplayed() && htmlPreview != null
                   && htmlPreview.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    /**
     * Returns opened state of Gadget preview view.
     *
     * @return {@link Boolean} opened state of Gadget preview
     */
    public boolean isGadgetPreviewOpened() {
        try {
            return operationForm != null && operationForm.isDisplayed() && gadgetPreview != null
                   && gadgetPreview.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    /**
     * Returns active state of HTML preview view.
     *
     * @return {@link Boolean} active state of HTML preview
     */
    public boolean isHtmlPreviewActive() {
        return IDE().PERSPECTIVE.isViewActive(htmlPreview);
    }


    /**
     * Returns active state of Gadget preview view.
     *
     * @return {@link Boolean} active state of Gadget preview
     */
    public boolean isGadgetPreviewActive() {
        return IDE().PERSPECTIVE.isViewActive(gadgetPreview);
    }

    /** Select preview frame. */
    public void selectPreviewIFrame() {
        driver().switchTo().frame(PREVIEW_FRAME_ID);
    }

    public String getPreviewContent() {
        return body.getText();
    }

    /** Select preview HTML frame. */
    public void selectPreviewHtmlIFrameWithFactoryImage() {
        driver().switchTo().frame(htmlIframePreview);
        WebElement innerFrame = driver().findElement(By.tagName("iframe"));
        driver().switchTo().frame(innerFrame);
    }


    /** Select preview HTML frame. */
    public void selectPreviewHtmlIFrame() {
        driver().switchTo().frame(htmlIframePreview);
    }


    public void selectGadgetPreviewIframe() {
        driver().switchTo().frame(gadgetIframe);
        driver().switchTo().frame(driver().findElement(By.tagName("iframe")));
    }

    /**
     * return of the text of the title Google Gadget from preview panel
     *
     * @return
     */
    public String getTitlePreview() {
        driver().switchTo().frame(gadgetIframe);
        String txt = gadgetPreviewTitle.getText();
        IDE().selectMainFrame();
        return txt;
    }

    public void closeView() {
        IDE().PERSPECTIVE.getCloseViewButton(VIEW_TITLE).click();
    }


}
