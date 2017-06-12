/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Facilitates posting messages to the faces context
 * @author leandros
 */
public class MessageUtil {
    
    public static void putMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(severity, summary, detail));
    }
    
    public static void putInfo(String summary, String detail) {
        putMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    }
    
    public static void putWarning(String summary, String detail) {
        putMessage(FacesMessage.SEVERITY_WARN, summary, detail);
    }
    
    public static void putError(String summary, String detail) {
        putMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    }
    
}
