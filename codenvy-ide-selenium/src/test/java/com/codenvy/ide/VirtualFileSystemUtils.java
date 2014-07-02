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

import com.codenvy.ide.core.Response;

import org.apache.commons.io.IOUtils;
import org.everrest.core.impl.provider.json.JsonException;
import org.everrest.core.impl.provider.json.JsonParser;
import org.everrest.core.impl.provider.json.JsonValue;
import org.everrest.core.impl.provider.json.ObjectBuilder;
import org.exoplatform.ide.vfs.shared.Link;
import org.exoplatform.ide.vfs.shared.VirtualFileSystemInfo;
import org.exoplatform.ide.vfs.shared.VirtualFileSystemInfoImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Vitaly Parfonov
 */
public class VirtualFileSystemUtils {

    private static VirtualFileSystemInfo vfsInfo;

    private static Map<String, Link> rootLinks;

    static {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    /**
     * @param path
     * @throws IOException
     */
    public static void delete(String path) throws IOException {
        HttpURLConnection connection = null;

        if (get(BaseTest.REST_URL + "itembypath/" + path).getStatusCode() == 200) {
            try {
                if (vfsInfo == null) {
                    initVFS();
                }
                String url_decoded = vfsInfo.getUrlTemplates().get((Link.REL_ITEM_BY_PATH)).getHref();
                url_decoded = URLDecoder.decode(url_decoded, "UTF-8").replace("[path]", path);
                URL url = new URL(url_decoded);

                connection = Utils.getConnection(url);

                connection.setRequestMethod("GET");

                JsonParser parser = new JsonParser();
                parser.parse(connection.getInputStream());
                JsonValue object = parser.getJsonObject();
                String href = object.getElement("links").getElement("delete").getElement("href").getStringValue();

                deleteFolder(href);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.getInputStream().close();
                    connection.disconnect();
                }
            }
        } else {
            System.out.println("[VFS INFO]: Project or folder not found!");
        }
    }


    /**
     * check existing any project, if the projects or project exist - try delete it
     *
     * @throws IOException
     */
    public static void checkExistedProjectsAndDelete() {
        HttpURLConnection connection = null;
        StringBuilder vfsItemsResponse = new StringBuilder();
        String jsonDataWithVfsItems = null;
        String numItems = null;
        List<String> storeItems = new ArrayList<String>();
        try {
            if (vfsInfo == null) {
                getItemsFromRootVfs();
            }
            URL url = new URL(getItemsFromRootVfs());
            connection = Utils.getConnection(url);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != 200) {
                throw (new Exception("I can not get any item from current workspase"));
            }
            JsonParser parser = new JsonParser();
            while (connection.getInputStream().available() != 0) {
                vfsItemsResponse.append((char)connection.getInputStream().read());
            }

            jsonDataWithVfsItems = vfsItemsResponse.toString();
            Iterator<JsonValue> iter =
                    org.exoplatform.ide.commons.JsonHelper.parseJson(jsonDataWithVfsItems).getElement("items").getElements();
            if (org.exoplatform.ide.commons.JsonHelper.parseJson(jsonDataWithVfsItems).getElement("numItems").getIntValue() > 0) {
                while (iter.hasNext()) {
                    storeItems.add(iter.next().getElement("name").toString());
                }
            }
            for (String storeItem : storeItems) {
                System.out
                      .println("Next project has not been deleted: " + storeItem + "Deleting in the @AfterClass method in Base class");
                VirtualFileSystemUtils.delete(storeItem.replaceAll("\"", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connection.disconnect();
            }
        }
    }

    /**
     * get projects href for projects on current workspace
     *
     * @return
     * @throws IOException
     */
    private static String getItemsFromRootVfs() throws IOException {
        String uRl = BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/vfs/v2";
        String itemsFromRootLink = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(uRl);
            connection = Utils.getConnection(url);
            connection.setRequestMethod("GET");
            JsonParser parser = new JsonParser();

            parser.parse(connection.getInputStream());

            connection.getInputStream().close();
            vfsInfo = ObjectBuilder.createObject(VirtualFileSystemInfoImpl.class, parser.getJsonObject());

            JsonValue element = parser.getJsonObject().getElement("root").getElement("links");
            Field field = VirtualFileSystemUtils.class.getDeclaredField("rootLinks");
            rootLinks = ObjectBuilder.createObject(Map.class, (ParameterizedType)field.getGenericType(), element);
            itemsFromRootLink = rootLinks.get("children").getHref();

        } catch (JsonException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        System.out.println("getted-link---:" + itemsFromRootLink);
        return itemsFromRootLink;
    }

    /**
     * @param storageUrl
     * @return HTTPStatus code
     * @throws IOException
     */
    public static Response get(String storageUrl) throws IOException {
        URL url = new URL(storageUrl);
        HttpURLConnection connection = null;
        int status = -1;
        try {
            connection = Utils.getConnection(url);
            System.out.println(url);
            connection.setRequestMethod("GET");
            status = connection.getResponseCode();
            InputStream in = connection.getInputStream();
            int lenght = connection.getContentLength();
            try {
                byte[] data = readBody(in, lenght);
                return new Response(status, new String(data));
            } finally {
                in.close();
            }
        } catch (Exception e) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new Response(status, null);
    }

    /**
     * @param storageUrl
     * @return File content
     * @throws IOException
     */
    public static String getContent(String storageUrl) throws IOException {
        URL url = new URL(storageUrl);
        HttpURLConnection connection = null;
        int status = -1;
        try {
            connection = Utils.getConnection(url);
            connection.setRequestMethod("GET");
            status = connection.getResponseCode();
            InputStream in = connection.getInputStream();
            int lenght = connection.getContentLength();
            try {
                byte[] data = readBody(in, lenght);
                return (new String(data).toString());
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return storageUrl;
    }

    /**
     * Delete folder by Link
     *
     * @param link
     * @return http status code
     * @throws IOException
     */
    public static int deleteFolder(Link link) throws IOException {
        if (link == null) {
            throw new IllegalArgumentException("Parameter 'link' can't be null!");
        }


        return deleteFolder(link.getHref());
    }

    /**
     * Delete folder by href
     *
     * @param href
     * @return http status code
     * @throws IOException
     */
    private static int deleteFolder(String href) throws IOException {


        int status = -1;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(href);
            connection = Utils.getConnection(url);
            connection.setRequestMethod("POST");
            status = connection.getResponseCode();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return status;
    }

    public static Map<String, Link> createDefaultProject(String name) throws IOException {
        return importZipProject(name, "src/test/resources/org/exoplatform/ide/project/default-selenium-test.zip");
    }

    /**
     * create empty exo-project in IDE
     *
     * @param name
     * @return
     * @throws IOException
     */
    public static Map<String, Link> createExoProject(String name) throws IOException {
        return importZipProject(name, "src/test/resources/org/exoplatform/ide/project/exo-project.zip");
    }

    public static int createFile(Link link, String name, String mimeType, String content) throws IOException {
        if (link == null) {
            throw new IllegalArgumentException("Parameter 'link' can't be null!");
        }
        int status = -1;
        HttpURLConnection connection = null;
        try {
            String href = URLDecoder.decode(link.getHref(), "UTF-8");
            href = href.replace("[name]", name);
            URL url = new URL(href);
            connection = Utils.getConnection(url);
            connection.setRequestMethod("POST");
            connection.setRequestProperty(HTTPHeader.CONTENT_TYPE, mimeType);
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            out.write(content.getBytes());
            out.close();
            status = connection.getResponseCode();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return status;
    }

    /**
     * Import project zip to IDE
     *
     * @param projectName
     *         name of the project
     * @param zipPath
     *         local path to project zip
     * @return map of the Link, related to created project
     * @throws IOException
     */
    public static Map<String, Link> importZipProject(String projectName, String zipPath) throws IOException {
        HttpURLConnection connection = null;
        Map<String, Link> folderLiks = createFolder(projectName);
        try {

            Link href = folderLiks.get(Link.REL_IMPORT);
            if (href == null) {
                throw new RuntimeException("Folder not created or 'import' relation not found.");
            }
            URL url = new URL(href.getHref());
            connection = Utils.getConnection(url);

            connection.setRequestMethod("POST");
            connection.setRequestProperty(HTTPHeader.CONTENT_TYPE, "application/zip");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();

            File f = new File(zipPath);
            FileInputStream inputStream = new FileInputStream(f);

            IOUtils.copy(inputStream, outputStream);

            inputStream.close();
            outputStream.close();
            connection.getResponseCode();
            return folderLiks;
        } catch (Exception e) {
            e.printStackTrace();
            return folderLiks;
        } catch (IllegalAccessError e) {
            e.printStackTrace();
            return folderLiks;
        }

    }

    /**
     * Create folder in root.
     *
     * @param name
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Link> createFolder(String name) {
        HttpURLConnection connection = null;
        try {
            if (rootLinks == null) {
                initVFS();
            }
            String href = rootLinks.get(Link.REL_CREATE_FOLDER).getHref();

            href = URLDecoder.decode(href, "UTF-8").replace("[name]", name);

            URL url = new URL(href);

            connection = Utils.getConnection(url);
            connection.setRequestMethod("POST");

            JsonParser parser = new JsonParser();
            parser.parse(connection.getInputStream());
            connection.getInputStream().close();
            Field field = VirtualFileSystemUtils.class.getDeclaredField("rootLinks");

            return (Map<String, Link>)ObjectBuilder.createObject(Map.class, (ParameterizedType)field.getGenericType(),
                                                                 parser.getJsonObject().getElement("links"));
        } catch (JsonException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static void createFolder(Link link, String name) throws IOException {
        HttpURLConnection connection = null;
        try {
            String href = URLDecoder.decode(link.getHref(), "UTF-8").replace("[name]", name);
            URL url = new URL(href);
            connection = Utils.getConnection(url);
            connection.setRequestMethod("POST");
            JsonParser parser = new JsonParser();
            parser.parse(connection.getInputStream());
            connection.getInputStream().close();
        } catch (JsonException e) {
        } catch (SecurityException e) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    private static void initVFS() throws IOException {
        String uRl = BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/vfs/v2";

        HttpURLConnection connection = null;
        try {
            URL url = new URL(uRl);
            connection = Utils.getConnection(url);
            connection.setRequestMethod("GET");
            JsonParser parser = new JsonParser();

            parser.parse(connection.getInputStream());

            connection.getInputStream().close();
            vfsInfo = ObjectBuilder.createObject(VirtualFileSystemInfoImpl.class, parser.getJsonObject());

            JsonValue element = parser.getJsonObject().getElement("root").getElement("links");
            Field field = VirtualFileSystemUtils.class.getDeclaredField("rootLinks");
            rootLinks = ObjectBuilder.createObject(Map.class, (ParameterizedType)field.getGenericType(), element);

        } catch (JsonException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    /**
     * Create file with local file content
     *
     * @param link
     * @param name
     * @param mimeType
     * @param filePath
     * @throws IOException
     */
    public static void createFileFromLocal(Link link, String name, String mimeType, String filePath)
            throws IOException {
        createFile(link, name, mimeType, Utils.readFileAsString(filePath));
    }

    private static byte[] readBody(InputStream input, int contentLength) throws IOException {
        if (contentLength > 0) {
            byte[] b = new byte[contentLength];
            for (int point = -1, off = 0; (point = input.read(b, off, contentLength - off)) > 0; off += point) //
            {
                ;
            }
            return b;
        } else if (contentLength < 0) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int point = -1;
            while ((point = input.read(buf)) != -1) {
                bout.write(buf, 0, point);
            }
            return bout.toByteArray();
        }
        return new byte[0];
    }
}