/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.configuration;

import org.lestr.astenn.plugin.IPersistenceDriver;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author PIBONNIN
 */
public class RAMPersistenceDriver implements IPersistenceDriver {


    private HashMap<String, ArrayList<String>> plugins;


    public RAMPersistenceDriver()
    {

        plugins = new HashMap<String, ArrayList<String>>();

    }// END Constructor


    @Override
    public void addPluginInterface(String pluginInterfaceName) {

        plugins.put(pluginInterfaceName, new ArrayList<String>());

    }// END Method addPluginInterface


    @Override
    public void removePluginInterface(String pluginInterfaceName) {

        plugins.remove(pluginInterfaceName);

    }// END Method removePluginInterface


    @Override
    public boolean existPluginInterface(String pluginInterfaceName) {

        return plugins.containsKey(pluginInterfaceName);

    }// END Method existPluginInterface


    @Override
    public void addPluginImplementation(String pluginInterfaceName, String pluginImplementationAddress) {

        plugins.get(pluginInterfaceName).add(pluginImplementationAddress);

    }// END Method addPluginImplementation


    @Override
    public void removePluginImplementation(String pluginInterfaceName, String pluginImplementationAddress) {

        plugins.get(pluginInterfaceName).remove(pluginImplementationAddress);

    }// END Method removePluginImplementation


    @Override
    public boolean existPluginImplementation(String pluginInterfaceName, String pluginImplementationAddress) {

        return existPluginInterface(pluginInterfaceName) && plugins.get(pluginInterfaceName).contains(pluginImplementationAddress);

    }// END Method existPluginImplementation


    @Override
    public Iterable<String> getPluginInterfacesNames() {
        
        return plugins.keySet();

    }// END Method getPluginInterfacesNames


    @Override
    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        return plugins.get(pluginInterfaceName);

    }// END Method getPluginImplementationsStrings


}// END Class RAMPersistenceDriver
