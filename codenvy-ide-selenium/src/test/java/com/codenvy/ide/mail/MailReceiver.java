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
package com.codenvy.ide.mail;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.core.AbstractTestModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/** The class receives email */
public class MailReceiver extends AbstractTestModule {
    /** @param ide */
    public MailReceiver(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private static final Logger LOG = LoggerFactory.getLogger(MailCheck.class);

    private static String HOST = "imap.gmail.com";

    private static Message[] MESSAGES;

    private static Folder FOLDER;

    private static Store STORE;

    private static Pattern pattern;

    private static Matcher matcher;


    /*
     * The method receives email messages from host
     */
    public static void receiveInviteEmail(String user, String password) throws TimeoutException, MessagingException {
        Properties properties = System.getProperties(); // Get system properties
        Session session = Session.getDefaultInstance(properties); // Get the default Session object.
        Store store = session.getStore("imaps"); // Get a Store object that implements the specified protocol.
        try {
            store.connect(HOST, user,
                          password); //Connect to the current host using the specified username and password.
        } catch (Exception e) {
            e.printStackTrace();
        }
        Folder folder = store.getFolder("inbox"); //Create a Folder object corresponding to the given name.
        folder.open(Folder.READ_ONLY); // Open the Folder.

        Message[] messages = folder.getMessages();
        MailReceiver.STORE = store;
        MailReceiver.FOLDER = folder;
        MailReceiver.MESSAGES = messages;
    }

    public static void cleanMailBox(String user, String password) throws TimeoutException, MessagingException {
        Properties properties = System.getProperties(); // Get system properties
        Session session = Session.getDefaultInstance(properties); // Get the default Session object.
        Store store = session.getStore("imaps"); // Get a Store object that implements the specified protocol.
        try {
            store.connect(HOST, user,
                          password); //Connect to the current host using the specified username and password.
        } catch (Exception e) {
            throw new TimeoutException("Impossible connect to host : " + HOST + " /user -> " + user + " /password -> "
                                       + password);
        }
        Folder folder = store.getFolder("inbox"); //Create a Folder object corresponding to the given name.
        folder.open(Folder.READ_WRITE); // Open the Folder.

        Message[] messages = folder.getMessages();
        for (int i = 0, n = messages.length; i < n; i++) {
            messages[i].setFlag(Flags.Flag.DELETED, true);

        }
        LOG.info(user + " mail box cleaned.");
        // Close connection
        folder.close(true);
        store.close();
    }

    /*
     * The method gets appropriate string from message with specified parameters: Subject, Pattern for message body
     * to found 1-st e-mail: Subject = "Your Cloud IDE Domain",
     * tenant = "/create?tenantName=selenium???&user-mail=test.cldide@gmail.com"
     * 2-nd e-mail: Subject = "Your Cloud IDE Domain", tenant = "Your Cloud IDE Domain",
     * "selenium???.cloud-ide.com/cloud/?username=test.cldide@gmail.com"
     * set correct tenantName = selenium???
     * @param subject - the subject of the message
     * @param tenantName - key string to search link
     * @return the link or null
     */
    public static String getLink(String subject, String tenantName) throws MessagingException, IOException {
        Date date = new Date(0);
        String msg = "null";
        for (int i = 0; i < MESSAGES.length; i++) {
            if (MESSAGES[i].getSubject().equals(subject) &&
                (MESSAGES[i].getContent().toString().indexOf(tenantName) >= 0)
                && MESSAGES[i].getReceivedDate().after(date)) {
                msg = MESSAGES[i].getContent().toString();
                date = MESSAGES[i].getReceivedDate();
                try {
                    int startInd = msg.indexOf(tenantName);
                    int endInd = msg.indexOf("\"", startInd + 10);
                    msg = msg.substring(startInd, endInd);
                } catch (StringIndexOutOfBoundsException e) {
                    throw new StringIndexOutOfBoundsException("Unable found the string in the message : " + msg);
                }
            }
        }
        return msg.equals("null") ? null : msg;
    }

    /**
     * Get full message
     *
     * @param subject
     * @return invitation message
     * @throws MessagingException
     * @throws IOException
     */
    public static String getFullMessage(String subject) throws MessagingException, IOException {
        Date date = new Date(0);
        String msg = "null";
        for (int i = 0; i < MESSAGES.length; i++) {
            if (MESSAGES[i].getSubject().equals(subject) && MESSAGES[i].getReceivedDate().after(date)) {
                msg = MESSAGES[i].getContent().toString();
                date = MESSAGES[i].getReceivedDate();
            }
        }
        return msg.equals("null") ? null : msg;
    }


    public static String getConfirmLink(String subject, String tenantName) throws MessagingException, IOException, PatternSyntaxException {

        Date date = new Date(0);
        String msg = null;
        String confirmLink = "";
        for (int i = 0; i < MESSAGES.length; i++) {
            if (MESSAGES[i].getSubject().equals(subject) &&
                (MESSAGES[i].getContent().toString().contains(tenantName))
                && MESSAGES[i].getReceivedDate().before(new Date())) {
                msg = MESSAGES[i].getContent().toString();
                try {
                    String linkPattern =
                            "(http|https)://" + BaseTest.IDE_HOST + "/site/auth/create.*?" + "bearertoken=([A-Za-z0-9]).*\\&.*\"";
                    pattern = Pattern.compile(linkPattern);
                    matcher = pattern.matcher(msg);

                    while (matcher.find()) {
                        confirmLink = matcher.group().replace(" style", " ");
                    }

                } catch (StringIndexOutOfBoundsException e) {
                    throw new StringIndexOutOfBoundsException("Unable found the string in the message : " + msg);
                } catch (PatternSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!confirmLink.equals("")) {
            confirmLink = confirmLink.substring(0, confirmLink.length() - 1);
        }
        return confirmLink.equals("") ? null : confirmLink;
    }


    public static String getFactoryLink(String subject, String tenantName) throws MessagingException, IOException, PatternSyntaxException {

        Date date = new Date(0);
        String msg = null;
        String confirmLink = "";
        for (int i = 0; i < MESSAGES.length; i++) {
            if (MESSAGES[i].getSubject().equals(subject) &&
                (MESSAGES[i].getContent().toString().contains(tenantName))
                && MESSAGES[i].getReceivedDate().before(new Date())) {
                msg = MESSAGES[i].getContent().toString();
                try {
                    String linkPattern =
                            "(http|https)://" + BaseTest.IDE_HOST + "/" + "factory\\?id=\\w{16}";
                    pattern = Pattern.compile(linkPattern);
                    matcher = pattern.matcher(msg);

                    while (matcher.find()) {
                        confirmLink = matcher.group();
                    }

                } catch (StringIndexOutOfBoundsException e) {
                    throw new StringIndexOutOfBoundsException("Unable found the string in the message : " + msg);
                } catch (PatternSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        return confirmLink.equals("") ? null : confirmLink;
    }


    @Override
   /*
    * The method closes opened resources before gc
    */
    protected void finalize() throws Throwable {
        try {
            FOLDER.close(true);
            STORE.close(); // close store
        } catch (Exception e) {
            throw new Exception("Error closing store MailReciever");
        } finally {
            super.finalize();
        }

    }

}