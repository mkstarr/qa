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
package com.codenvy.ide.preferences;

import com.codenvy.ide.BaseTest;

/**
 * @author <a href="mailto:oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 *
 */
public abstract class AbstractCustomizeHotkeys extends BaseTest {
    static final String MESSAGE_LABEL = "ideCustomizeHotKeysMessageLabel";

    static final int NUMBER_OF_COMMANDS = 500;

    static final String GOOGLE_GADGET_FILE = "GoogleGadget.xml";

    static final String DEFAULT_TEXT_IN_GADGET = "Hello, world!";

    static String FOLDER_NAME;

    static final String INFO_MESSAGE_STYLE = "exo-cutomizeHotKey-label-info";

    static final String ERROR_MESSAGE_STYLE = "exo-cutomizeHotKey-label-error";

    static final String BIND_BUTTON_LOCATOR = "ideCustomizeHotKeysViewBindButton";

    static final String SAVE_BUTTON_LOCATOR = "ideCustomizeHotKeysViewOkButton";

    static final String CUSTOMIZE_HOTKEYS_FORM_LOCATOR = "ideCustomizeHotKeysView-window";

    static final String UNBIND_BUTTON_LOCATOR = "ideCustomizeHotKeysViewUnbindButton";

    static final String CANCEL_BUTTON_LOCATOR = "ideCustomizeHotKeysViewCancelButton";

    static final String TEXT_FIELD_LOCATOR = "ideCustomizeHotKeysViewHotKeyField";

       String locator;

    interface Commands {
        public static final String CREATE_FILE_FROM_TEMPLATE = "Create File From Template...";

        public static final String NEW_CSS_FILE = "New CSS";

        public static final String NEW_TEXT_FILE = "New TEXT";

        public static final String NEW_HTML_FILE = "New HTML";
    }

}
