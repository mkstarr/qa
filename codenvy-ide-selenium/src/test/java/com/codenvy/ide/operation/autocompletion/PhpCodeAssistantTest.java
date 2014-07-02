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
package com.codenvy.ide.operation.autocompletion;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** @author Evgen Vidolob */
public class PhpCodeAssistantTest extends BaseTest {

    private static final String PROJECT = PhpCodeAssistantTest.class.getSimpleName();

    private static final String FILE_NAME = "PHPTest.php";

    public static final String KEY_WORDS =
            "$GLOBALS\n$HTTP_COOKIE_VARS\n$HTTP_ENV_VARS\n$HTTP_GET_VARS\n$HTTP_POST_FILES\n$HTTP_POST_VARS\n$HTTP_SERVER_VARS" +
            "\n$HTTP_SESSION_VARS\n$_COOKIE\n$_ENV\n$_FILES\n$_GET\n$_POST\n$_REQUEST\n$_SERVER\n$_SESSION\nadd()\nadd_root()\naddaction" +
            "()\naddcolor()\naddentry()\naddfill()\naddshape()\naddstring()\nalign()\nattributes()\nchildren()\nchop()\nclose()\ncom_get" +
            "()\ncom_propset()\ncom_set()\ncv_add()\ncv_auth()\ncv_command()\ncv_count()\ncv_delete()\ncv_done()\ncv_init()\ncv_lookup()" +
            "\ncv_new()\ncv_report()\ncv_return()\ncv_reverse()\ncv_sale()\ncv_status()\ncv_textvalue()\ncv_void()\ndie()\ndir()" +
            "\ndiskfreespace()\ndomxml_getattr()\ndomxml_setattr()\ndoubleval()\ndrawarc()\ndrawcircle()\ndrawcubic()\ndrawcubicto()" +
            "\ndrawcurve()\ndrawcurveto()\ndrawglyph()\ndrawline()\ndrawlineto()\ndtd()\ndumpmem()\nfbsql()\nfputs()\nget_attribute()" +
            "\ngetascent()\ngetascent()\ngetattr()\ngetdescent()\ngetheight()\ngetleading()\ngetshape1()\ngetwidth()\ngzputs()" +
            "\ni18n_convert()\ni18n_discover_encoding()\ni18n_http_input()\ni18n_http_output()\ni18n_internal_encoding()" +
            "\ni18n_ja_jp_hantozen()\ni18n_mime_header_decode()\ni18n_mime_header_encode()\nimap_create()\nimap_fetchtext()" +
            "\nimap_getmailboxes()\nimap_getsubscribed()\nimap_header()\nimap_listmailbox()\nimap_listsubscribed()\nimap_rename()" +
            "\nimap_scan()\nimap_scanmailbox()\nini_alter()\nis_double()\nis_integer()\nis_long()\nis_real()\nis_writeable()\njoin()" +
            "\nlabelframe()\nlast_child()\nlastchild()\nldap_close()\nmagic_quotes_runtime()\nmbstrcut()\nmbstrlen()\nmbstrpos()" +
            "\nmbstrrpos()\nmbsubstr()\nming_setcubicthreshold()\nming_setscale()\nmove()\nmovepen()\nmovepento()\nmoveto()\nmsql()" +
            "\nmsql_createdb()\nmsql_dbname()\nmsql_dropdb()\nmsql_fieldflags()\nmsql_fieldlen()\nmsql_fieldname()\nmsql_fieldtable()" +
            "\nmsql_fieldtype()\nmsql_freeresult()\nmsql_listdbs()\nmsql_listfields()\nmsql_listtables()\nmsql_numfields()\nmsql_numrows" +
            "()\nmsql_regcase()\nmsql_selectdb()\nmsql_tablename()\nmssql_affected_rows()\nmssql_close()\nmssql_connect()" +
            "\nmssql_data_seek()\nmssql_fetch_array()\nmssql_fetch_field()\nmssql_fetch_object()\nmssql_fetch_row()\nmssql_field_seek()" +
            "\nmssql_free_result()\nmssql_get_last_message()\nmssql_min_client_severity()\nmssql_min_error_severity()" +
            "\nmssql_min_message_severity()\nType for more results";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.APPLICATION_PHP,


                                                       "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/php/php.php");
        } catch (Exception e) {
            fail("Can't create test project");
        }
    }

    @Test
    public void testPhpKeyWords() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(20);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("Type for more results");
        assertTrue(IDE.CODE_ASSISTANT_JAVA.getAllFormProposalsText().equals(KEY_WORDS));
        IDE.CODE_ASSISTANT_JAVA.closeForm();
    }


    @Test
    public void selectAndInsertSomeKeyWords() throws Exception {
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("cho");
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("chop()");
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(";");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("chop();");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor("cv_cou");
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("cv_count()");
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(";");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("cv_count();");
    }

    // TODO On this momemnt in current PHP editor work only base token for autocomplete
    // if editor will be impoved we should also improve this test
    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

}
