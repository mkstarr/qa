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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * @author Musienko Maxim
 *
 */
public class ConfigureDataSource extends AbstractTestModule {
    public ConfigureDataSource(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW                                         = "ideConfigureDatasourceView-window";

        String OK_BUTTON_DISABLED                           = "//div[@id='ide.configure_datasource.button.ok' and @button-enabled='false']";

        String OK_BUTTON_ENABLED                            = "//div[@id='ide.configure_datasource.button.ok' and @button-enabled='true']";

        String CANCEL_BUTTON_ENABLED                        =
                                                              "//div[@id='ide.configure_datasource.button.cancel' and @button-enabled='true']";

        String CANCEL_BUTTON_DISABLED                       =
                                                              "//div[@id='ide.configure_datasource.button.cancel' and @button-enabled='true']";

        String VIEW_ID                                      = "//div[@view-id='ideConfigureDatasourceView']";

        String ADD_DATASOURCE_BUTTON                        =
                                                              "//img[contains(@style, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABqElEQVR42rWSyUtCURTGL+JA5pAZtGgRQf9HmzYtJdq6bdcuaqMgSC0SqSjhRT59UUSRRa1CaIKi2jSApQTJa6CCes8Gs4jw5HerRwOWi7rwg4/vfOfAvfcw9i/Hx1z6sD5ZJTkVAA2vtOYgKzMJJnnqZJSOH1McaHio/T5AYHanVKkePu3SFaU50PBQK97Yz0xsgDnZIKu3ilZ1+36F5PwWBxoeajyD7Lc7D+tTlZJDcYgVat14bX4zO0/7z8scaHioIYPspzcxCUY5lO6i1btpWrieoKWbSdrIzdL6wzQHGh5qyITS3YQebYBDsmfi1xKt5EZoMRvlxG/DBcQ3wpqPDLLo0QZURG2Z2GUvzWWCNKMGODGlp0DgjR7NRwZZ9GgDjEMG2ZtoJenMR8MnHhJPvTRy4SXxvJPDdcFDDRlk0fPx7106QZeyRyyKVSxXa8aq833pNuo/auVAw0MNGWTRU/QbzWKZ6k+5yX/Q8kpBwyv+jV8WyRYpVz17zdSRaORAw/t5kT6sskEwyO61BmrfaeJAwyttld/fZEiXtIhmBUB/v/MfnRcSPFdfxuX26wAAAABJRU5ErkJggg==')]";
        String DELETE_DATASOURCE_BUTTON_DISABLED            =
                                                              "//img[contains(@style, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAl0lEQVR42u2SMQrEIBBFcyDBSqxE9AY5mJcQ7GyDNxAELazS7gECOcJsfrFCGtkiZQYegrz/YWCW5Z1nhjG2Sil3a+1hjJkCBy4yo+D6+MQYqfdOrbUpcOAiMwqu5jPnTLVWKqVMgQMXmVGgtT63baOUEuGd8XOQua3gnKMQAnnvp8CBe1uBc74KIXal1PEPcJF5z++h+QITuM9rWGVhyQAAAABJRU5ErkJggg==')]";
        String DELETE_DATASOURCE_BUTTON_ENABLED             =
                                                              "//img[contains(@style, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAqElEQVR42mNgGAVUAo0MgSxzWa4LLxR6I7hQAC8GqQGpBemB62efyfZg1sOu/ye/b/l/+Ns6vBikBqQWpAduANDk93s/Lfl/5Oey/we/L8GLQWpAakF64AYILOB7v+pF7/91b7r/r3ndiReD1IDUgvTADWCbyfqg4nzi/5l3K/9PuVOKF4PUgNSC9CACsY8hkGkW03W++dxveOfhxyA1ILUgPaOpj0oAAAy0z2U5SR4wAAAAAElFTkSuQmCC')]";

        String ADD_DATASOURCE_BUTTON_DISABLED               =
                                                              "//img[contains(@style, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAqElEQVR42mNgGAVUAo0MgSxzWa4LLxR6I7hQAC8GqQGpBemB62efyfZg1sOu/ye/b/l/+Ns6vBikBqQWpAduANDk93s/Lfl/5Oey/we/L8GLQWpAakF64AYILOB7v+pF7/91b7r/r3ndiReD1IDUgvTADWCbyfqg4nzi/5l3K/9PuVOKF4PUgNSC9CACsY8hkGkW03W++dxveOfhxyA1ILUgPaOpj0oAAAy0z2U5SR4wAAAAAElFTkSuQmCC')]";

        String NAME_FIELD                                   = "//input[@title='Configuration name']";

        String URL_FIELD                                    = "//input[@datasource-option-name='url']";

        String USERNAME_FIELD                               = "//input[@datasource-option-name='username']";

        String PASSWORD_FIELD                               = "//input[@datasource-option-name='password']";

        String DRIVER_CLASSNAME_FIELD                       = "//input[@datasource-option-name='driverClassName']";

        String DEFAULT_AUTOCOMIT_FIELD                      = "//input[@datasource-option-name='defaultAutoCommit']";

        String INITIAL_SIZE_FIELD                           = "//input[@datasource-option-name='initialSize']";

        String MAX_ACTIVE_FIELD                             = "//input[@datasource-option-name='maxActive']";

        String MAX_IDLE_FIELD                               = "//input[@datasource-option-name='maxIdle']";

        String MIN_IDLE_FIELD                               = "//input[@datasource-option-name='minIdle']";

        String MAX_WAIT_FIELD                               = "//input[@datasource-option-name='maxWait']";

        String POOL_PREPARED_STATEMENTS_FIELD               = "//input[@datasource-option-name='poolPreparedStatements']";

        String MAX_OPEN_PREPARED_STATEMENTS                 = "//input[@datasource-option-name='maxOpenPreparedStatements']";

        String DATASOURCE_BY_NAME                           = "//div[@id='ideConfigureDatasourceView-window']//table//div[text()='%s']";

        String ASK_FOR_VALUE_DATASOURCE_FORM                = "codenvyAskForValueModalView-window";

        String ASK_FOR_VALUE_DATASOURCE_NAME_FIELD          = "//div[@view-id='codenvyAskForValueModalView']//input[@name='valueField']";

        String ASK_FOR_VALUE_DATASOURCE_FORM_OK_BTN_CSS     = "div#codenvyAskForValueModalView-window div#OkButton";

        String ASK_FOR_VALUE_DATASOURCE_FORM_CANCEL_BTN_CSS = "div#codenvyAskForValueModalView-window div#CancelButton";

        String LABELS_OF_FIELDS                             =
                                                              "//div[@id='ideConfigureDatasourceView-window']//table//td[text()='Name']/parent::tr/parent::tbody";


    }

    @FindBy(id = Locators.VIEW)
    private WebElement configureDataSourceView;

    @FindBy(xpath = Locators.ADD_DATASOURCE_BUTTON)
    private WebElement addNewBaseBtn;

    @FindBy(id = Locators.ASK_FOR_VALUE_DATASOURCE_FORM)
    private WebElement askForValueDBForm;

    @FindBy(css = Locators.ASK_FOR_VALUE_DATASOURCE_FORM_OK_BTN_CSS)
    private WebElement askForValueDBFormOkBtn;

    @FindBy(xpath = Locators.ASK_FOR_VALUE_DATASOURCE_NAME_FIELD)
    private WebElement askForValueDataSourceNameFieald;

    @FindBy(xpath = Locators.URL_FIELD)
    private WebElement URLField;

    @FindBy(xpath = Locators.USERNAME_FIELD)
    private WebElement userNameField;

    @FindBy(xpath = Locators.PASSWORD_FIELD)
    private WebElement passwordField;

    @FindBy(xpath = Locators.DRIVER_CLASSNAME_FIELD)
    private WebElement driverClassNameField;

    @FindBy(css = Locators.ASK_FOR_VALUE_DATASOURCE_FORM_CANCEL_BTN_CSS)
    private WebElement askForValueDBFormCancelBtn;

    @FindBy(xpath = Locators.LABELS_OF_FIELDS)
    private WebElement allLabels;

    @FindBy(xpath = Locators.OK_BUTTON_ENABLED)
    private WebElement okBtnEnabled;

    @FindBy(xpath = Locators.OK_BUTTON_DISABLED)
    private WebElement okBtnDisabled;

    @FindBy(xpath = Locators.CANCEL_BUTTON_DISABLED)
    private WebElement cancelBtnDisabled;

    @FindBy(xpath = Locators.CANCEL_BUTTON_ENABLED)
    private WebElement cancelBtnEnabled;

    @FindBy(xpath = Locators.DELETE_DATASOURCE_BUTTON_ENABLED)
    private WebElement deleteDataSourceEnabled;


    /**
     * wait while configure data source form appears
     */
    public void waitConfigireViewForm() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(configureDataSourceView));
    }


    /**
     * wait ok Btn beings is ebabled
     */
    public void waitOkBtnIsEnabled() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(okBtnEnabled));
    }


    /**
     * wait while configure data source form disappear
     */
    public void waitConfigireFormDisappear() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.VIEW)));
    }

    /**
     * wait while appear ask for value new DB form
     */
    public void waitAskForValueNewDBForm() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(askForValueDBForm));
    }


    /**
     * wait while disappear ask for value new DB form
     */
    public void waitAskForValueNewDBFormIsClosed() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.ASK_FOR_VALUE_DATASOURCE_FORM)));
    }


    /**
     * click on add new base ("+") button on the configure database form
     */
    public void clickOnAddButton() {
        addNewBaseBtn.click();
    }


    /**
     * wait while delete data source button changes on active state
     */
    public void waitDelDbButtonIsActive() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(deleteDataSourceEnabled));
    }

    /**
     * click on delete base ("-") button on the configure database form
     */
    public void clickOnDelDBButton() {
        waitDelDbButtonIsActive();
        deleteDataSourceEnabled.click();
    }

    /**
     * click on ok btn on ask for value add new DB form
     */
    public void clickOnOkAskForValBtn() {
        askForValueDBFormOkBtn.click();
    }

    /**
     * click on cancel btn on ask for value add new DB form
     */
    public void clickOnCancelAskForValBtn() {
        askForValueDBFormCancelBtn.click();
    }

    /**
     * click on cancel btn on ask for value add new DB form
     */
    public void clickOkBtn() {
        waitOkBtnIsEnabled();
        okBtnEnabled.click();
        waitConfigireFormDisappear();
    }


    /**
     * select in database List database by name
     * 
     * @param nm
     */
    public void selectDataBaseByName(String nm) {
        driver().findElement(By.xpath(String.format(Locators.DATASOURCE_BY_NAME, nm))).click();
    }

    /**
     * wait appearance data source with specified name
     */
    public void waitNameIsPresentInDataSourceList(final String name) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.DATASOURCE_BY_NAME,
                                                                                                                   name))));
    }

    /**
     * wait appearance data source with specified name
     */
    public void waitDisappearNameDataSourceList(final String name) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(Locators.DATASOURCE_BY_NAME,
                                                                                                                     name))));
    }

    /**
     * type URL into require url field
     * 
     * @param URL
     */
    public void typeURLField(String URL) {
        URLField.sendKeys(URL);
    }

    /**
     * type into user field name
     * 
     * @param URL
     */
    public void typeUserNameField(String name) {
        userNameField.sendKeys(name);
    }


    /**
     * type into password field password
     * 
     * @param URL
     */
    public void typePasswordField(String name) {
        passwordField.sendKeys(name);
    }

    /**
     * set new name for data source
     * 
     * @param URL
     */
    public void typeNameOfDataSource(String name) {
        askForValueDataSourceNameFieald.clear();
        askForValueDataSourceNameFieald.sendKeys(name);
    }


    /**
     * type into driver name field name of a driver
     * 
     * @param URL
     */
    public void typeDriverClassField(String drvName) {
        driverClassNameField.sendKeys(drvName);
    }


    /**
     * get all text from labels in Configure Datasource form
     * 
     * @return
     */
    public String getAllTextOfLabels() {
        return allLabels.getText();
    }


}
