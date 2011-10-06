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
 * @author pibonnin
 */
public class CachePersistenceDriver implements IPersistenceDriver {


    private HashMap<String, ArrayList<String>> plugins;


    private IPersistenceDriver cachedPersistenceDriver;


    public CachePersistenceDriver() {

        plugins = new HashMap<String, ArrayList<String>>();
        cachedPersistenceDriver = null;

    }// END Constructor


    public CachePersistenceDriver(IPersistenceDriver cachedPersistenceDriver) {

        this();

        setCachedPersistenceDriver(cachedPersistenceDriver);

    }// END Constructor


    /**
     * @return the cachedPersistenceDriver
     */
    public IPersistenceDriver getCachedPersistenceDriver() {

        return cachedPersistenceDriver;

    }// END Method getCachedPersistenceDriver


    /**
     * @param cachedPersistenceDriver the cachedPersistenceDriver to set
     */
    public void setCachedPersistenceDriver(IPersistenceDriver cachedPersistenceDriver) {

        this.cachedPersistenceDriver = cachedPersistenceDriver;

        if (cachedPersistenceDriver != null)
            reloadCache();

    }// END Method setCachedPersistenceDriver


    public void reloadCache() {

        plugins = new HashMap<String, ArrayList<String>>();

        for (String pluginInterfaceName : getCachedPersistenceDriver().getPluginInterfacesNames()) {

            plugins.put(pluginInterfaceName, new ArrayList<String>());

            for (String pluginImplementationString : getCachedPersistenceDriver().getPluginImplementationsAddresses(pluginInterfaceName))
                plugins.get(pluginInterfaceName).add(pluginImplementationString);

        }

    }// END Method ReloadCache


    @Override
    public void addPluginInterface(String pluginInterfaceName) {

        if (getCachedPersistenceDriver() != null) {

            getCachedPersistenceDriver().addPluginInterface(pluginInterfaceName);
            plugins.put(pluginInterfaceName, new ArrayList<String>());

        }

    }// END Method AddPluginInterface


    @Override
    public void removePluginInterface(String pluginInterfaceName) {

        if (getCachedPersistenceDriver() != null) {

            getCachedPersistenceDriver().removePluginInterface(pluginInterfaceName);
            plugins.remove(pluginInterfaceName);

        }

    }// END Method RemovePluginInterface


    @Override
    public boolean existPluginInterface(String pluginInterfaceName) {

        return plugins.containsKey(pluginInterfaceName);

    }// END Method ExistPluginInterface


    @Override
    public void addPluginImplementation(String pluginInterfaceName,
                                        String pluginImplementationAddress) {

        if (getCachedPersistenceDriver() != null) {

            getCachedPersistenceDriver().addPluginImplementation(pluginInterfaceName, pluginImplementationAddress);
            plugins.get(pluginInterfaceName).add(pluginImplementationAddress);

        }

    }// END Method AddPluginImplementation


    @Override
    public void removePluginImplementation(String pluginInterfaceName,
                                           String pluginImplementationAddress) {

        if (getCachedPersistenceDriver() != null) {

            getCachedPersistenceDriver().removePluginImplementation(pluginInterfaceName, pluginImplementationAddress);
            plugins.get(pluginInterfaceName).remove(pluginImplementationAddress);

        }

    }// END Method RemovePluginImplementation


    @Override
    public boolean existPluginImplementation(String pluginInterfaceName,
                                             String pluginImplementationAddress) {

        return existPluginInterface(pluginInterfaceName) && plugins.get(pluginInterfaceName).contains(pluginImplementationAddress);

    }// END Method ExistPluginImplementation


    @Override
    public Iterable<String> getPluginInterfacesNames() {

        return plugins.keySet();

    }// END Method GetPluginImplementationsNames


    @Override
    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        return plugins.get(pluginInterfaceName);

    }// END Method GetPluginImplementationsNames


}// END Class CachePersistenceDriver


