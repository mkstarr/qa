package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.JsonHelper;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertTrue;

/** @author Musienko Maxim */
public class FactoryURLWithoutCommitIDTest extends BaseTest {

    private static final String PROJECT = "factoryUrlRepo" + System.currentTimeMillis();

    private String PUBLIC_ORGANIZATION_REPO = "git@github.com:exoinvitemain/factoryUrlRepo.git";

    private String factoryParams = "&action=openproject&ptype=Spring&vcsinfo=true";

    private String URLToLastCommit = "https://api.github.com/repos/exoinvitemain/factoryUrlRepo/git/refs/heads/master";

    private String currentHeadCommitHash;

    private JsonHelper jsonHelper;

    public FactoryURLWithoutCommitIDTest() throws Exception {
        currentHeadCommitHash = getLastCommitFromGitHub(URLToLastCommit);
    }


    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void clonePublicOrganizationRepositoryTest() throws Exception {

        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        // clone public organization test repository with default remote name.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(PUBLIC_ORGANIZATION_REPO);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(PROJECT);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(PUBLIC_ORGANIZATION_REPO + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        IDE.LOADER.waitClosed();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.FACTORY_URL.clickOnFinishButtonInFactoryURLForm();
        IDE.LOGIN.logout();

        jsonHelper = new JsonHelper(factoryURL);
        // building v1 factory url
        String factory_url_v1 =
                PROTOCOL + "://" + IDE_HOST + "/factory?v=1.0&pname=" + PROJECT + "&wname=" + TENANT_NAME + "&vcs=git&vcsurl=" +
                jsonHelper.getValueByKey("vcsurl") + factoryParams;

        driver.get(factory_url_v1);
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();


        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        assertTrue(IDE.GIT.getCommitRevisionFromHistoryPanel().contains(currentHeadCommitHash));


        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitBranchIsCheckout("master");


    }

    private String getLastCommitFromGitHub(String APIURL) throws Exception {
        URL url = new URL(APIURL);
        String regexp = "\\w{40}";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher;
        String commit = null;
        StringBuilder build = new StringBuilder();

        HttpURLConnection connect = (HttpURLConnection)url.openConnection();
        connect.setRequestMethod("GET");
        connect.setDoInput(true);
        while (connect.getInputStream().available() != 0) {
            build.append(((char)connect.getInputStream().read()));
        }
        connect.disconnect();
        commit = build.toString();
        matcher = pattern.matcher(commit);
        if (matcher.find())
            commit = matcher.group();
        return commit;
    }
}