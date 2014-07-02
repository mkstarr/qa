package com.codenvy.ide.paas;


import com.codenvy.ide.core.exceptions.PaaSException;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Status {
    private boolean isSuccess;
    private Throwable exception;

    private String screenshotName;


    
    public Status() {
        init();
    }
    
    public Status init() {
        isSuccess = true;
        exception = null;
        screenshotName = null;
        
        return this;
    }
    
    public boolean isSuccess() {
        return isSuccess == true;
    }

    public void setSuccess() {
        this.isSuccess = true;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    
    public boolean isFail() {
        return isSuccess != true;
    }
    
    public void setFail() {
        this.isSuccess = false;
    }
    
    public void setException(Throwable exception) {
        this.exception = exception;
    }
    
    public void setScreenshotName(String screenshotName) {
        this.screenshotName = screenshotName;
    }
    
    private String getScreenshotName() {
        return this.screenshotName;
    }
    
    public String getExceptionInfo() {       
        if (exception == null) {
            return null;
        }
        
        return String.format("%s: %s \n%s.\n(see follow screenshot for details: %s)\n\n", 
                             exception.getClass().getName(), 
                             exception.getMessage(),
                             Arrays.toString(exception.getStackTrace()).replace(", ", ",\n   "),
                             getScreenshotName());
    }
    

    /** 
     * @return error message of PaaSException
     */
    public String getPaaSErrorMessage() {
        if (exception == null 
            || ! (exception instanceof PaaSException)) {
            return null;
        }
        
        return exception.getMessage();
    }

    /**
     * Verifications
     */

    public void checkFail() {
        assertTrue(isFail());
    }

    public void checkSuccess() {
        if (! isSuccess()) {
            String errorMessageString = getExceptionInfo() != null ? 
                (".\n>>>[Error Info]: " + getExceptionInfo() + ".") : ".";
                
            fail(String.format("There was fail at %s%s", com.codenvy.ide.Utils.getCurrentTimeForLog(), errorMessageString));
        }
    }
    
    public void checkPaaSErrorMessage(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, getPaaSErrorMessage());
    }
    
}
