package com.codenvy.sitecldide;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.IDE;
import com.codenvy.sitecldide.core.ProfilePage;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/**
 * @author Musienko Maxim
 *
 */
public class CheckUpdatedProfileTst extends BaseCLDIDE {
    @Test
    public void checkSettedEarlyNames() throws Exception {
        IDE ide = new IDE("", this.driver);
        driver.get(BaseTest.LOGIN_URL);
        ide.LOGIN.waitTenantLoginPage();
        ide.LOGIN.tenantLogin(BaseCLDIDE.MAIL_BOX_LOGIN, BaseCLDIDE.MAIL_BOX_PASSWORD);
        ide.EXPLORER.waitOpened();
        ide.GET_STARTED_WIZARD.waitAndCloseWizard();
        String currentWin = driver.getWindowHandle();
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Profile")));
        driver.findElement(By.linkText("Profile")).click();
        ide.DEBUGER.waitOpenedSomeWin();
        switchToWin(currentWin);
        CLDIDE.PROFILE.waitProfilePage();
        CLDIDE.PROFILE.waitFirstNameFieldWitSpecificText(MAIL_BOX_LOGIN.substring(0, MAIL_BOX_LOGIN.indexOf('@')));
        CLDIDE.PROFILE.waitLastNameFieldWitSpecificText(MAIL_BOX_LOGIN.substring(0, MAIL_BOX_LOGIN.indexOf('@')) + "lastMame");
        CLDIDE.PROFILE.waitPhoneNumberFieldWithText("86645678");
        CLDIDE.PROFILE.waitCompanyFieldWithSpecificText("codenvy");
        CLDIDE.PROFILE.waitRoleListdWithSpecificText(ProfilePage.JobList.OTHER);
    }

    public void switchToWin(String winHandl) {
        Set<String> driverWindows = driver.getWindowHandles();
        for (String wins : driverWindows) {
            if (!wins.equals(winHandl)) {
                driver.switchTo().window(wins);

            }
        }
    }

}
