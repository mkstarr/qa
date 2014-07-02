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
package com.codenvy.ide;

import com.codenvy.ide.core.*;
import com.codenvy.ide.core.appfog.AppFogPaaS;
import com.codenvy.ide.core.aws.AwsPaaS;
import com.codenvy.ide.core.cloudbees.CloudBeesPaaS;
import com.codenvy.ide.core.gae.GaePaaS;
import com.codenvy.ide.core.heroku.HerokuPaaS;
import com.codenvy.ide.core.openshift.OpenShiftPaaS;
import com.codenvy.ide.core.project.CreateProject;
import com.codenvy.ide.core.project.OpenProject;
import com.codenvy.ide.core.project.PackageExplorer;
import com.codenvy.ide.core.project.ProjectExplorer;
import com.codenvy.ide.mail.MailCheck;
import com.codenvy.ide.mail.MailReceiver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 */

public class IDE {
    private WebDriver driver;

    private IDE instance;

    public IDE getInstance() {
        return instance;
    }

    public Menu MENU;

    public Toolbar TOOLBAR;

    public Editor EDITOR;

    public AboutDialog ABOUT;

    public DeployNodeType DEPLOY_NODE_TYPE;

    public AvailableDependencies AVAILABLE_DEPENDENCIES;

    public Loader LOADER;

    public Outline OUTLINE;

    public Perspective PERSPECTIVE;

    public CodeAssistant CODEASSISTANT;

    public Preview PREVIEW;

    public WarningDialog WARNING_DIALOG;

    public AskDialog ASK_DIALOG;

    public AskForValueDialog ASK_FOR_VALUE_DIALOG;

    public InformationDialog INFORMATION_DIALOG;

    public Statusbar STATUSBAR;

    public SelectWorkspace SELECT_WORKSPACE;

    public GoToLine GOTOLINE;

    public GetURL GET_URL;

    public Upload UPLOAD;

    public FindReplace FINDREPLACE;

    public SaveAsTemplate SAVE_AS_TEMPLATE;

    public Search SEARCH;

    public SearchResult SEARCH_RESULT;

    public Output OUTPUT;

    public Properties PROPERTIES;

    public PreviewNodeType PREVIEW_NODE_TYPE;

    public Folder FOLDER;

    public Rename RENAME;

    public ProgressPanelTab PROGRESS_PANEL_TAB;

    public Input INPUT;

    public Button BUTTON;

    public Delete DELETE;

    public Login LOGIN;

    public LogReader LOG_READER;

    public CustomizeToolbar CUSTOMIZE_TOOLBAR;


    public ProgressBar PROGRESS_BAR;

    public CustomizeHotkeys CUSTOMIZE_HOTKEYS;

    public ShowKeyboardShortcuts SHOW_KEYBOARD_SHORTCUTS;

    public OpenFileByURL OPEN_FILE_BY_URL;

    public OpenFileByPath OPEN_FILE_BY_PATH;

    public WelcomePage WELCOME_PAGE;

    public CkEditor CK_EDITOR;

    public Build BUILD;

    public ErrorMarks ERROR_MARKS;

    public CodeAssistantJava CODE_ASSISTANT_JAVA;

    public PopupDialogsBrowser POPUP;

    public ContextMenu CONTEXT_MENU;

    public Debuger DEBUGER;

    public OrginizeImport ORGINIZEIMPORT;

    public ProjectsMenu PROJECTMENU;

    public DeployForm DEPLOY;

    public JavaEditor JAVAEDITOR;

    public Preferences PREFERENCES;

    public Ssh SSH;

    public Formatter FORMATTER;

    public MailCheck MAIL_CHECK;

    public MailReceiver MAIL_RECEIVER;

    public Google GOOGLE;

    public GitHub GITHUB;

    public ImportFromGitHub IMPORT_FROM_GITHUB;

    public InvitefromGoogle INVITE_FORM;

    public CreateANewProjectFromScratch CREATE_PROJECT_FROM_SCRATHC;

    public Refactoring REFACTORING;

    public OpenResource OPEN_RESOURCE;

    public ManageAcces MANAGE_ACCESS;

    public File FILE;

    public Shell SHELL;

    public ImportASampleProject IMPORT_A_SAMPLE_PRJ;

    public ProjectExplorer EXPLORER;

    public CreateProject CREATE;

    public OpenProject OPEN;

    public PackageExplorer PACKAGE_EXPLORER;

    public Navigator NAVIGATOR;

    public CreateNewClassForm CREATE_NEW_CLASS;

    public Theme THEME;

    public Chat CHAT;

    public Notifications NOTIFICATIONS;

    public GetStartedWizard GET_STARTED_WIZARD;

    public Git GIT;

    public JRebel JREBEL;

    public SelectProjectType SELECT_PROJECT_TYPE;

    public ReadOnlyMode READ_ONLY_MODE;

    public ConfigureDataSource DATA_SOURCE_CONFIGURE;

    public GaePaaS GAE;

    public HerokuPaaS HEROKU;

    public AppFogPaaS APP_FOG;

    public AwsPaaS AWS;

    public OpenShiftPaaS OPEN_SHIFT;

    public ErrorTenantNamePage ERROR_TENANT_NAME_PAGE;

    public FactoryURL FACTORY_URL;

    public ProgressorWindow PROGRESSOR_WINDOW;

    public CloudBeesPaaS CLOUD_BEES;

    public LoadingBehaviorPage LOADING_BEHAVIOR;

    public BitBucket BITBUCKET;

    public GerritHub GERRITHUB;

    public IDE(String workspaceURL, WebDriver driver) {
        this.workspaceURL = workspaceURL;
        this.driver = driver;
        instance = this;
        PageFactory.initElements(driver, ABOUT = new AboutDialog(this));
        PageFactory.initElements(driver, ASK_DIALOG = new AskDialog(this));
        PageFactory.initElements(driver, ASK_FOR_VALUE_DIALOG = new AskForValueDialog(this));
        PageFactory.initElements(driver, AVAILABLE_DEPENDENCIES = new AvailableDependencies(this));
        PageFactory.initElements(driver, BUILD = new Build(this));
        PageFactory.initElements(driver, BUTTON = new Button(this));
        PageFactory.initElements(driver, CODEASSISTANT = new CodeAssistant(this));
        PageFactory.initElements(driver, CONTEXT_MENU = new ContextMenu(this));
        PageFactory.initElements(driver, DELETE = new Delete(this));
        PageFactory.initElements(driver, DEPLOY_NODE_TYPE = new DeployNodeType(this));
        PageFactory.initElements(driver, EDITOR = new Editor(this));
        PageFactory.initElements(driver, FOLDER = new Folder(this));
        PageFactory.initElements(driver, FINDREPLACE = new FindReplace(this));
        PageFactory.initElements(driver, GOTOLINE = new GoToLine(this));
        PageFactory.initElements(driver, GET_URL = new GetURL(this));
        PageFactory.initElements(driver, INPUT = new Input(this));
        PageFactory.initElements(driver, LOADER = new Loader(this));
        PageFactory.initElements(driver, MENU = new Menu(this));
        PageFactory.initElements(driver, OUTLINE = new Outline(this));
        PageFactory.initElements(driver, OUTPUT = new Output(this));
        PageFactory.initElements(driver, OPEN_FILE_BY_URL = new OpenFileByURL(this));
        PageFactory.initElements(driver, OPEN_FILE_BY_PATH = new OpenFileByPath(this));
        PageFactory.initElements(driver, PREVIEW = new Preview(this));
        PageFactory.initElements(driver, PREVIEW_NODE_TYPE = new PreviewNodeType(this));
        PageFactory.initElements(driver, PERSPECTIVE = new Perspective(this));
        PageFactory.initElements(driver, PROPERTIES = new Properties(this));
        PageFactory.initElements(driver, RENAME = new Rename(this));
        PageFactory.initElements(driver, SAVE_AS_TEMPLATE = new SaveAsTemplate(this));
        PageFactory.initElements(driver, SELECT_WORKSPACE = new SelectWorkspace(this));
        PageFactory.initElements(driver, SEARCH = new Search(this));
        PageFactory.initElements(driver, SEARCH_RESULT = new SearchResult(this));
        PageFactory.initElements(driver, STATUSBAR = new Statusbar(this));
        PageFactory.initElements(driver, TOOLBAR = new Toolbar(this));
        PageFactory.initElements(driver, WARNING_DIALOG = new WarningDialog(this));
        PageFactory.initElements(driver, GOTOLINE = new GoToLine(this));
        PageFactory.initElements(driver, INFORMATION_DIALOG = new InformationDialog(this));
        PageFactory.initElements(driver, LOGIN = new Login(this));
        PageFactory.initElements(driver, LOG_READER = new LogReader(this));
        PageFactory.initElements(driver, CUSTOMIZE_TOOLBAR = new CustomizeToolbar(this));
        PageFactory.initElements(driver, PROGRESS_BAR = new ProgressBar(this));
        PageFactory.initElements(driver, CUSTOMIZE_HOTKEYS = new CustomizeHotkeys(this));
        PageFactory.initElements(driver, SHOW_KEYBOARD_SHORTCUTS = new ShowKeyboardShortcuts(this));
        PageFactory.initElements(driver, UPLOAD = new Upload(this));
        PageFactory.initElements(driver, WELCOME_PAGE = new WelcomePage(this));
        PageFactory.initElements(driver, CK_EDITOR = new CkEditor(this));
        PageFactory.initElements(driver, ERROR_MARKS = new ErrorMarks(this));
        PageFactory.initElements(driver, CODE_ASSISTANT_JAVA = new CodeAssistantJava(this));
        PageFactory.initElements(driver, POPUP = new PopupDialogsBrowser(this));
        PageFactory.initElements(driver, DEBUGER = new Debuger(this));
        PageFactory.initElements(driver, ORGINIZEIMPORT = new OrginizeImport(this));
        PageFactory.initElements(driver, PROJECTMENU = new ProjectsMenu(this));
        PageFactory.initElements(driver, DEPLOY = new DeployForm(this));
        PageFactory.initElements(driver, JAVAEDITOR = new JavaEditor(this));
        PageFactory.initElements(driver, PREFERENCES = new Preferences(this));
        PageFactory.initElements(driver, SSH = new Ssh(this));
        PageFactory.initElements(driver, FORMATTER = new Formatter(this));
        PageFactory.initElements(driver, INVITE_FORM = new InvitefromGoogle(this));
        PageFactory.initElements(driver, MAIL_CHECK = new MailCheck(this));
        PageFactory.initElements(driver, MAIL_RECEIVER = new MailReceiver(this));
        PageFactory.initElements(driver, GITHUB = new GitHub(this));
        PageFactory.initElements(driver, GOOGLE = new Google(this));
        PageFactory.initElements(driver, IMPORT_FROM_GITHUB = new ImportFromGitHub(this));
        PageFactory.initElements(driver, CREATE_PROJECT_FROM_SCRATHC = new CreateANewProjectFromScratch(this));
        PageFactory.initElements(driver, REFACTORING = new Refactoring(this));
        PageFactory.initElements(driver, MANAGE_ACCESS = new ManageAcces(this));
        PageFactory.initElements(driver, FILE = new File(this));
        PageFactory.initElements(driver, OPEN_RESOURCE = new OpenResource(this));
        PageFactory.initElements(driver, SHELL = new Shell(this));
        PageFactory.initElements(driver, IMPORT_A_SAMPLE_PRJ = new ImportASampleProject(this));
        PageFactory.initElements(driver, PROGRESS_PANEL_TAB = new ProgressPanelTab(this));
        PageFactory.initElements(driver, EXPLORER = new ProjectExplorer(this));
        PageFactory.initElements(driver, CREATE = new CreateProject(this));
        PageFactory.initElements(driver, OPEN = new OpenProject(this));
        PageFactory.initElements(driver, PACKAGE_EXPLORER = new PackageExplorer(this));
        PageFactory.initElements(driver, NAVIGATOR = new Navigator(this));
        PageFactory.initElements(driver, CREATE_NEW_CLASS = new CreateNewClassForm(this));
        PageFactory.initElements(driver, THEME = new Theme(this));
        PageFactory.initElements(driver, CHAT = new Chat(this));
        PageFactory.initElements(driver, NOTIFICATIONS = new Notifications(this));
        PageFactory.initElements(driver, GET_STARTED_WIZARD = new GetStartedWizard(this));
        PageFactory.initElements(driver, GIT = new Git(this));
        PageFactory.initElements(driver, JREBEL = new JRebel(this));
        PageFactory.initElements(driver, SELECT_PROJECT_TYPE = new SelectProjectType(this));
        PageFactory.initElements(driver, READ_ONLY_MODE = new ReadOnlyMode(this));
        PageFactory.initElements(driver, DATA_SOURCE_CONFIGURE = new ConfigureDataSource(this));
        PageFactory.initElements(driver, ERROR_TENANT_NAME_PAGE = new ErrorTenantNamePage(this));
        PageFactory.initElements(driver, FACTORY_URL = new FactoryURL(this));
        PageFactory.initElements(driver, PROGRESSOR_WINDOW = new ProgressorWindow(this));
        PageFactory.initElements(driver, GAE = new GaePaaS(this));
        PageFactory.initElements(driver, HEROKU = new HerokuPaaS(this));
        PageFactory.initElements(driver, APP_FOG = new AppFogPaaS(this));
        PageFactory.initElements(driver, AWS = new AwsPaaS(this));
        PageFactory.initElements(driver, OPEN_SHIFT = new OpenShiftPaaS(this));
        PageFactory.initElements(driver, CLOUD_BEES = new CloudBeesPaaS(this));
        PageFactory.initElements(driver, BITBUCKET = new BitBucket(this));
        PageFactory.initElements(driver, GERRITHUB = new GerritHub(this));
        PageFactory.initElements(driver, LOADING_BEHAVIOR = new LoadingBehaviorPage(this) {
        });
    }

    public WebDriver driver() {
        return driver;
    }

    /**
     * Select main frame of IDE.
     * <p/>
     * This method is used, after typing text in editor. To type text you must select editor iframe. After typing, to return to them main
     * frame, use selectMainFrame()
     */
    public void selectMainFrame() {
        driver().switchTo().defaultContent();
    }

    private String workspaceURL;

    public void setWorkspaceURL(String workspaceURL) {
        this.workspaceURL = workspaceURL;
    }

    public String getWorkspaceURL() {
        return workspaceURL;
    }

}
