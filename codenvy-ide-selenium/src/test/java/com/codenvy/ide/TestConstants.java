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

/** @author Ann Zhuleva */
public interface TestConstants {

    int multiple = 1;

    int IDE_INITIALIZATION_PERIOD = 3000 * multiple;

    int REDRAW_PERIOD = 1000 * multiple;

    int TYPE_DELAY_PERIOD = 30;

    int ANIMATION_PERIOD = 10;

    int FOLDER_REFRESH_PERIOD = 1000 * multiple;

    int PAGE_LOAD_PERIOD = 2000 * multiple;

    int EDITOR_OPEN_PERIOD = 2000 * multiple;

    int CODEMIRROR_PARSING_PERIOD = 1000 * multiple;

    int IDE_LOAD_PERIOD = 20000 * multiple;

    int SLEEP = 3000 * multiple;

    int SLEEP_SHORT = 500 * multiple;

    int WAIT_FOR_FILE_SAVING = 4000;

    int MIDDLE_WAIT_IN_SEC = 10;

    int LONG_WAIT_IN_SEC      = 100;
    //TODO i change timeout on 60 because if test fail timeout too long
    int VERY_LONG_WAIT_IN_SEC = 60;  // 15 minutes

    int SHORT_WAIT_IN_SEC = 2;

    /** Timeout in milliseconds to wait for element present. */
    int TIMEOUT = 10000 * multiple;

    //   /**
    //    * Period to wait for element present.
    //    */
    //   int WAIT_PERIOD = 60;

    /** Realm for GateIn gatein-domain */
    String REALM_GATEIN_DOMAIN = "exo-domain";

    /** root */
    String USER = "root";

    /** gtn */
    String PASSWD = "gtn";

    public interface NodeTypes {
        /** exo:groovyResourceContainer */
        public static final String EXO_GROOVY_RESOURCE_CONTAINER = "exo:groovyResourceContainer";

        /** nt:resource */
        public static final String NT_RESOURCE = "nt:resource";

        /** nt:file */
        public static final String NT_FILE = "nt:file";

        /** exo:googleGadget */
        public static final String EXO_GOOGLE_GADGET = "exo:googleGadget";
    }

//    /** Users, allowed in IDE */
//    public interface Users {
//        /** administrators and developers */
//        String ROOT = BaseTest.IDE_SETTINGS.getString("ide.user.root.name");
//
//        String ROOT_PASS = BaseTest.IDE_SETTINGS.getString("ide.user.root.password");
//
//        /** developers */
//        String DEV = BaseTest.IDE_SETTINGS.getString("ide.user.dev.name");
//
//        String DEV_PASS = BaseTest.IDE_SETTINGS.getString("ide.user.dev.password");
//
//    }

    String CODEMIRROR_EDITOR_LOCATOR = "//body[@class='editbox']";

    String CK_EDITOR_LOCATOR = "//body";

    String EDITOR_BODY_LOCATOR = "//body";


    public static final String UNTITLED_FILE_NAME = "Untitled file";
}
