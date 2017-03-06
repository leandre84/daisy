/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.security;

import at.technikum.mic16.prj.entity.UserRole;
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author leandros
 */
@ManagedBean
@SessionScoped
public class SessionInformationController implements Serializable {

    private String userName;
    private String password;
    // Context of user session
    private final ExternalContext externalContext = FacesContext.
            getCurrentInstance().getExternalContext();
    private final String currentUser = externalContext.getUserPrincipal().
            getName();
    private final boolean isEmployee = externalContext.isUserInRole(UserRole.Role.CUSTOMER.name());

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public Boolean getIsEmployee() {
        return isEmployee;
    }

    public String logout() {
        // Invalidate Session
        externalContext.invalidateSession();
        // Redirect browser
        return "/index.xhtml?faces-redirect=true";
    }

}
