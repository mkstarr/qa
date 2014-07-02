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

/**
 * Created by The eXo Platform SAS.
 *
 * @author Oksana Vereshchaka
 */
public interface MenuCommands {
    public interface New {

        String NEW = "New";

        String PACKAGE = "Package";

        String XML_FILE = "XML";

        String HTML_FILE = "HTML";

        String TEXT_FILE = "Text";

        String JAVASCRIPT_FILE = "JavaScript";

        String CSS_FILE = "CSS";

        String OPENSOCIAL_GADGET_FILE = "OpenSocial Gadget";

        String GROOVY_TEMPLATE_FILE = "Template";

        String FOLDER = "Folder...";

        String JAVA_PROJECT = "Java Project...";

        String JAVA_SPRING_PROJECT = "Java Spring Project...";

        String PROJECT_TEMPLATE = "Project Template...";

        String JAVA_CLASS = "Java Class";

        String JSP = "JSP";

        String RUBY = "Ruby File";

        String PHP = "PHP File";

        String PYTHON = "Python File";

    }

    public interface Run {

        public static final String RUN = "Run";

        public static final String UNDEPLOY_REST_SERVICE = "Undeploy";

        public static final String DEPLOY_REST_SERVICE = "Deploy";

        public static final String DEPLOY_SANDBOX = "Deploy to Sandbox";

        public static final String UNDEPLOY_SANDBOX = "Undeploy from Sandbox";

        public static final String SET_AUTOLOAD = "Set Autoload";

        public static final String UNSET_AUTOLOAD = "Unset Autoload";

        public static final String LAUNCH_REST_SERVICE = "Launch REST Service...";

        public static final String RUN_GROOVY_SERVICE = "Run in Sandbox";

        public static final String SHOW_PREVIEW = "Show Preview";

        public static final String SHOW_GADGET_PREVIEW = "Show Gadget Preview";

        public static final String SHOW_GROOVY_TEMPLATE_PREVIEW = "Show Template Preview";

        public static final String VALIDATE = "Validate";

        public static final String PREVIEW_NODE_TYPE = "Preview node type";

        public static final String DEPLOY_NODE_TYPE = "Deploy node type";

        public static final String DEPLOY_GADGET = "Deploy Gadget to GateIn";

        public static final String UNDEPLOY_GADGET = "UnDeploy Gadget from GateIn";

        public static final String DEBUG_APPLICATION = "Debug Application";

        public static final String RUN_APPLICATION = "Run Application";

        public static final String STOP_APPLICATION = "Stop Application";

        public static final String UPDATE_APPLICATION = "Update Application";

        public static final String SHOW_LOGS = "Show Logs...";

    }

    public interface View {
        String VIEW = "View";

        String OUTLINE = "Outline";

        String PROPERTIES = "Properties";

        String GO_TO_FOLDER = "Go to Folder";

        String GET_URL = "Get URL...";

        String SHOW_PROPERTIES = "Properties";

        String VERSION_HISTORY = "Version History...";

        String VERSION_LIST = "Version...";

        String NEWER_VERSION = "Newer Version";

        String OLDER_VERSION = "Older Version";

        String LOG_READER = "Log";

        String SHOW_HIDDEN_FILES = "Show Hidden Files";

        String COLLABORATION = "Collaboration";

        String PROGRESS = "Progress";

        String OUTPUT = "Output";
    }

    public interface File {
        public static final String FILE = "File";

        public static final String UPLOAD_FILE = "Upload File...";

        public static final String UPLOAD_FOLDER = "Upload Zipped Folder...";

        public static final String OPEN_LOCAL_FILE = "Open Local File...";

        public static final String OPEN_FILE_BY_PATH = "Open File By Path...";

        public static final String OPEN_FILE_BY_URL = "Open by URL...";

        public static final String DOWNLOAD = "Download...";

        public static final String DOWNLOAD_ZIPPED_FOLDER = "Download Zipped Folder...";

        public static final String SAVE = "Save";

        public static final String SAVE_AS = "Save As...";

        public static final String SAVE_ALL = "Save All";

        public static final String SAVE_AS_TEMPLATE = "Save As Template...";

        public static final String DELETE = "Delete...";

        public static final String RENAME = "Rename...";

        public static final String SEARCH = "Search...";

        public static final String REFRESH = "Refresh";

        public static final String RESTORE_VERSION = "Restore to Version";

        public static final String REFRESH_TOOLBAR = "Refresh Selected Folder";

        public static final String CLOSE = "Close";

        public static final String NEW = "New";
    }

    public interface Edit {
        public static final String EDIT_MENU = "Edit";

        public static final String ADD_BLOCK_COMMENT = "Add Block Comment";

        public static final String REMOVE_BLOCK_COMMENT = "Remove Block Comment";

        public static final String TOGGLE_COMMENT = "Toggle Comment";

        public static final String UNDO_TYPING = "Undo Typing";

        public static final String REDO_TYPING = "Redo Typing";

        public static final String CUT_TOOLBAR = "Cut Selected Item(s)";

        public static final String COPY_TOOLBAR = "Copy Selected Item(s)";

        public static final String PASTE_TOOLBAR = "Paste Selected Item(s)";

        public static final String CUT_MENU = "Cut Item(s)";

        public static final String COPY_MENU = "Copy Item(s)";

        public static final String PASTE_MENU = "Paste Item(s)";

        public static final String HIDE_LINE_NUMBERS = "Hide Line Numbers";

        public static final String SHOW_LINE_NUMBERS = "Show Line Numbers";

        public static final String FORMAT = "Format";

        public static final String SELECT_ALL = "Select All";

        public static final String DELETE = "Delete";

        public static final String GO_TO_LINE = "Go to Line...";

        public static final String DELETE_CURRENT_LINE = "Delete Current Line";

        public static final String FIND_REPLACE = "Find/Replace...";

        public static final String LOCK_FILE = "Lock File";

        public static final String UNLOCK_FILE = "Unlock File";

        public static final String MOVE_LINE_UP = "Move Line Up";

        public static final String MOVE_LINE_DOWN = "Move Line Down";

        public static final String LUCK_UNLOCK_FILE = "Lock " + "\\" + " Unlock File";

        public static final String SOURCE = "Source";

        public static final String REFACTOR = "Refactor";

        public static final String RENAME = "Rename...";

        public static final String SEND_CODE_POINTER = "Send Code Pointer";

        public static final String FOLD_SELECTION = "Fold Selection";

    }

    public interface CodeEditors {
        public static final String CODE_MIRROR = "Code Editor";

        public static final String CK_EDITOR = "WYSWYG Editor";
    }

    public interface Help {
        public static final String HELP = "Help";

        public static final String ABOUT = "About...";

        public static final String SHOW_KEYBOARD_SHORTCUTS = "Show Keyboard Shortcuts...";

        public static final String DOCUMENTATION = "Documentation";

        public static final String CONTACT_SUPPORT = "Contact Support";

        public static final String SUBMIT_FEEDBACK = "Vote for Features";
    }

    public interface Share {
        public static final String SHARE = "Share";

        public static final String FACTORY_URL = "Factory URL...";

        public static final String MANAGE_ACCESS = "Manage Access";

        public static final String INVITE_DEVELOPERS = "Invite Developers...";

        public static final String INVITE_GITHUB_COLLABORATORS = "Invite GitHub Collaborators...";
    }

    public interface Window {
        public static final String WINDOW = "Window";

        public static final String SHOW_VIEW = "Show View";

        public static final String NAVIGATION = "Navigation";

        public static final String PREFERNCESS = "Preferences...";

        public static final String FORMATTER = "Formatter";

        public static final String NEXT_EDITOR = "Next Editor";

        public static final String PREVIOUS_EDITOR = "Previous Editor";

        public static final String WELCOME = "Welcome";

        public interface ShowView {
            public static final String NAVIGATOR = "Navigator";

            public static final String PROJECT_EXPLORER = "Project Explorer";

            public static final String PACKAGE_EXPLORER = "Package Explorer";
        }
    }

    public interface Git {

        String GIT = "Git";

        String ADD = "Add...";

        String RESET = "Reset...";

        String REMOVE = "Remove...";

        String COMMIT = "Commit...";

        String BRANCHES = "Branches...";

        String MERGE = "Merge...";

        String REMOTE = "Remote";

        String RESET_INDEX = "Reset Index...";

        String INITIALIZE_REPOSITORY = "Initialize Repository";

        String CLONE_REPOSITORY = "Clone Repository...";

        String DELETE_REPOSITORY = "Delete Repository...";

        String SHOW_HISTORY = "Show History...";

        String STATUS = "Status";

        String GIT_URL = "Git URL (Read-Only)...";

        String PUSH = "Push...";

        String FETCH = "Fetch...";

        String PULL = "Pull...";

        String REMOTES = "Remotes...";
    }

    public interface Preferences {
        String CUSTOMIZE_TOOLBAR = "Customize Toolbar";

        String CUSTOMIZE_HOTKEYS = "Customize hotkeys";

        String WORKSPACE = "Workspace";

        String FORMATTER = "Formatter";

        String SSH_KEY = "Ssh Keys";

    }

    public interface Project {
        String PROJECT = "Project";

        public interface PaaS {

            String PAAS = "PaaS";

            String GAE = "Google App Engine";

            String HEROKU = "Heroku";

            String OPENSHIFT = "OpenShift";

            String APP_FOG = "AppFog";

            String CLOUD_BEES = "CloudBees";
        }

        String NEW = "New";

        String OPEN_PROJECT = "Open...";

        String OPEN_RECOURCE = "Open Resource...";

        String CLOSE_PROJECT = "Close";

        String EMPTY_PROJECT = "Empty Project...";

        String CREATE_PROJECT = "Create Project...";

        String FROM_TEMPLATE = "From Template...";

        String CONFIGURE_CLASS_PATH = "Configure Classpath...";

        String BUILD_PROJECT = "Build";

        String BUILD_AND_PUBLISH_PROJECT = "Build & Publish";

        String UPDATE_DEPENDENCY = "Update Dependency";

        String IMPORT_FROM_GITHUB = "Import from GitHub...";

        String CREATE_MODULE = "Create Module...";

        String PROJECT_PROPERTIES = "Properties...";

        String DATASOURCE = "Datasource...";

        String ENABLE_COLLABORATION_MODE = "Enable Collaboration Mode";

        String DISABLE_COLLABORATION_MODE = "Disable Collaboration Mode";

        String SHOW_SYNTAX_ERROR_HIGHLIGHTING = "Show Syntax Error Highlighting";

        String HIDE_SYNTAX_ERROR_HIGHLIGHTING = "Hide Syntax Error Highlighting";

    }

    public interface Source {
        String GENERATE_GETTERS_AND_SETTERS = "Generate Getters and Setters";

        String GENERATE_CONSTRUCTOR = "Generate Constructor using Fields...";
    }

    public interface PaaS {
        String PAAS = "PaaS";

        public interface Heroku {
            String HEROKU = "Heroku";

            String SWITCH_ACCOUNT = "Switch account...";

            String CREATE_APPLICATION = "Create application...";

            String DELETE_APPLICATION = "Delete application...";

            String RENAME_APPLICATION = "Rename application...";

            String APPLICATION = "Applications...";

            String APPLICATION_INFO = "Application info...";

            String DEPLOY_PUBLIC_KEY = "Deploy public key";

            String RAKE = "Rake...";
        }

        public interface OpenShift {
            String OPENSHIFT = "OpenShift";

            String CREATE_DOMAIN = "Create domain...";

            String CREATE_APPLICATION = "Create application...";

            String DELETE_APPLICATION = "Delete application...";

            String APPLICATION_INFO = "Application info...";

            String USER_INFO = "User info...";

            String CHANGE_NAME_SPACE = "Change Namespace...";

            String UPDATE_SSH_PUBLIC_KEY = "Update SSH public key...";

            String APPLICATIONS = "Applications...";

            String SWITCH_ACCOUNT = "Switch account...";


        }

        public interface CloudFoundry {

            String CLOUDFOUNDRY = "CloudFoundry";

            String CREATE_APPLICATION = "Create Application";

            String UPDATE_APPLICATION = "Update Application";

            String DELETE_APPLICATION = "Delete Application...";

            String APPLICATION_INFO = "Application Info...";

            String START_APPLICATION = "Start Application";

            String STOP_APPLICATION = "Stop Application";

            String RESTART_APPLICATION = "Restart Application";

            String APPLICATION_URLS = "Application URLs";

            String UPDATE_MEMORY = "Update Memory...";

            String UPDATE_INSTANCES = "Update Instances...";

            String SWITCH_ACCOUNT = "Switch Account...";

            String APPPLICATIONS = "Applications";

        }

        public interface AppFog {
            String APP_FOG = "AppFog";

            String CREATE_APPLICATION = "Create Application...";

            String APLICATIONS = "Applications...";

            String SWITCH_ACCOUNT = "Switch Account...";

        }


        public interface CloudBees {
            String CLOUD_BEES = "CloudBees";

            String CREATE_APPLICATION = "Create Application...";

            String APLICATIONS = "Applications...";

            String SWITCH_ACCOUNT = "Switch Account...";

            String CREATE_ACCOUNT = "Create Account...";
        }

        public interface ElasticBeanstalk {

            String ELASTIC_BEANSTALK = "Elastic Beanstalk";

            String CREATE_APPLICATION = "Create Application...";

            String MANAGE_APPLICATION = "Manage Application...";

            String SWITCH_ACCOUNT = "Switch Account...";

            String EC2_MANAGEMENT = "EC2 Management";

            String EC3_MANAGEMENT = "S3 Management";
        }

        public interface GoogleAppEngine {
            String GOOGLEAPPENGINE = "Google App Engine";

            String UPDATE_APPLICATION = "Update Application";

            String CREATE_APPLICATION = "Create Application...";

            String LOGIN = "Login";

            String LOGOUT = "Logout";
        }

        public interface Tier3WebFabric {
            String TIRE_FABRIC_3 = "Tier3 Web Fabric";

            String CREATE_APPLICATION = "Create Application...";

            String APPLICATIONS = "Applications...";

            String SWITCH_ACCOUNT = "Switch Account...";
        }

        public interface Manymo {

            String MANYMO = "ManyMo Android";

            String DEPLOY = "Deploy";
        }


    }

}
