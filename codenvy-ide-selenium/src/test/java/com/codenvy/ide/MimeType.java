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

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.php
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */

/*
 * MediaType.java
 *
 * Created on March 22, 2007, 2:35 PM
 *
 */

package com.codenvy.ide;

/**
 * An abstraction for a media type. Instances are immutable.
 *
 * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7">HTTP/1.1 section 3.7</a>
 */
public class MimeType {

    public final static String WILDCARD = "*/*";

    /** "application/xml" */
    public final static String APPLICATION_XML = "application/xml";

    /** "application/atom+xml" */
    public final static String APPLICATION_ATOM_XML = "application/atom+xml";

    /** "application/xhtml+xml" */
    public final static String APPLICATION_XHTML_XML = "application/xhtml+xml";

    /** "application/svg+xml" */
    public final static String APPLICATION_SVG_XML = "application/svg+xml";

    /** "application/json" */
    public final static String APPLICATION_JSON = "application/json";

    /** "application/x-www-form-urlencoded" */
    public final static String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

    /** "multipart/form-data" */
    public final static String MULTIPART_FORM_DATA = "multipart/form-data";

    /** "application/octet-stream" */
    public final static String APPLICATION_OCTET_STREAM = "application/octet-stream";

    /** "text/plain" */
    public final static String TEXT_PLAIN = "text/plain";

    /** "text/xml" */
    public final static String TEXT_XML = "text/xml";

    /** "text/html" */
    public final static String TEXT_HTML = "text/html";

    /** "text/css" */
    public final static String TEXT_CSS = "text/css";

    /**
     * temporary  application/x-jaxrs+groovy replaced on application/x-jaxrs-groovy to avoid error of PROPFIND response
     * which returns "application/x-jaxrs groovy
     */
    public final static String GROOVY_SERVICE = "application/x-jaxrs+groovy";
//"script/groovy"; //application/x-jaxrs+groovy

    /** "application/x-groovy" */
    public final static String APPLICATION_GROOVY = "application/x-groovy";

    /** "application/javascript" */
    public final static String APPLICATION_JAVASCRIPT = "application/javascript";

    /** "application/x-javascript" */
    public final static String APPLICATION_X_JAVASCRIPT = "application/x-javascript";

    /** "text/javascript" */
    public final static String TEXT_JAVASCRIPT = "text/javascript";

    /** "application/x-google-gadget" */
    public final static String GOOGLE_GADGET = "application/x-google-gadget";

    /** "application/x-uwa-widget" */
    public final static String UWA_WIDGET = "application/x-uwa-widget";

    /**
     * temporary "application/x-groovy+html" replaced on "application/x-groovy-html" to avoid error of PROPFIND response
     * which returns "application/x-groovy html"
     */
    public final static String GROOVY_TEMPLATE = "application/x-groovy+html";

    /** Chromattic Data Object */
    public final static String CHROMATTIC_DATA_OBJECT = "application/x-chromattic+groovy";

    /** "application/java" */
    public final static String APPLICATION_JAVA = "application/java";

    /** "application/jsp" */
    public final static String APPLICATION_JSP = "application/jsp";

    /** Ruby script Mime type "application/x-ruby" */
    public final static String APPLICATION_RUBY = "application/x-ruby";

    /** PHP script Mime types */
    public final static String APPLICATION_PHP         = "application/php";
    public final static String APPLICATION_X_PHP       = "application/x-php";
    public final static String APPLICATION_X_HTTPD_PHP = "application/x-httpd-php";

    /** Diff mime-type "text/x-diff" */
    public final static String DIFF = "text/x-diff";

    /** text/x-python */
    public final static String TEXT_X_PYTHON = "text/x-python";

    /** text/yaml */
    public final static String TEXT_YAML = "text/yaml";
}