package com.certh.iti.cloud4all.restful;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author nkak
 */
public class SST_Manager 
{
    public static final boolean RUN_INTEGRATION_TESTS_ON_BUILD = true;
    
    public String APACHE_SERVER_IP;
    public String HTDOCS_DIR;
    public String DIR_IN_HTDOCS;
    public int DELETE_FILES_OLDER_THAN_THESE_MINUTES;
    public String JAVA_SRC_PATH;
    public String X_MASHAPE_AUTHORIZATION_KEY;
    public String EYEFACE_EMAIL;
    public String EYEFACE_PASSWORD;
    public boolean USE_TIMESTAMP_IN_OUTPUT_FILENAMES;
    
    public Map HTTPStatusCodes;
    
    public int URLValidationLimitPerPage;
    public int URLValidationAlreadyPerformed; //we need this counter because in a page where many validations are needed
                                              //this may result to a slow response of the SST
                                              //thus, we put a limit to the valitations performed to each page
                                              //to -URLValidationLimitPerPage- validations per page
    
    private static SST_Manager instance = null;
    
    private SST_Manager() 
    {
        URLValidationLimitPerPage = 50;
        URLValidationAlreadyPerformed = 0;
        
        //read properties file
        Properties prop = new Properties();
	InputStream input = null;
        
 	try 
        {
            input = new FileInputStream(System.getProperty("user.dir") + "/../webapps/CLOUD4All_SST_Restful_WS/WEB-INF/config.properties");
            prop.load(input);
            APACHE_SERVER_IP = prop.getProperty("ip");
            HTDOCS_DIR = prop.getProperty("htdocsDir");
            int htdocsIndex = HTDOCS_DIR.indexOf("htdocs/");            
            if(htdocsIndex != -1)
                DIR_IN_HTDOCS = HTDOCS_DIR.substring(htdocsIndex + 7);
            DELETE_FILES_OLDER_THAN_THESE_MINUTES = Integer.parseInt(prop.getProperty("deleteFilesOlderThanTheseMinutes"));
            JAVA_SRC_PATH = prop.getProperty("javaSrcPath");
            X_MASHAPE_AUTHORIZATION_KEY = prop.getProperty("xMashapeAuthorizationKey");   
            EYEFACE_EMAIL = prop.getProperty("eyefaceEmail");
            EYEFACE_PASSWORD = prop.getProperty("eyefacePassword");
            USE_TIMESTAMP_IN_OUTPUT_FILENAMES = Boolean.parseBoolean(prop.getProperty("useTimestampInOutputFilenames"));
 	} 
        catch (IOException ex) 
        {
            //System.out.println("\n\n*\n* Config not found...This is probably an integration test!\n*");
            try
            {
                input = new FileInputStream(System.getProperty("user.dir") + "/src/main/webapp/WEB-INF/config.properties");
                prop.load(input);
                APACHE_SERVER_IP = prop.getProperty("ip");
                HTDOCS_DIR = prop.getProperty("htdocsDir");
                int htdocsIndex = HTDOCS_DIR.indexOf("htdocs/");            
                if(htdocsIndex != -1)
                    DIR_IN_HTDOCS = HTDOCS_DIR.substring(htdocsIndex + 7);
                DELETE_FILES_OLDER_THAN_THESE_MINUTES = Integer.parseInt(prop.getProperty("deleteFilesOlderThanTheseMinutes"));
                JAVA_SRC_PATH = prop.getProperty("javaSrcPath");
                X_MASHAPE_AUTHORIZATION_KEY = prop.getProperty("xMashapeAuthorizationKey");
                EYEFACE_EMAIL = prop.getProperty("eyefaceEmail");
                EYEFACE_PASSWORD = prop.getProperty("eyefacePassword");
                USE_TIMESTAMP_IN_OUTPUT_FILENAMES = Boolean.parseBoolean(prop.getProperty("useTimestampInOutputFilenames"));
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
	} 
        finally {
            if (input != null) {
		try {
                    input.close();
		} catch (IOException e) {
                    e.printStackTrace();
		}
            }
	}  
        //-read properties file
        
        //HTTP codes
        HTTPStatusCodes = new Hashtable();
        HTTPStatusCodes.put(HttpURLConnection.HTTP_ACCEPTED, "Accepted");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_BAD_GATEWAY, "Bad Gateway");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_BAD_METHOD, "Method Not Allowed");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_CLIENT_TIMEOUT, "Request Time-Out");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_CONFLICT, "Conflict");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_CREATED, "Created");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_ENTITY_TOO_LARGE, "Request Entity Too Large");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "Gateway Timeout");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_GONE, "Gone");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_LENGTH_REQUIRED, "Length Required");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_MOVED_PERM, "Moved Permanently");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_MOVED_TEMP, "Temporary Redirect");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_MULT_CHOICE, "Multiple Choices");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_NO_CONTENT, "No Content");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "Not Acceptable");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_NOT_AUTHORITATIVE, "Non-Authoritative Information");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_NOT_IMPLEMENTED, "Not Implemented");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_NOT_MODIFIED, "Not Modified");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_OK, "OK");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_PARTIAL, "Partial Content");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "Payment Required");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_PRECON_FAILED, "Precondition Failed");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_PROXY_AUTH, "Proxy Authentication Required");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_REQ_TOO_LONG, "Request-URI Too Large");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_RESET, "Reset Content");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_SEE_OTHER, "See Other");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_UNAVAILABLE, "Service Unavailable");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_UNSUPPORTED_TYPE, "Unsupported Media Type");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_USE_PROXY, "Use Proxy");
        HTTPStatusCodes.put(HttpURLConnection.HTTP_VERSION, "HTTP Version Not Supported");
    }
    
    public static SST_Manager getInstance() 
    {
        if(instance == null) 
            instance = new SST_Manager();
        return instance;
    }
    
    
    
}
