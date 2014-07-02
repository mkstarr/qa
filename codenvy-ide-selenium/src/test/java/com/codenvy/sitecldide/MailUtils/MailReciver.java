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
package com.codenvy.sitecldide.MailUtils;

import com.codenvy.ide.BaseTest;

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

/**
 * @author Musienko Maxim
 *
 */


/**
 * auxiliary class for receive mail and search of the link for tenant creation into email content. Class works only gmail boxes. IMAP must
 * be enabled for correct working. Also must be disabled 2 step verification.
 */
public class MailReciver {
    String confirmLink;

    private String user;

    private String password;

    private String tenantName;

    private Store mailStore;

    private Folder mailBoxFolder;


    protected String host = "imap.gmail.com";

    private Pattern pattern;

    private Matcher matcher;

    /**
     * convenient to use if will be used a work with specified
     * tenant name
     * <p/>
     * for init user name, password and tenant name
     *
     * @param user
     *         mail name
     * @param password
     *         password for mailbox
     * @param tenantName
     *         your tenant name
     */
    public MailReciver(String user, String password, String tenantName) {
        this.user = user;
        this.password = password;
        this.tenantName = tenantName;

    }


    /**
     * convenient to use if will be used a simple operations with a mail box (receive, delete, read)
     * for init user name, password and tenant name
     *
     * @param user
     *         mail name
     * @param password
     *         password for mailbox
     */
    public MailReciver(String user, String password) {
        this.user = user;
        this.password = password;

    }


    /**
     * Creates session with email server with using IMAP - mail protocol. Return array messages
     *
     * @param user
     * @param password
     * @return
     * @throws TimeoutException
     * @throws MessagingException
     */
    private Message[] receiveInviteEmail(String user, String password) throws TimeoutException, MessagingException {
        Properties properties = System.getProperties(); // Get system properties
        Session session = Session.getDefaultInstance(properties); // Get the default Session object.
        mailStore = session.getStore("imaps"); // Get a Store object that implements the specified protocol.
        Message[] msgs = null;
        Folder folder = null;
        try {
            mailStore.connect(host, user,
                              password); //Connect to the current host using the specified username and password.
            mailBoxFolder = mailStore.getFolder("inbox"); //Create a Folder object corresponding to the given name.
            mailBoxFolder.open(Folder.READ_ONLY); // Open the Folder.
            msgs = mailBoxFolder.getMessages();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgs;
    }

    /**
     * delete all mails in mailbox.
     *
     * @throws TimeoutException
     * @throws MessagingException
     */
    public void cleanMailBox() throws TimeoutException, MessagingException {
        Properties properties = System.getProperties(); // Get system properties
        Session session = Session.getDefaultInstance(properties); // Get the default Session object.
        Store store = session.getStore("imaps"); // Get a Store object that implements the specified protocol.
        Message[] msgs = null;
        Folder folder = null;
        try {
            store.connect(host, user,
                          password); //Connect to the current host using the specified username and password.

            folder = store.getFolder("inbox"); //Create a Folder object corresponding to the given name.
            folder.open(Folder.READ_WRITE); // Open the Folder.

            Message[] messages = folder.getMessages();
            for (int i = 0, n = messages.length; i < n; i++) {
                messages[i].setFlag(Flags.Flag.DELETED, true);


            }
        } catch (Exception e) {
            throw new TimeoutException("Impossible connect to host : " + host + " /user -> " + user + " /password -> "
                                       + password);
        } finally {
            folder.close(true);
            store.close();
        }

    }

    /**
     * Gets content from email and search web-link with confirm for tenant creation
     *
     * @param subject
     *         in seconds
     * @param tenantName
     *         in seconds
     * @return
     * @throws MessagingException
     * @throws IOException
     * @throws PatternSyntaxException
     * @throws TimeoutException
     */
    public String getConfirmLink(String subject, String tenantName)
            throws MessagingException, IOException, PatternSyntaxException, TimeoutException {
        Date date = new Date(0);
        String msg = null;
        String confirmLink = "";
        Message[] messages = receiveInviteEmail(user, password);
        for (int i = 0; i < messages.length; i++) {
            if (messages[i].getSubject().equals(subject) &&
                (messages[i].getContent().toString().contains(tenantName))
                && messages[i].getReceivedDate().before(new Date())) {
                msg = messages[i].getContent().toString();
                try {
                    String linkPattern = "(http|https)://" + BaseTest.IDE_HOST + "/" + ".*" + "bearertoken=([A-Za-z0-9])*\\&?";
                    pattern = Pattern.compile(linkPattern);
                    matcher = pattern.matcher(msg);

                    while (matcher.find()) {
                        confirmLink = matcher.group();
                    }

                } catch (StringIndexOutOfBoundsException e) {
                    throw new StringIndexOutOfBoundsException("Unable found the string in the message : " + msg);
                } catch (PatternSyntaxException e) {
                    e.printStackTrace();
                } finally {
                    mailBoxFolder.close(true);
                    mailStore.close();
                }
            }
        }
        return confirmLink.equals("") ? null : confirmLink;
    }


    public String getForgotPassLink(String subject, String keyWordInMail)
            throws MessagingException, IOException, PatternSyntaxException, TimeoutException {
        Date date = new Date(0);
        String msg = null;
        String confirmLink = "";
        Message[] messages = receiveInviteEmail(user, password);
        for (int i = 0; i < messages.length; i++) {
            if (messages[i].getSubject().equals(subject) &&
                (messages[i].getContent().toString().contains(keyWordInMail))
                && messages[i].getReceivedDate().before(new Date())) {
                msg = messages[i].getContent().toString();
                try {
                    String linkPattern = "(http|https)://" + BaseTest.IDE_HOST + "/site/" + ".*" + "setup-password" + ".*" + "id=" +
                                         "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}";
                    pattern = Pattern.compile(linkPattern);
                    matcher = pattern.matcher(msg);

                    while (matcher.find()) {
                        confirmLink = matcher.group();

                    }

                } catch (StringIndexOutOfBoundsException e) {
                    throw new StringIndexOutOfBoundsException("Unable found the string in the message : " + msg);
                } catch (PatternSyntaxException e) {
                    e.printStackTrace();
                } finally {
                    mailBoxFolder.close(true);
                    mailStore.close();
                }
            }
        }
        return confirmLink.equals("") ? null : confirmLink;
    }

    /**
     * get all subjects headers from user gmail box
     *
     * @return
     * @throws TimeoutException
     * @throws MessagingException
     */
    public String getSubjects() throws TimeoutException, MessagingException {
        StringBuilder subjectsStr = new StringBuilder("*");
        for (Message msg : receiveInviteEmail(user, password)) {
            subjectsStr.append(msg.getSubject());
        }
        return subjectsStr.toString();
    }

    /**
     * time between retries getting of the confirmation link
     *
     * @param responceDelay
     *         in seconds
     * @param maxTimeAttemps
     *         in seconds
     *         time during which method trying get confirmation link
     * @return
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws MessagingException
     * @throws IOException
     */
    public String waiAndGetConfirmLink(int responceDelay, int maxTimeAttemps)
            throws InterruptedException, TimeoutException, MessagingException, IOException {

        String confLink = null;
        while (maxTimeAttemps >= responceDelay) {
            confLink = getConfirmLink("Your Codenvy Workspace", tenantName);
            if (confLink != null) {
                break;
            } else {
                Thread.sleep(responceDelay * 1000);
                maxTimeAttemps = maxTimeAttemps - responceDelay;
            }

        }
        Thread.sleep((maxTimeAttemps % responceDelay) * 1000);

        if (confLink == null) throw new TimeoutException("There are not any messages from codenvy in mailbox");
        return confLink;
    }


    /**
     * time between retries getting of the confirmation link for recover password mail
     *
     * @param responceDelay
     *         in seconds
     * @param maxTimeAttemps
     *         in seconds
     *         time during which method trying get confirmation link
     * @return returns login and password with '$' separator
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws MessagingException
     * @throws IOException
     */
    public String waiAndGetForgotPassLink(int responceDelay, int maxTimeAttemps)
            throws InterruptedException, TimeoutException, MessagingException, IOException {

        String confLink = null;
        while (maxTimeAttemps >= responceDelay) {
            confLink = getForgotPassLink("Codenvy password recover", "Have You Lost Your Password?");
            if (confLink != null) {
                break;
            } else {
                Thread.sleep(responceDelay * 1000);
                maxTimeAttemps = maxTimeAttemps - responceDelay;
            }

        }
        Thread.sleep((maxTimeAttemps % responceDelay) * 1000);

        if (confLink == null) throw new TimeoutException("There are not any messages from codenvy in mailbox");

        return confLink;
    }


}
