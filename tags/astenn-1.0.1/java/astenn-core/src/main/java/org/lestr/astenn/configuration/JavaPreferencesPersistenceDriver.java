/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.configuration;

import org.lestr.astenn.plugin.IPersistenceDriver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author PIBONNIN
 */
public class JavaPreferencesPersistenceDriver implements IPersistenceDriver {


    private String applicationName;


    public JavaPreferencesPersistenceDriver(){

        applicationName = "astenn.default";
    
    }// END Constructor


    public JavaPreferencesPersistenceDriver(String applicationName){

        this.applicationName = applicationName;

    }// END Constructor


    /**
     * @return the applicationName
     */
    public String getApplicationName() {

        return applicationName;

    }// END Method getApplicationName


    /**
     * @param applicationName the applicationName to set
     */
    public void setApplicationName(String applicationName) {

        this.applicationName = applicationName;

    }// END Method setApplicationName


    @Override
    public void addPluginInterface(String pluginInterfaceName) {

        Preferences.userRoot().node(applicationName).node(pluginInterfaceName);

    }// END Method addPluginInterface


    @Override
    public void removePluginInterface(String pluginInterfaceName) {
        
        try {
            Preferences.userRoot().node(applicationName).node(pluginInterfaceName).removeNode();
        } catch (BackingStoreException ex) {
            Logger.getLogger(JavaPreferencesPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// END Method removePluginInterface


    @Override
    public boolean existPluginInterface(String pluginInterfaceName) {

        try {

            for (String currentPluginInterfaceName : Preferences.userRoot().node(applicationName).childrenNames())
                if (currentPluginInterfaceName.equals(pluginInterfaceName))
                    return true;

        } catch (BackingStoreException ex) {
            Logger.getLogger(JavaPreferencesPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }// END Method existPluginInterface


    @Override
    public void addPluginImplementation(String pluginInterfaceName, String pluginImplementationAddress) {

        Preferences implementations = Preferences.userRoot().node(applicationName).node(pluginInterfaceName);
        implementations.node(UUID.randomUUID().toString()).put("value", pluginImplementationAddress);

    }// END Method addPluginImplementation


    @Override
    public void removePluginImplementation(String pluginInterfaceName, String pluginImplementationAddress) {

        try {

            Preferences implementations = Preferences.userRoot().node(applicationName).node(pluginInterfaceName);

            for (String currentPluginImplementationId : implementations.childrenNames())
                if (implementations.node(currentPluginImplementationId).get("value", "local:").equals(pluginImplementationAddress)) {
                    implementations.node(currentPluginImplementationId).removeNode();
                    break;
                }

        } catch (BackingStoreException ex) {
            Logger.getLogger(JavaPreferencesPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// END Method removePluginImplementation


    @Override
    public boolean existPluginImplementation(String pluginInterfaceName, String pluginImplementationAddress) {

        if(!existPluginInterface(pluginInterfaceName))
            return false;

        try {

            Preferences implementations = Preferences.userRoot().node(applicationName).node(pluginInterfaceName);

            for (String currentPluginImplementationId : implementations.childrenNames())
                if (implementations.node(currentPluginImplementationId).get("value", "local:").equals(pluginImplementationAddress))
                    return true;

        } catch (BackingStoreException ex) {
            Logger.getLogger(JavaPreferencesPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        } 

        return false;

    }// END Method existPluginImplementation


    @Override
    public Iterable<String> getPluginInterfacesNames() {

        Collection<String> rslt = new ArrayList<String>();

        try {
            
            rslt.addAll(Arrays.asList(Preferences.userRoot().node(applicationName).childrenNames()));

        } catch (BackingStoreException ex) {
            Logger.getLogger(JavaPreferencesPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END method getPluginInterfacesNames


    @Override
    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        Collection<String> rslt = new ArrayList<String>();

        try {

            Preferences implementations = Preferences.userRoot().node(applicationName).node(pluginInterfaceName);

            for (String currentPluginImplementationId : implementations.childrenNames())
                rslt.add(implementations.node(currentPluginImplementationId).get("value", null));

        } catch (BackingStoreException ex) {
            Logger.getLogger(JavaPreferencesPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Method getPluginImplementationsStrings
    

}// END Class JavaPreferencesPersistenceDriver
