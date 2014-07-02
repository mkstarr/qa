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
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 *
 */
public interface ToolbarCommands {

    public interface Editor {
        String FIND_REPLACE = "Find/Replace...";

        String UNDO = "Undo Typing";

        String REDO = "Redo Typing";

        String LOCK_FILE = "Lock File";

        String UNLOCK_FILE = "Unlock File";
    }

    public interface File {
        String SAVE = "Save";

        String SAVE_AS = "Save As...";

        String DELETE = "Delete Item(s)...";

        String REFRESH = "Refresh Selected Folder";

        String CUT_SELECTED_ITEM = "Cut Selected Item(s)";

        String COPY_SELECTED_ITEM = "Copy Selected Item(s)";

        String PASTE = "Paste Selected Item(s)";

        final String SEARCH = "Search...";

        String PACKAGE_EXPLORER = "Package Explorer";
    }

    public interface View {
        String SHOW_OUTLINE = "Show Outline";

        String HIDE_OUTLINE = "Hide Outline";

        String SHOW_DOCUMENTATION = "Show Documentation";

        String HIDE_DOCUMENTATION = "Hide Documentation";

        String SHOW_PROPERTIES = "Show Properties";

        String VIEW_VERSION_HISTORY = "View Item Version History";

        String HIDE_VERSION_HISTORY = "Hide Item Version History";

        String VIEW_VERSION = "View Item Version...";

        String VIEW_NEWER_VERSION = "View Newer Version";

        String VIEW_OLDER_VERSION = "View Older Version";
    }

    public interface Run {
        String SHOW_PREVIEW = "Show Preview";

        String SHOW_GADGET_PREVIEW = "Show Gadget Preview";

        String DEPLOY_GADGET = "Deploy Gadget to GateIn";

        String UNDEPLOY_GADGET = "UnDeploy Gadget from GateIn";

        String RUN_GROOVY_SERVICE = "Run in Sandbox";

        String VALIDATE_GROOVY_SERVICE = "Validate REST Service";

        String DEPLOY_GROOVY_SERVICE = "Deploy REST Service";

        String UNDEPLOY_GROOVY_SERVICE = "Undeploy REST Service";

        String DEPLOY_SANDBOX = "Deploy REST Service to Sandbox";

        String UNDEPLOY_SANDBOX = "Undeploy REST Service from Sandbox";

        String SET_AUTOLOAD = "Set REST Service Autoload";

        String PREVIEW_NODE_TYPE = "Preview node type";

        String DEPLOY_NODE_TYPE = "Deploy node type";

        String LAUNCH_REST_SERVICE = "Launch REST Service...";

        String UNSET_AUTOLOAD = "Unset REST Service Autoload";

    }

    public interface PackageExplorer {
        String PACKAGE_EXPLORER = "Package Explorer";
    }


    public interface Collaboration {
        String COLLABORATION = "Collaboration";
    }
}
