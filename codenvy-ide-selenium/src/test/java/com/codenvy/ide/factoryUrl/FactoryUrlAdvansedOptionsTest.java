package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/** @author Musienko Maxim */
public class FactoryUrlAdvansedOptionsTest extends BaseTest {

    private static final String PROJECT = "factory_prj";


    String defaultHtmlText = "<title>Sample Page</title>";

    private final String logoPath =
            "src/test/resources/org/exoplatform/ide/factory/vini.png";

    private File pathToImage = new File(logoPath);

    private final String uploadedImgPrefix =
            "data:image/png;base64," +
            "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAKJ2lDQ1BpY2MAAEjHnZZ3VFTXFofPvXd6oc0w0hl6ky4wgPQuIB0EURhmBhjKAMMMTWyIqEBEEREBRZCggAGjoUisiGIhKKhgD0gQUGIwiqioZEbWSnx5ee/l5ffHvd/aZ+9z99l7n7UuACRPHy4vBZYCIJkn4Ad6ONNXhUfQsf0ABniAAaYAMFnpqb5B7sFAJC83F3q6yAn8i94MAUj8vmXo6U+ng/9";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/project/JavaScriptAutoComplete.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void factoryUrlAdvansedOptionsTest() throws Exception {
        //go to the IDE, call Factory URL, check init states into customize section
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + "js");
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickAdvancedOptionsDropIcon();
        IDE.FACTORY_URL.uploadLogoImage(pathToImage.getAbsolutePath());
        System.out.println("------------------------------------------compare-Base64-prefix-in-the-test" + uploadedImgPrefix);
        IDE.FACTORY_URL.waitUploadedImage(uploadedImgPrefix);
        IDE.FACTORY_URL.waitDownloadedImageNameInLogoField("vini.png");
        checkAllQuestionsMark();
        IDE.FACTORY_URL.typeDescription("Selenium description " + new Date());
        checkOpenFileAfterLaunch();

        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryWeb = IDE.FACTORY_URL.getOnWebSiteURL();
        String factoryUrl = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.FACTORY_URL.clickOnFinishButtonInFactoryURLForm();
        IDE.EXPLORER.openItem(PROJECT + "/index.html");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(9);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(factoryWeb);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark("index.html");
        IDE.TOOLBAR.runCommand(ToolbarCommands.Run.SHOW_PREVIEW);
        checkFactoryUrlWithLogo();
        driver.get(factoryUrl);
        IDE.EXPLORER.waitOpened();
        IDE.FACTORY_URL.waitWelcomePanelTitle("Discover");
        IDE.EDITOR.waitTabPresent("index.html");
        IDE.EDITOR.selectTab("index.html");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(defaultHtmlText);
    }

    private void checkOpenFileAfterLaunch() {
        IDE.FACTORY_URL.clickOnOpenAfterLaunch();
        IDE.OPEN_RESOURCE.waitOpenResouceFormIsOpen();
        IDE.OPEN_RESOURCE.typeToSearchField("in");
        IDE.OPEN_RESOURCE.waitFoundFiles("index.html");
        IDE.OPEN_RESOURCE.selectItemInMatchList("index.html");
        IDE.OPEN_RESOURCE.clickOnOpenBtn();
        IDE.OPEN_RESOURCE.waitOpenResouceFormIsClosed();
    }

    private void checkAllQuestionsMark() {
        //check all question marks and appearance of the titles
        IDE.FACTORY_URL.moveCursorToLogoQuestMark();
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromLogo("Create and embed a custom Factory");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromLogo("button with your own logo");

        IDE.FACTORY_URL.moveCursorToDescriptionQuestMark();
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromDescription("Specify a description of your Factory");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromDescription("to all users");

        IDE.FACTORY_URL.moveCursorToContactEmailQuestMark();
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromContactEmail("Include your email informations");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromContactEmail("and let users able to contact you");

        IDE.FACTORY_URL.moveCursorToAuthorQuestMark();
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromAuthor("Include your author informations");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromAuthor("and let everyone know that");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromAuthor("you originated this Factory");


        IDE.FACTORY_URL.moveCursorToOpenAfterLaunchQuestMark();
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromOpenAfterLaunch("Define the file to open once");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromOpenAfterLaunch("the project will be factory-created");


        IDE.FACTORY_URL.moveCursorToOrganizationIdQuestMark();
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromOrganizationId("Specify the Organization ID");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromOrganizationId("that we provide you to include this factory");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromOrganizationId("in reports we generate for you");


        IDE.FACTORY_URL.moveCursorToAffiliateIdQuestMark();
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromAffiliateIdQuestMark("Use your AffiliateID and earn up to 10%");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromAffiliateIdQuestMark("on Codenvy sales from traffic that is sent to Codenvy");
        IDE.FACTORY_URL.waitAppearQuestMarkTextFromAffiliateIdQuestMark("from embedded IDEs that you include on your site");

    }

    //
    private void checkFactoryUrlWithLogo() {
        IDE.PREVIEW.selectPreviewHtmlIFrameWithFactoryImage();

        //check that uploaded logo present into just created factory url
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            String viniImgLocator = "//div[@class='advanced-factory-noted']/img";

            @Override
            public Boolean apply(WebDriver input) {
                return driver.findElement(By.xpath(viniImgLocator)).getAttribute("src").contains("image?imgId=");
            }
        });


        //check background uploaded logo present into just created factory url
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            String factoryBackGroundImgLocator = "//div[@class='advanced-factory-noted']/div";
            String backgroundBase64Part =
                    "url(data:image/png;base64," +
                    "iVBORw0KGgoAAAANSUhEUgAAAG4AAABuCAYAAADGWyb7AAAACXBIWXMAAA";

            @Override
            public Boolean apply(WebDriver input) {
                return driver.findElement(By.xpath(factoryBackGroundImgLocator)).getCssValue("background-image")
                             .contains(backgroundBase64Part);
            }
        });

    }
}
