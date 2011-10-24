/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.configuration;

import java.util.Map;

/**
 *
 * @author PIBONNIN
 */
public interface IConfiguration {


    public String getAstennInstanceId();


    public CompositePersistenceDriver getPersistenceDriver();


    public IPermissionsManager getPermissionsManager();


    public Map<String, Object> getProperties();


    public Map<String, Object> getCurrentThreadSpecificsProperties();


}// END Interface IConfiguration


