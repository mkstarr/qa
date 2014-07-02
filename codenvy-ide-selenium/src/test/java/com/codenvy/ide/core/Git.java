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

import com.codenvy.ide.IDE;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 @author Roman Iuvshin
 *
 */
public class Git extends AbstractTestModule {

    public Git(IDE ide) {
        super(ide);
    }

    private interface Locators {

        interface InitializeRepository {
            String INITIALIZATION_FORM = "//div[@view-id='ideInitRepositoryView']";

            String BARE_REPOSITORY = "ideInitRepositoryViewBareField";

            String INITIALIZATION_OK_BUTTON_ID = "ideInitRepositoryViewInitButton";

            String INITIALIZATION_CANCEL_BUTTON_ID = "ideInitRepositoryViewCancelButton";

            String INITIALIZATION_REPOSITORY_NAME = "ideInitRepositoryViewWorkDirField";
        }

        interface Branches {
            String BRANCHES_VIEW_ID = "ideBranchView-window";

            String MAXIMIZE_BRANCH_WINDOW = "//div[@id='ideBranchView-window']//img[@title='Close']";

            String CLOSE_BRANCH_WINDOW = "//div[@id='ideBranchView-window']//img[@title='Maximize']";

            String TABLE_BRANHES = "ideBranchGrid";

            String DISABLED_CHECKOUT_BTN_CSS = "div#ideBranchViewCheckoutButton[button-enabled='false']";

            String ENABLED_CHECKOUT_BTN_CSS = "div#ideBranchViewCheckoutButton[button-enabled='true']";

            String DELETE_BRANCH_DISABLED_BTN_CSS = "div#ideBranchViewDeleteButton[button-enabled='false']";

            String DELETE_BRANCH_ENABLED_BTN_CSS = "div#ideBranchViewDeleteButton[button-enabled='true']";

            String CLOSE_BRANCH_BTN_ID = "ideBranchViewCloseButton";

            String CREATE_BRANCH_BTN_ID = "ideBranchViewCreateButton";

            String NAME_BRANCH = "//table[@id='ideBranchGrid']//div[contains(., '%s')]";

            String ACTIVE_BRANCH = "//table[@id='ideBranchGrid']//div[contains(., '%s')]/img";

            String CREATE_NEW_BRANCH_ID = "codenvyAskForValueModalView-window";

            String OK_BUTTON_ID = "OkButton";

            String CANCEL_BTN_ID = "CancelButton";

            String INPUT_NEW_BRANCH =
                    "//div[@view-id='codenvyAskForValueModalView']//span[text()='Type branch name:']/ancestor::div[1]/input";

            String RENAME_BRANCH_BUTTON_ID = "ideBranchViewRenameButton";

            String RENAME_BRANCH_BUTTON_DISABLED = "//div[@id='ideBranchViewRenameButton' and @button-enabled='false']";

            String DELETE_BRANCH_BUTTON_DISABLED = "//div[@id='ideBranchViewDeleteButton' and @button-enabled='false']";

            String RENAME_BRANCH_INPUT_NAME =
                    "//div[@view-id='codenvyAskForValueModalView']//span[text()='Type new name of branch']/ancestor::div[1]/input";
        }


        interface GitUrl {
            String GIT_URL_FORM = "//div[@view-id='ideGitUrlView']";

            String GIT_URL = "//div[@view-id='ideGitUrlView']//input";

            String GIT_URL_CLOSE_BUTTON_ID = "ideGitUrlOkButton";
        }

        interface CloneRepository {
            String GIT_CLONE_FORM = "//div[@view-id='ideCloneRepositoryView']";

            String GIT_CLONE_FORM_REMOTE_REPO_URL = "ideCloneRepositoryViewRemoteUriField";

            String GIT_CLONE_FORM_PROJECT_NAME = "ideCloneRepositoryViewProjectNameField";

            String GIT_CLONE_FORM_REMOTE_NAME = "ideCloneRepositoryViewRemoteNameField";

            String GIT_CLONE_FORM_CLONE_BUTTON = "ideCloneRepositoryViewCloneButton";

            String GIT_CLONE_FORM_CANCEL_BUTTON = "ideCloneRepositoryViewCancelButton";
        }

        interface ShowHistory {
            String GIT_SHOW_HISTORY_FORM = "//div[@view-id='ideHistoryView']";

            String REVISION_GRID_ID = "ideRevisionGrid";

            String CLOSE_HISTORY_VIEW = "//div[@button-name='close-tab' and @tab-title='History']";

            String SELECT_COMMIT = "//table[@id='ideRevisionGrid']/tbody//div[text()='%s']";

            String REV_A = "//div[@id='information']//td[text()='Rev.A:']/..//input";

            String COMMIT_DATE = "//div[@id='information']//td[text()='Rev.A:']/../../tr[2]//input";

            String REVISION_DIFF = "//div[@id='information']//iframe";

            String INNER_DIFF_IFRAME = "iframe";
        }

        interface RemoteRepositories {
            String REMOTE_REPOSITORIES_FORM = "//div[@view-id='ideRemoteView']";

            String REMOTE_GRID = "ideRemoteGrid";

            String REMOTE_REPOSITORIES_ADD_BUTTON_ID = "ideRemoteViewAddButton";

            String REMOTE_REPOSITORIES_REMOVE_BUTTON_ID = "ideRemoteViewDeleteButton";

            String REMOTE_REPOSITORIES_CLOSE_FORM_BUTTON_ID = "ideRemoteViewCloseButton";

            String ADD_REMOTE_REPOSITORY_FORM = "//div[@view-id='ideAddRemoteRepositoryView']";

            String ADD_REMOTE_REPOSITORY_NAME_FIELD = "ideAddRemoteRepositoryViewNameField";

            String ADD_REMOTE_REPOSITORY_LOCATION_FIELD = "ideAddRemoteRepositoryViewUrlField";

            String ADD_REMOTE_REPOSITIRY_OK_BUTTON = "ideAddRemoteRepositoryViewOkButton";

            String SELECTOR_REPOSITORY_BY_NAME = "//table[@id='ideRemoteGrid']//div[text()='%s']";
        }


        interface Merge {
            String MERGE_VIEW_ID = "MergeView-window";

            String NODES_VIEW = "MergeViewRefTree";

            String ITEM_IN_TREE = "//div[@id='" + NODES_VIEW + "']" + "//div[text()='%s']";

            String EXPAND_LOCAL_BRANCHES =
                    "//div[@id='MergeViewRefTree']//div[text()='Local " +
                    "Branches']/parent::td/ancestor-or-self::td[2]/preceding-sibling::td/img";

            String EXPAND_REMOTE_BRANCHES =
                    "//div[@id='MergeViewRefTree']//div[text()='Remote " +
                    "Branches']/parent::td/ancestor-or-self::td[2]/preceding-sibling::td/img";


            String EXPANDITEM_ICON_BASE_64 =
                    "data:image/gif;base64,"
                    +
                    "R0lGODlhEAAQAIQaAFhorldnrquz1mFxsvz9/vr6/M3Q2ZGbw5mixvb3+Gp5t2Nys77F4GRzs9ze4mt6uGV1s8/R2VZnrl5usFdortPV2/P09"
                    +
                    "+3u8eXm6lZnrf///wAAzP///////////////yH5BAEAAB8ALAAAAAAQABAAAAVE4CeOZGmeaKquo5K974MuTKHdhDCcgOVvvoTkRLkYN8bL0E"
                    +
                    "TBbJ5PTIaIqW6q0lPAYcVOTRNEpEI2HCYoCOzVYLnf7hAAOw==";

            String MERGE_BUTTON_ID = "MergeViewMergeButton";

            String CANCEL_BUTTON_ID = "MergeViewCancelButton";
        }


        interface AddToIndex {
            String ADD_TO_INDEX_FORM = "//div[@view-id='ideAddToIndexView']";

            String ADD_TO_INDEX_FORM_UPDATE_CHECKBOX_NAME = "ideAddToIndexViewUpdaterField";

            String ADD_TO_INDEX_FORM_ADD_BUTTON_ID = "ideAddToIndexViewAddButton";
        }

        interface RemoveFromIndex {
            String REMOVE_FROM_INDEX_VIEW = "ideRemoveFromIndexView-window";

            String REMOVE_INDEX_BTN = "ideRemoveFromIndexViewRemoveButton";

            String REMOVE_INDEX_CANCEL_BTN = "ideRemoveFromIndexViewCancelButton";

            String REMOVE_FROM_INDEX_ONLY_CHECKBOX = "//span[@id='ideRemoveFromIndexOnlyBox']/input";
        }

        interface Commit {
            String COMMIT_FROM = "//div[@view-id='ideCommitView']";

            String COMMIT_FORM_ADD_ALL_CHANGES_CHECKBOX_NAME = "ideCommitViewAllField";

            String COMMIT_MESSAGE_TEXTAREA_NAME = "ideCommitViewMessageField";

            String COMMIT_FORM_COMMIT_BUTTON_ID = "ideCommitViewCommitButton";

            String COMMIT_FORM_CANCEL_BUTTON_ID = "ideCommitViewCancelButton";
        }

        interface ResetIndex {
            String RESET_INDEX_FORM = "//div[@view-id='ideResetFilesView']";

            String SELECT_FILE_TO_RESET_BY_NAME =
                    "//table[@id='ideIndexFilesGrid']//div[text()='%s']/../..//input[@type='checkbox']";

            String RESET_BUTTON_IN_RESET_INDEX_FORM_ID = "ideResetFilesViewResetButton";
        }

        interface Reset {
            String RESET_FROM = "//div[@view-id='ideResetToCommitView']";

            String SELECT_COMMIT_IN_RESET_FORM = "//table[@id='ideRevisionGrid']/tbody//div[contains(.,'%s')]";

            String RESET_GRID_ID = "ideRevisionGrid";

            String RESET_FORM_SOFT_RESET_CHECKBOX = "//div[@view-id='ideResetToCommitView']//label[text()='soft']/../input";

            String RESET_FORM_MIXED_RESET_CHECKBOX = "//div[@view-id='ideResetToCommitView']//label[text()='mixed']/../input";

            String RESET_FORM_HARD_RESET_CHECKBOX = "//div[@view-id='ideResetToCommitView']//label[text()='hard']/../input";

            String RESET_FORM_RESET_BUTTON_ID = "ideRevertToCommitViewRevertButton";
        }

        interface Pull {
            String PULL_FORM = "//div[@view-id='idePullView']";

            String PULL_BUTTON_ID = "idePullViewPullButton";

            String CANCEL_PULL_FORM_BUTTON_ID = "idePullViewCancelButton";

            String REMOTES_SELECTOR = "//select[@name='idePullViewRemoteField']/option[text()='%s']";

            String PULL_FROM_REMOTE_BRANCH_FIELD = "idePullViewRemoteBranchesField";

            String PULL_LOCAL_BRANCH_FIELD = "idePullViewLocalBranchesField";


        }

        interface Push {
            String PUSH_FORM = "idePushToRemoteView-window";

            String CHOOSE_REOTE_REPO_FIELDS_NAME = "idePushToRemoteViewRemoteField";

            String SELECT_IN_REMOTE_REPO_DROPDOWN = "//select[@name='idePushToRemoteViewRemoteField']/option[text()='%s']";

            String PUSH_TO_BRANCH_FIELD = "idePushToRemoteViewRemoteBranchesField";

            String ENABLED_PUSH_BTN = "div#idePushToRemoteViewPushButton[button-enabled=true]";

            String PUSH_FROM_BRANCH_FIELD = "idePushToRemoteViewLocalBranchesField";

            String PUSH_BTN_ID = "idePushToRemoteViewPushButton";

            String PUSH_CANCEL_BTN_ID = "idePushToRemoteViewCancelButton";
        }

        interface Fetch {
            String FETCH_FORM = "ideFetchView-window";

            String FETCH_BUTTON_ID = "ideFetchViewFetchButton";

            String FETCH_ENABLED_STATE_CSS = "div#ideFetchViewFetchButton[button-enabled='true']";
        }

        interface SelectProjectType {
            String SELECT_PROJECT_TYPE_FORM = "//div[@view-id='ideProjectPrepareView']";

            String SELECT_PROJECT_TYPE = "//div[@view-id='ideProjectPrepareView']//select/option[text()='%s']";

            String SELECT_RPOJECT_TYPE_OK_BUTTON = "//table//td[text()='Ok']";

            String SELECT_RPOJECT_TYPE_CANCEL_BUTTON = "//table//td[text()='Cancel']";
        }

    }

    public interface GitIcons {

        String ROOT =
                "data:image/png;base64," +
                "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAJCAYAAAD+WDajAAAAvUlEQVR42qXOzQoBUQAF4PtKo7mNodnYiRcYGy" +
                "+hEAsrW4lIFpSaKBtKYzGL8R8lxITYSH4mxdwnOMzMI1h8q3PqHMJOcRyaQSxzvKsgYFeVYA1lEDu4TCt4ztIOs8vj1vBhkQ" +
                "+A2O27oYNday6dw1sR0E9RkFVJxG2jgp1LLs2Dl+KFlhH/CZd5CnM" +
                "/AnvUXWMOVkuAmvxtXjthGO0YVo2oY12m2Bb9OCohEDaJYP673UtQh5oSoGclfAYyvi2/tp5q6QDwAAAAAElFTkSuQmCC";

        String IN_REPOSITORY =
                "data:image/png;base64,"
                +
                "iVBORw0KGgoAAAANSUhEUgAAAAYAAAAJCAYAAAARml2dAAAAv0lEQVR42p2OvWrCUABG7ytFEq5aXLqV"
                +
                "+gJx6UsIVXTo1LWEiiIOCkJQcGmhxMEh"
                +
                "/lUUgkorKnUR8ScUNPcJjvoKrt85cD6h/p5Z1B7w3nS8d8lvKUbQMRHXcf1d5DDI4n/qbKtRRtY94mruZi5qU0a5Gkdb0swYiHE"
                +
                "+wnbqoFZ5VCvEvx2m9RK5BXiWgT/vovYVVE8jqEuc9KWx+YgzayQZV5+YFAx+cncs7UeE6icYXu59pQycjMR9jXFqm5wBsDeapY2ZTToAAAAASUVORK5CYII=";

        String UNTRACKED =
                "data:image/png;base64," +
                "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAICAYAAAA1BOUGAAAAMUlEQVR42mNgIASEm6/+Z2DsBWMwGwUgC4IUYQUis//z5J" +
                "/DIonVSKIAyC7xhcTrBAB6oxikOBH9uwAAAABJRU5ErkJggg==";

        String MODIFIED =
                "data:image/png;base64,"
                +
                "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAYAAADED76LAAAAWUlEQVR42mNgIAZ0txj/x4XBClobDP83l/mC8c/f1"
                +
                "+Hs1iZDiIK6Sj24BDJdU64HUVBSoAUXhGEQv6xAA6IgL1sFqwkFWSoQBZlpiljdkJWiAFWQIv0fFwYAqlR7LKscR70AAAAASUVORK5CYII=";

        String CHANGED =
                "data:image/png;base64,"
                +
                "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAYAAADED76LAAAAWUlEQVR42mNgIAYEWsr+x4XBCjyNpf6HWBiA8a9f5"
                +
                "+BsTxMpiAJ7HTG4BDJtBxQHKzBWEYILwjCIb6IiCFGgKcuL1QQtoDhYgaI4F1Y3KAHFwQpkRdj/48IAcdtjs3Na2CYAAAAASUVORK5CYII=";

        String ADDED =
                "data:image/png;base64,"
                +
                "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAYAAADED76LAAAAk0lEQVR42mNgIAVY9gT9t+gN+G/ZF/wfqwKLiUH/r920+28+IRRVgUGt23+DKiCu9fh"
                +
                "/7Jr1f71qj//61W7/9as8IAo1s+3+d64xAWJTMN2z2vR/11qz/xrZjhAFqsn2/9WSrf+rpNj8T55k+F89yQbItwSLoVglF2j8P3mK3n85fyPsjpTxMfgv46P"
                +
                "/X8ZbH64AAJnVQnFFeKHgAAAAAElFTkSuQmCC";

        String REMOVED =
                "data:image/png;base64,"
                +
                "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAYAAADED76LAAAAeElEQVR42o2PuwpEIQxE7xf6xMJCG60VH7WlfvYsCayX7TbNIYdhSJ7nn+m9I6WEMQa"
                +
                "+ewgBRA7EGHHOwZyTBQX33iB/G7z3HCK51uL9NtA455BzZkm01uLnBmMMSilQSjG11m8DyVrrFUQKSSnfG4QQ94vWGjcRP7YoX3pHgscsAAAAAElFTkSuQmCC";

        String CONFLICT =
                "data:image/png;base64,"
                +
                "iVBORw0KGgoAAAANSUhEUgAAAAsAAAAKCAYAAABi8KSDAAAAm0lEQVR42mNgQAOrjDX+I9M4wUpdtf8vYqL/L9dD0FgVLldT+v/YwwuMn9rYwtkgcRSFyxTk"
                +
                "/9/VMcaJQfJghUulpP5fUlL6f0tS+f9dBW0grQJmg/A9DRMwDZIHqQNrWCwqDhZ4rG0Ixk/UdeFskDhIHsUpiwVEwBJPjczgGMRfBBTH6smFPIIQBVB6IY8Q"
                +
                "/uBbyCnwH5kmGwAAk9N3qYx6DNEAAAAASUVORK5CYII=";


    }

    @FindBy(id = Locators.InitializeRepository.INITIALIZATION_OK_BUTTON_ID)
    private WebElement initializeOkBtn;

    @FindBy(id = Locators.InitializeRepository.INITIALIZATION_CANCEL_BUTTON_ID)
    private WebElement initializeCancelBtn;

    @FindBy(name = Locators.InitializeRepository.INITIALIZATION_REPOSITORY_NAME)
    private WebElement initializeRepoName;

    @FindBy(name = Locators.InitializeRepository.BARE_REPOSITORY)
    private WebElement bareRepo;

    @FindBy(id = Locators.Branches.BRANCHES_VIEW_ID)
    private WebElement branchesControl;

    @FindBy(id = Locators.Branches.CREATE_BRANCH_BTN_ID)
    private WebElement createBranchButton;

    @FindBy(id = Locators.Branches.CREATE_NEW_BRANCH_ID)
    private WebElement createSmallFormWithField;

    @FindBy(id = Locators.Branches.OK_BUTTON_ID)
    private WebElement createConfirmCreateNewBranhBtn;

    @FindBy(css = Locators.Branches.ENABLED_CHECKOUT_BTN_CSS)
    private WebElement checkOutBranchBtn;

    @FindBy(id = Locators.Branches.CLOSE_BRANCH_BTN_ID)
    private WebElement closeBranchButton;

    @FindBy(css = Locators.Branches.DELETE_BRANCH_ENABLED_BTN_CSS)
    private WebElement deleteBranchButton;

    @FindBy(id = Locators.GitUrl.GIT_URL_CLOSE_BUTTON_ID)
    private WebElement closeGitUrlBtn;

    @FindBy(name = Locators.CloneRepository.GIT_CLONE_FORM_REMOTE_REPO_URL)
    private WebElement remoteGitRepoUri;

    @FindBy(name = Locators.CloneRepository.GIT_CLONE_FORM_PROJECT_NAME)
    private WebElement cloneFormProjectName;

    @FindBy(name = Locators.CloneRepository.GIT_CLONE_FORM_REMOTE_NAME)
    private WebElement cloneFormRemoteName;

    @FindBy(id = Locators.CloneRepository.GIT_CLONE_FORM_CLONE_BUTTON)
    private WebElement cloneFormCloneBtn;

    @FindBy(id = Locators.CloneRepository.GIT_CLONE_FORM_CANCEL_BUTTON)
    private WebElement cloneFormCancelBtn;

    @FindBy(id = Locators.ShowHistory.REVISION_GRID_ID)
    private WebElement revisionGrid;

    @FindBy(xpath = Locators.ShowHistory.CLOSE_HISTORY_VIEW)
    private WebElement closeHistoryViewBtn;

    @FindBy(id = Locators.RemoteRepositories.REMOTE_GRID)
    private WebElement remoteRepositoriesGrid;

    @FindBy(id = Locators.RemoteRepositories.REMOTE_REPOSITORIES_ADD_BUTTON_ID)
    private WebElement addRemoteRepositoryBtn;

    @FindBy(id = Locators.RemoteRepositories.REMOTE_REPOSITORIES_REMOVE_BUTTON_ID)
    private WebElement removeRemoteRepositoryBtn;

    @FindBy(id = Locators.RemoteRepositories.REMOTE_REPOSITORIES_CLOSE_FORM_BUTTON_ID)
    private WebElement closeRemoteRepositoryFormBtn;

    @FindBy(name = Locators.AddToIndex.ADD_TO_INDEX_FORM_UPDATE_CHECKBOX_NAME)
    private WebElement addToIndexUpdateCheckBox;

    @FindBy(id = Locators.AddToIndex.ADD_TO_INDEX_FORM_ADD_BUTTON_ID)
    private WebElement addToIndexAddButton;

    @FindBy(id = Locators.RemoveFromIndex.REMOVE_FROM_INDEX_VIEW)
    private WebElement removeFromIndexView;

    @FindBy(id = Locators.RemoveFromIndex.REMOVE_INDEX_BTN)
    private WebElement removeFromIndexBtn;

    @FindBy(id = Locators.RemoveFromIndex.REMOVE_INDEX_CANCEL_BTN)
    private WebElement removeFromIndexCancelBtn;

    @FindBy(name = Locators.Commit.COMMIT_FORM_ADD_ALL_CHANGES_CHECKBOX_NAME)
    private WebElement commitAddAllChangesCheckbox;

    @FindBy(name = Locators.Commit.COMMIT_MESSAGE_TEXTAREA_NAME)
    private WebElement commitMessageTextarea;

    @FindBy(id = Locators.Commit.COMMIT_FORM_COMMIT_BUTTON_ID)
    private WebElement commitButton;

    @FindBy(name = Locators.Commit.COMMIT_FORM_CANCEL_BUTTON_ID)
    private WebElement commitCancelButton;

    @FindBy(tagName = Locators.ShowHistory.INNER_DIFF_IFRAME)
    private WebElement innerDiffIframe;

    @FindBy(xpath = Locators.RemoveFromIndex.REMOVE_FROM_INDEX_ONLY_CHECKBOX)
    private WebElement removeFromIndexOnlyCheckbox;

    @FindBy(id = Locators.ResetIndex.RESET_BUTTON_IN_RESET_INDEX_FORM_ID)
    private WebElement resetIndexButton;

    @FindBy(id = Locators.Merge.MERGE_VIEW_ID)
    private WebElement mergeForm;

    @FindBy(id = Locators.Merge.MERGE_BUTTON_ID)
    private WebElement mergeBtn;

    @FindBy(id = Locators.Merge.CANCEL_BUTTON_ID)
    private WebElement mergeCancelBtn;

    @FindBy(xpath = Locators.Merge.EXPAND_LOCAL_BRANCHES)
    private WebElement expandLocalBranchesIcon;

    @FindBy(xpath = Locators.Merge.EXPAND_REMOTE_BRANCHES)
    private WebElement expandRemoteBranchesIcon;

    @FindBy(xpath = Locators.Reset.RESET_FORM_SOFT_RESET_CHECKBOX)
    private WebElement resetSoftCheckbox;

    @FindBy(xpath = Locators.Reset.RESET_FORM_MIXED_RESET_CHECKBOX)
    private WebElement resetMixedCheckbox;

    @FindBy(xpath = Locators.Reset.RESET_FORM_HARD_RESET_CHECKBOX)
    private WebElement resetHardCheckbox;

    @FindBy(id = Locators.Reset.RESET_FORM_RESET_BUTTON_ID)
    private WebElement reseButtonInResetToCommitForm;

    @FindBy(id = Locators.Reset.RESET_GRID_ID)
    private WebElement resetToCommitGrid;

    @FindBy(xpath = Locators.RemoteRepositories.ADD_REMOTE_REPOSITORY_FORM)
    private WebElement addRemoteRepoForm;

    @FindBy(name = Locators.RemoteRepositories.ADD_REMOTE_REPOSITORY_NAME_FIELD)
    private WebElement addRemoteRepoNameField;

    @FindBy(name = Locators.RemoteRepositories.ADD_REMOTE_REPOSITORY_LOCATION_FIELD)
    private WebElement addRemoteRepoLocationField;

    @FindBy(id = Locators.RemoteRepositories.ADD_REMOTE_REPOSITIRY_OK_BUTTON)
    private WebElement addRemoteRepoOkButton;

    @FindBy(id = Locators.Pull.PULL_BUTTON_ID)
    private WebElement pullButtonId;

    @FindBy(name = Locators.Pull.PULL_FROM_REMOTE_BRANCH_FIELD)
    private WebElement pullFromeRemoteBranchField;

    @FindBy(name = Locators.Pull.PULL_LOCAL_BRANCH_FIELD)
    private WebElement pullFromeLocalBranchField;

    @FindBy(id = Locators.Push.PUSH_FORM)
    private WebElement pushView;

    @FindBy(name = Locators.Push.PUSH_FROM_BRANCH_FIELD)
    private WebElement pushFromBranchOption;

    @FindBy(name = Locators.Push.PUSH_TO_BRANCH_FIELD)
    private WebElement pushToBranchOption;

    @FindBy(name = Locators.Push.CHOOSE_REOTE_REPO_FIELDS_NAME)
    private WebElement pushChooseRemoteOption;

    @FindBy(id = Locators.Push.PUSH_BTN_ID)
    private WebElement pushBtn;

    @FindBy(id = Locators.Push.PUSH_CANCEL_BTN_ID)
    private WebElement pushCancelBtn;

    @FindBy(id = Locators.Pull.CANCEL_PULL_FORM_BUTTON_ID)
    private WebElement cancelPullFormButtonId;

    @FindBy(id = Locators.Fetch.FETCH_BUTTON_ID)
    private WebElement fetchButton;

    @FindBy(xpath = Locators.SelectProjectType.SELECT_RPOJECT_TYPE_OK_BUTTON)
    private WebElement selectProjectTypeOkButton;

    @FindBy(xpath = Locators.SelectProjectType.SELECT_RPOJECT_TYPE_CANCEL_BUTTON)
    private WebElement selectProjectTypeCancelButton;

    @FindBy(id = Locators.Branches.RENAME_BRANCH_BUTTON_ID)
    private WebElement renameBranchBtnId;

    @FindBy(xpath = Locators.Branches.RENAME_BRANCH_INPUT_NAME)
    private WebElement renameBranchInput;

    /** wait for initialize local repository form appear */
    public void waitInitializeLocalRepositoryForm() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(Locators.InitializeRepository.INITIALIZATION_FORM)));
    }

    /** wait for initialize local repository form disappear */
    public void waitInitializeLocalRepositoryFormDisappear() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath(Locators.InitializeRepository.INITIALIZATION_FORM)));
    }

    /** click ok on initialize local repository form */
    public void clickOkInitializeLocalRepository() {
        initializeOkBtn.click();
    }

    /** click cancel on initialize local repository form */
    public void clickCancelInitializeLocalRepository() {
        initializeCancelBtn.click();
    }

    /** type initialization repository name */
    public void typeInitializationRepositoryName(String repoName) {
        initializeRepoName.clear();
        initializeRepoName.sendKeys("/" + repoName);
    }

    /** select bare repository option on initialization local repository form */
    public void selectBareRepositoryOption() {
        bareRepo.click();
    }

    /** wait appearance of the branches control */
    public void waitBranhesOpened() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(branchesControl));
    }

    /** wait appearance of the create btn */
    public void waitBranhesCreateBtn() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(createBranchButton));
    }

    /** wait appearance of the create btn */
    public void clickBranhesCreateBtn() {
        createBranchButton.click();
    }

    /** wait form for input name of the branch */
    public void waitCreateNewBranchFieldIsOpen() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(createSmallFormWithField));
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(createSmallFormWithField.findElement(By.name("valueField"))));
    }

    /**
     * Wait Evaluate expression view opened.
     *
     * @throws Exception
     */
    public void waitFieldWithNewNameBranchForm(final String val) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(By.xpath(String.format(Locators.Branches.NAME_BRANCH, val))).isDisplayed();
            }
        });
    }

    /**
     * wait while specified name disappear from branch form
     *
     * @param val
     * @throws Exception
     */
    public void waitDisappearNameBranchInBranchForm(final String val) throws Exception {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(Locators.Branches.NAME_BRANCH,
                                                                                              val))));
    }


    /** wait form for input name of the branch */
    public void waitCreateNewBranchFieldIsClose() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.Branches.INPUT_NEW_BRANCH)));
    }

    /** wait delete branch btn is active */
    public void waitDeleteBranchBtnIsActive() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOf(deleteBranchButton));
    }

    /** wait form for input name of the branch */
    public void waitCloseBranchBtnIsActive() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOf(closeBranchButton));
    }


    /** wait form for input name of the branch */
    public void waitPushView() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOf(pushView));
    }


    public void typeInFieldNewBranchName(String name) {
        createSmallFormWithField.findElement(By.name("valueField")).sendKeys(name);
    }

    /** wait appearance of the create btn */
    public void clickCreateNewBranchOkBtn() {
        createConfirmCreateNewBranhBtn.click();
    }

    /** wait click on active delete branch btn */
    public void clickDeleteBranchBtn() {
        waitDeleteBranchBtnIsActive();
        deleteBranchButton.click();
    }


    /** wait for git url form */
    public void waitGitUrlForm() throws InterruptedException {
        Thread.sleep(1000);// need for appearing url in form
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.GitUrl.GIT_URL_FORM)));
    }

    /** wait for git url form disappear */
    public void waitGitUrlFormDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.GitUrl.GIT_URL_FORM)));
    }

    /** wait for choose remote repo dropdawn */
    public void waitPushChooseRemoteRepo() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(pushChooseRemoteOption));
    }


    /** wait while push form is closed */
    public void waitPushFormIsClosed() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.Push.PUSH_FORM)));
        //need for complete pushing changes on stg and pro
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    // *****************************************************************************************

    /**
     * get current selected value from field 'Push from branch'
     *
     * @return
     */
    public String getPushFromBranchValue() {
        return pushFromBranchOption.getAttribute("value");
    }


    /**
     * get current selected value from field 'Push to branch'
     *
     * @return
     */
    public String getPushPushToBranchValue() {
        return pushToBranchOption.getAttribute("value");
    }

    /** waits while push button on git form will become active */
    public void waitPushBtnIsEnabled() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(Locators.Push.ENABLED_PUSH_BTN)));
    }

    /**
     * wait specified value into push from branch field
     *
     * @param val
     * @throws Exception
     */
    public void waitPushFromBranchFieldIsSetVal(final String val) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getPushFromBranchValue().equals(val);
            }
        });
    }


    /**
     * wait specified value into push to branch field
     *
     * @param val
     * @throws Exception
     */
    public void waitPushToBranchFieldIsSetVal(final String val) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getPushPushToBranchValue().equals(val);
            }
        });
    }


    /** wait for choose remote repo dropdawn */
    public void waitPushChooseRemoteRepoIspresentInDropDawn(String item) {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.Push.SELECT_IN_REMOTE_REPO_DROPDOWN,
                                                                                            item))));
    }


    /**
     * !!!Warning this method unchecked choose remote repo from field
     *
     * @param value
     */
    public void choosePushRemoteRepo(String value) {
        pushChooseRemoteOption.click();
        waitPushChooseRemoteRepoIspresentInDropDawn(value);
        driver().findElement(By.xpath(String.format(Locators.Push.SELECT_IN_REMOTE_REPO_DROPDOWN, value))).click();
    }

    /** click on push to remote repo btn */
    public void clickPushBtn() throws InterruptedException {
        waitPushBtnIsEnabled();
        Thread.sleep(500);
        pushBtn.click();
        waitPushFormIsClosed();
    }

    /** click on cancel btn on push form */
    public void clickPushCancelBtn() {
        pushCancelBtn.click();
    }


    /**
     * get git repository url
     *
     * @return link to git repository
     */
    public String getGitUrl() {
        WebElement element = driver().findElement(By.xpath(Locators.GitUrl.GIT_URL));
        return element.getAttribute("value");
    }

    /** close git url form */
    public void clickCloseGitUrlForm() {
        closeGitUrlBtn.click();
        waitGitUrlFormDisappear();
    }

    /** wait for git clone form */
    public void waitGitCloneForm() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .visibilityOfElementLocated(By.xpath(Locators.CloneRepository.GIT_CLONE_FORM)));
    }

    /** wait for git clone form disappear */
    public void waitGitCloneFormDisappear() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .invisibilityOfElementLocated(By.xpath(Locators.CloneRepository.GIT_CLONE_FORM)));
    }

    /**
     * type Remote repository URI:
     *
     * @param remoteGitRepoUri
     *         remote git repository URI
     */
    public void typeRemoteGitRepositoryURI(String remoteGitRepoUri) {
        this.remoteGitRepoUri.clear();
        this.remoteGitRepoUri.sendKeys(remoteGitRepoUri);
    }

    /**
     * clone repository form - type project name.
     *
     * @param projectName
     *         project name.
     */
    public void typeProjectNameInCloneRepositoryForm(String projectName) {
        cloneFormProjectName.clear();
        cloneFormProjectName.sendKeys(projectName);
    }

    /**
     * clone repository form - type project name.
     *
     * @param remoteName
     *         remote name.
     */
    public void typeRemoteNameInCloneRepositoryForm(String remoteName) {
        cloneFormRemoteName.clear();
        cloneFormRemoteName.sendKeys(remoteName);
    }

    /** click clone button on clone remote git repository form. */
    public void clickCloneButtonOnCloneRemoteRepositoryForm() {
        cloneFormCloneBtn.click();
        waitGitCloneFormDisappear();
    }

    /** click on clone button in git clone form. Need for test with checking valid names */
    public void justClickCloneButtonOnCloneRemoteRepositoryForm() {
        cloneFormCloneBtn.click();
    }

    /** click cancel button on clone remote git repository form. */
    public void clickCancelButtonOnCloneRemoteRepositoryForm() {
        cloneFormCancelBtn.click();
        waitGitCloneFormDisappear();
    }

    /**
     * wait for git show history form
     *
     * @throws InterruptedException
     */
    public void waitGitShowHistoryForm() throws InterruptedException {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .visibilityOfElementLocated(By.xpath(Locators.ShowHistory.GIT_SHOW_HISTORY_FORM)));
        IDE().LOADER.waitClosed();
    }

    /** wait for git show history form disappear */
    public void waitGitShowHistoryFormDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath(Locators.ShowHistory.GIT_SHOW_HISTORY_FORM)));
    }

    /**
     * wait for commit in revision grid of show git history view
     * <p/>
     * - committer name
     *
     * @param comment
     *         - comment for commit
     */
    public void waitCommitInHistoryView(final String comment) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return revisionGrid.getText().contains(comment);
            }
        });
    }


    /** wait appearance merge form */
    public void waitMergeView() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(mergeForm));
    }

    /**
     * wait disappear merge form
     *
     * @throws Exception
     */
    public void waitMergeViewIsClosed() throws Exception {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.Merge.MERGE_VIEW_ID)));
        IDE().LOADER.waitClosed();
    }

    /** wait appearance icon for expand of the list with local branches */
    public void waitMergeExpandLocBranchIcon() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(expandLocalBranchesIcon));
    }

    /** wait appearance icon for expand of the list with remote branches */
    public void waitMergeExpandRemoteBranchIcon() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(expandRemoteBranchesIcon));
    }

    /** wait appearance icon for expand of the list with remote branches not present */
    public void waitMergeExpandRemoteBranchIconNotPresent() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath(Locators.Merge.EXPAND_REMOTE_BRANCHES)));
    }

    /** wait specified item in merge list */
    public void waitItemInMergeList(final String item) {
        new WebDriverWait(driver(), 20)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.Merge.ITEM_IN_TREE,
                                                                                            item))));
    }

    /** wait specified item in merge list is not present */
    public void waitItemInMergeListIsNotPresent(final String item) {
        new WebDriverWait(driver(), 20)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(Locators.Merge.ITEM_IN_TREE,
                                                                                              item))));
    }

    /**
     * select item in merge list
     *
     * @param item
     */
    public void selectItemInMegeForm(final String item) {
        driver().findElement(By.xpath(String.format(Locators.Merge.ITEM_IN_TREE, item))).click();
    }

    /** click on merge btn */
    public void clickMergeBtn() {
        mergeBtn.click();
    }

    /**
     * click on cancel merge btn
     *
     * @throws Exception
     */
    public void clickCancelMergeBtn() throws Exception {
        mergeCancelBtn.click();
        waitMergeViewIsClosed();
    }

    /** click on expand icon "+" for local branches */
    public void clickOnExpandLocalBranchIcon() {
        expandLocalBranchesIcon.click();
    }

    /** click on expand icon "+" for remote branches */
    public void clickOnExpandRemotelBranchIcon() {
        expandRemoteBranchesIcon.click();
    }


    /**
     * wait for commit in revision grid of show git history view is not present
     *
     * @param committer
     *         - committer name
     * @param comment
     *         - comment for commit
     */
    public void waitCommitInHistoryViewIsNotPresent(final String committer, final String comment) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !(revisionGrid.getText().contains(committer) && revisionGrid.getText().contains(comment));
            }
        });
    }


    /** close git show history form */
    public void clickCloseShowHistoryForm() {
        closeHistoryViewBtn.click();
        waitGitShowHistoryFormDisappear();
    }

    /**
     * select commit in show history panel by comment message
     *
     * @param commitMessage
     *         commit comment message
     * @throws InterruptedException
     */
    public void selectCommitInHistory(String commitMessage) throws InterruptedException {
        WebElement element = driver().findElement(By.xpath(String.format(Locators.ShowHistory.SELECT_COMMIT, commitMessage)));
        element.click();
        IDE().LOADER.waitClosed();
    }

    /**
     * get current commit revision
     *
     * @return commit revision
     */
    public String getCommitRevisionFromHistoryPanel() {
        WebElement element = driver().findElement(By.xpath(Locators.ShowHistory.REV_A));
        return element.getAttribute("value");
    }

    /**
     * get commit date
     *
     * @return commit date
     */
    public String getCommitDateFromHistoryPanel() {
        WebElement element = driver().findElement(By.xpath(Locators.ShowHistory.COMMIT_DATE));
        return element.getAttribute("value");
    }

    /** wait disappear of the branches control */
    public void waitBranhesIsClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.Branches.BRANCHES_VIEW_ID)));
    }

    public void clickCloseBranchBtn() throws InterruptedException {
        closeBranchButton.click();
        waitCreateNewBranchFieldIsClose();
        IDE().LOADER.waitClosed();
    }

    /** wait while checkout button is enabled */
    public void waitCheckOutBranchBtnIsActive() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(checkOutBranchBtn));

    }

    /** click on active checkout btn */
    public void clickCheckOutBtn() {
        waitCheckOutBranchBtnIsActive();
        checkOutBranchBtn.click();
    }

    /**
     * click in branches form on the branch with specified name
     *
     * @param name
     * @throws InterruptedException
     */
    public void selectBranchInList(final String name) throws InterruptedException {
        driver().findElement(By.xpath(String.format(Locators.Branches.NAME_BRANCH, name))).click();
        IDE().LOADER.waitClosed();
    }

    /**
     * click in branches form on the branch with specified name
     *
     * @param name
     */
    public void waitBranchIsCheckout(final String name) {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.Branches
                                                                                                    .ACTIVE_BRANCH,
                                                                                            name))));
    }

    /** wait remote repositories form */
    public void waitRemoteRepositoriesForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.RemoteRepositories.REMOTE_REPOSITORIES_FORM)));
    }

    /** wait remote repositories form disappear */
    public void waitRemoteRepositoriesFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.RemoteRepositories.REMOTE_REPOSITORIES_FORM)));
    }

    /**
     * wait for remote repository in remote repositories grid
     *
     * @param name
     *         repository name
     * @param location
     *         repository location
     */
    public void waitRemoteRepository(final String name, final String location) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return remoteRepositoriesGrid.getText().contains(name) && remoteRepositoriesGrid.getText().contains(location);
            }
        });
    }

    /**
     * wait for remote repository in remote repositories grid is not present
     *
     * @param name
     *         repository name
     * @param location
     *         repository location
     */
    public void waitRemoteRepositoryNotPresent(final String name, final String location) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !(remoteRepositoriesGrid.getText().contains(name) && remoteRepositoriesGrid.getText().contains(location));
            }
        });
    }

    /** wait appearance remove index form */
    public void waitRemoveFromIndexForm() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(removeFromIndexView));
    }

    /** click on remove from index form */
    public void clickRemoveButtonInRemoveFromIndexForm() {
        removeFromIndexBtn.click();
        waitRemoveIndexFormIsClosed();
    }


    /** click on remove from index form */
    public void clickCancelBtnRemoveIndxForm() {
        removeFromIndexCancelBtn.click();
    }

    /** select remove from index only checkbox on remove from index form */
    public void selectRemoveFromIndexOnlyCheckbox() {
        removeFromIndexOnlyCheckbox.click();
    }

    public void selectRepoIntoRemoteRepositoriesList() {

    }

    /** wait disappear remove index form */
    public void waitRemoveIndexFormIsClosed() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.RemoveFromIndex.REMOVE_FROM_INDEX_VIEW)));
    }

    /** click on add remote repository button */
    public void clickAddRemoteRepository() {
        addRemoteRepositoryBtn.click();
    }

    /** click on remove remote repository button */
    public void clickRemoveRemoteRepository() {
        removeRemoteRepositoryBtn.click();
    }

    /** click on remove remote repository button */
    public void clickCloseRemoteRepositoryForm() {
        closeRemoteRepositoryFormBtn.click();
        waitRemoteRepositoriesFormDisappear();
    }

    /** wait add to index form */
    public void waitAddToIndexForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.AddToIndex.ADD_TO_INDEX_FORM)));
    }

    /** wait add to index form disappear */
    public void waitAddToIndexFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.AddToIndex.ADD_TO_INDEX_FORM)));
    }

    /** click on update checkbox in add to index form */
    public void clickOnUpdateCheckBoxInAddToIndexForm() {
        addToIndexUpdateCheckBox.click();
    }

    /** click add button in add to index form */
    public void clickAddButtonInAddToIndexForm() {
        addToIndexAddButton.click();
        waitAddToIndexFormDisappear();
    }

    /** wait commit form */
    public void waitCommitForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.Commit.COMMIT_FROM)));
    }

    /** wait add to index form disappear */
    public void waitCommitFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.Commit.COMMIT_FROM)));
    }

    /** select add all changes except of new files checkbox on commit form */
    public void clickAddAllChangesCheckboxInCommitForm() {
        commitAddAllChangesCheckbox.click();
    }

    /** click commit button on commit form */
    public void clickCommitButton() {
        commitButton.click();
        waitCommitFormDisappear();
    }

    /**
     * type commit message
     *
     * @param commitMessage
     *         message for commit
     */
    public void typeCommitMessage(String commitMessage) {
        commitMessageTextarea.sendKeys(commitMessage);
    }

    /** wait inner iframe with diff */
    public void waitInnerDiffIframe() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return innerDiffIframe != null && innerDiffIframe.isDisplayed();
            }
        });
    }

    /** wait outer iframe with diff */
    public void waitOuterDiffIframe() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ShowHistory.REVISION_DIFF)));
    }

    /** wait in diff container appear a some text */
    public void waitEditboxDiffInnerIframe() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body.editbox")));
    }


    /** wait while diff editbox container contains any text */
    public void waitDiffContainerNotEmpty() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return getCommitDiffFromHistoryPanel().length() > 0;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** wait while diff editbox container contains specified text fragment */
    public void waitDiffContainerContainText(final String txt) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return getCommitDiffFromHistoryPanel().contains(txt);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }


    /** wait while text into diff editbox container equals specified text */
    public void waitDiffContainerEqualsText(final String txt) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return getCommitDiffFromHistoryPanel().equals(txt);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }


    /** Select iframe, which contains diff on show history panel */
    public void selectIFrameWithEditor() throws Exception {
        waitOuterDiffIframe();
        WebElement editorFrame = driver().findElement(By.xpath(Locators.ShowHistory.REVISION_DIFF));
        driver().switchTo().frame(editorFrame);
        waitInnerDiffIframe();
        driver().switchTo().frame(innerDiffIframe);
        waitEditboxDiffInnerIframe();
    }

    /** Get text from commit diff */
    public String getCommitDiffFromHistoryPanel() throws Exception {
        selectIFrameWithEditor();
        String text = driver().switchTo().activeElement().getText();
        IDE().selectMainFrame();
        return text;
    }

    /** wait reset index form */
    public void waitResetIndexForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ResetIndex.RESET_INDEX_FORM)));
    }

    /** wait reset index form disappear */
    public void waitResetIndexFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.ResetIndex.RESET_INDEX_FORM)));
    }

    /**
     * select file to reset in reset index form
     *
     * @param filePath
     *         file path
     */
    public void waitAndSelectFileToResetInResetIndexForm(String filePath) {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(String.format(Locators.ResetIndex.SELECT_FILE_TO_RESET_BY_NAME,
                                               filePath))));
        WebElement element = driver().findElement(By.xpath(String.format(Locators.ResetIndex.SELECT_FILE_TO_RESET_BY_NAME, filePath)));
        element.click();
    }

    /** click on reset button in reset index form */
    public void clickOnResetButtonInResetIndexForm() {
        resetIndexButton.click();
        waitResetIndexFormDisappear();
    }


    /** wait reset to commit form */
    public void waitResetToCommitForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.Reset.RESET_FROM)));
    }

    /** wait reset to commit form disappear */
    public void waitResetToCommitFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.Reset.RESET_FROM)));
    }

    /** select soft reset check box */
    public void selectSoftResetCheckbox() {
        resetSoftCheckbox.click();
    }

    /** select mixed reset check box */
    public void selectMixedResetCheckbox() {
        resetMixedCheckbox.click();
    }

    /** select hard reset check box */
    public void selectHardResetCheckbox() {
        resetHardCheckbox.click();
    }

    /**
     * select remote repository (if there several) with specified name
     *
     * @param name
     */
    public void selectRemoteRepoIntoRemoteRepositoriesList(String name) {
        driver().findElement(By.xpath(String.format(Locators.RemoteRepositories.SELECTOR_REPOSITORY_BY_NAME, name))).click();
    }


    /**
     * wait for commit in reset to commit grid
     *
     * @param committer
     *         - committer name
     * @param comment
     *         - comment for commit
     */
    public void waitCommitInResetToCommitGrid(final String committer, final String comment) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return resetToCommitGrid.getText().contains(committer) && resetToCommitGrid.getText().contains(comment);
            }
        });
    }

    /**
     * select commit in reset to commit form by comment message
     *
     * @param commitMessage
     *         commit comment message
     */
    public void selectCommitInResetToCommitForm(String commitMessage) {
        WebElement element = driver().findElement(By.xpath(String.format(Locators.Reset.SELECT_COMMIT_IN_RESET_FORM, commitMessage)));
        element.click();
    }

    /** click reset button in reset to commit form */
    public void clickResetButtonInResetToCommitForm() {
        reseButtonInResetToCommitForm.click();
        waitResetToCommitFormDisappear();
    }

    /** wait add remote repositories form */
    public void waitAddRemoteRepositoriesForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.RemoteRepositories.ADD_REMOTE_REPOSITORY_FORM)));
    }

    /** wait add remote repositories form disappear */
    public void waitAddRemoteRepositoriesFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.RemoteRepositories.ADD_REMOTE_REPOSITORY_FORM)));
    }

    /**
     * type new remote repository name
     *
     * @param remoteRepoName
     *         repository name
     */
    public void typeRemoteRepositoryName(String remoteRepoName) {
        addRemoteRepoNameField.sendKeys(remoteRepoName);
    }

    /**
     * type new remote repository location
     *
     * @param remoteRepoLocation
     *         repository location
     */
    public void typeRemoteRepositoryLocation(String remoteRepoLocation) {
        addRemoteRepoLocationField.sendKeys(remoteRepoLocation);
    }

    /** click ok on add remote reposiotory form */
    public void clickOkOnAddRemoteRepositoryForm() {
        addRemoteRepoOkButton.click();
        waitAddRemoteRepositoriesFormDisappear();
    }

    /** wait git pull form */
    public void waitGitPullForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.Pull.PULL_FORM)));
    }

    /** wait git pull form disappear */
    public void waitGitPullFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.Pull.PULL_FORM)));
    }

    /** click pull button on git pull form */
    public void clickPullButtonOnGitPullForm() {
        pullButtonId.click();
        waitGitPullFormDisappear();
    }

    /** click cancel button on git pull form */
    public void clickCancelButtonOnGitPullForm() {
        cancelPullFormButtonId.click();
        waitGitPullFormDisappear();
    }


    /**
     * select item in pull to remote branches dropdown
     *
     * @param nameBr
     */
    public void selectOnPullFormRemoteBranchInDropDown(String nameBr) {
        WebElement dropDownPull = driver().findElement(By.name("idePullViewRemoteField"));
        Select select = new Select(dropDownPull);
        select.selectByVisibleText(nameBr);
    }


    /**
     * get current selected value from remote branches field
     *
     * @return
     */
    public String getItemFromPullToRempoteBranchField() {
        return pullFromeRemoteBranchField.getAttribute("value");
    }


    /**
     * get current selected value from local branches field
     *
     * @return
     */
    public String getItemFromPullLocalBranchField() {
        return pullFromeLocalBranchField.getAttribute("value");
    }


    /**
     * wait while specified value will be set into remote branches field
     *
     * @param value
     */
    public void waitPullInRmtBranchFieldValueIsSet(final String value) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getItemFromPullToRempoteBranchField().equals(value);
            }
        });
    }


    /**
     * wait while specified value will be set into local branches field
     *
     * @param value
     */
    public void waitPullInLocBranchFieldValueIsSet(final String value) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getItemFromPullLocalBranchField().equals(value);
            }
        });
    }


    /** wait remote repo in pull form */
    public void waitRemoteRepositoryInGitPullForm(String remoteRepositoryName) {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.Pull.REMOTES_SELECTOR,
                                                                                     remoteRepositoryName))));
    }

    /** wait remote repo in pull form disappear */
    public void waitRemoteRepositoryInGitPullFormDisappear(String remoteRepositoryName) {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath(String.format(Locators.Pull.REMOTES_SELECTOR, remoteRepositoryName))));
    }

    /** wait remote > fetch form */
    public void waitFetchForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.id(Locators.Fetch.FETCH_FORM)));
    }

    /** wait remote > fetch form form disappear */
    public void waitFetchFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.Fetch.FETCH_FORM)));
    }

    /** click fetch button on fetch form */
    public void clickFetchButtonOnFetchForm() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(Locators.Fetch.FETCH_ENABLED_STATE_CSS)));
        fetchButton.click();
        waitFetchFormDisappear();
    }

    /** wait select project type form */
    public void waitSelectProjectTypeForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.SelectProjectType.SELECT_PROJECT_TYPE_FORM)));
    }

    /** wait select project type form disappear */
    public void waitSelectProjectTypeFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.SelectProjectType.SELECT_PROJECT_TYPE_FORM)));
    }

    /** click ok on select project type form */
    public void clickOkButtonOnSelectProjectTypeForm() {
        selectProjectTypeOkButton.click();
        waitSelectProjectTypeFormDisappear();
    }

    public void selectProjectType(String projectType) {
        WebElement option = driver().findElement(By.xpath(String.format(Locators.SelectProjectType.SELECT_PROJECT_TYPE, projectType)));
        option.click();
    }

    /** click on rename branch button in branches form */
    public void clickOnRenameBranchButton() {
        renameBranchBtnId.click();
    }

    /** type new name for branch rename */
    public void typeNewBranchNameForRename(String newBrancName) {
        renameBranchInput.clear();
        renameBranchInput.sendKeys(newBrancName);
    }

    /** wait disabled rename branch button */
    public void waitDisabledRenameBranchButton() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.Branches.RENAME_BRANCH_BUTTON_DISABLED)));
    }

    /** wait disabled delete branch button */
    public void waitDisabledDeleteBranchButton() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.Branches.DELETE_BRANCH_BUTTON_DISABLED)));
    }

    /**
     * select item in push to remote branches dropdown
     *
     * @param nameBr
     */
    public void selectOnPushFormRemoteBranchInDropDown(String nameBr) {
        WebElement dropDownPull = driver().findElement(By.name("idePushToRemoteViewRemoteBranchesField"));
        Select select = new Select(dropDownPull);
        select.selectByVisibleText(nameBr);
    }

}
