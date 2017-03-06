/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.security;

import at.technikum.mic16.prj.entity.UserRole;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author leandros
 */
@Named(value = "sessionInformationController")
@SessionScoped
public class SessionInformationController implements Serializable {
    // Context of user session
    private ExternalContext externalContext =
            FacesContext.getCurrentInstance().getExternalContext();
    

    private void updateExternalContext() {
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public boolean isLoggedIn() {
        updateExternalContext();
        return (externalContext.getUserPrincipal() != null);
    }

    public String currentUserName() {
        updateExternalContext();
        if (isLoggedIn()) {
            return externalContext.getUserPrincipal().getName();
        }
        return null;    
    }
    
    public boolean isInRole(String roleName) {
        updateExternalContext();
        return externalContext.isUserInRole(roleName);
    }

    public boolean isCustomer() {
        updateExternalContext();
        return isInRole(UserRole.Role.CUSTOMER.name());
    }

    public String logout() {
        // Invalidate Session
        externalContext.invalidateSession();
        // Redirect browser
        return "index.xhtml?faces-redirect=true";
    }

}
