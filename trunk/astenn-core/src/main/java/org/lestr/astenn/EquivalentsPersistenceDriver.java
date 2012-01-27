/*
 * Copyright 2012 pibonnin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lestr.astenn;

import java.util.ArrayList;
import java.util.Collection;
import org.lestr.astenn.plugin.IEquivalentPluginInterfaceAdapter;
import org.lestr.astenn.plugin.IPersistenceDriver;
import org.lestr.astenn.plugin.IPluginsProvider;

/**
 *
 * @author pibonnin
 */
class EquivalentsPersistenceDriver implements IPersistenceDriver {


    private IPersistenceDriver persistenceDriver;


    public EquivalentsPersistenceDriver(IPersistenceDriver pluginsProvider) {

        this.persistenceDriver = pluginsProvider;

    }// END Constructor


    @Override
    public void addPluginInterface(String pluginInterfaceName) {

        persistenceDriver.addPluginInterface(pluginInterfaceName);

    }// END Méthode addPluginInterface


    @Override
    public void removePluginInterface(String pluginInterfaceName) {

        persistenceDriver.removePluginInterface(pluginInterfaceName);

    }// END Méthode removePluginInterface


    @Override
    public boolean existPluginInterface(String pluginInterfaceName) {

        boolean rslt = persistenceDriver.existPluginInterface(pluginInterfaceName);

        if (!pluginInterfaceName.equals(IEquivalentPluginInterfaceAdapter.class.getName())
            && !pluginInterfaceName.equals(IPluginsProvider.class.getName()))
            for (IEquivalentPluginInterfaceAdapter adapter : PluginsManager.getSingleton().getRegisteredLocalPlugins(IEquivalentPluginInterfaceAdapter.class))
                if (adapter.getPluginInterface().getName().equals(pluginInterfaceName))
                    rslt = rslt || persistenceDriver.existPluginInterface(adapter.getPluginEquivalentInterface().getName());

        return rslt;

    }// END Méthode existPluginInterface


    @Override
    public void addPluginImplementation(String pluginInterfaceName,
                                        String pluginImplementationAddress) {

        persistenceDriver.addPluginImplementation(pluginInterfaceName,
                                                  pluginImplementationAddress);

    }// END Méthode addPluginImplementation


    @Override
    public void removePluginImplementation(String pluginInterfaceName,
                                           String pluginImplementationAddress) {

        persistenceDriver.removePluginImplementation(pluginInterfaceName,
                                                     pluginImplementationAddress);

    }// END Méthode removePluginImplementation


    @Override
    public boolean existPluginImplementation(String pluginInterfaceName,
                                             String pluginImplementationAddress) {

        boolean rslt = persistenceDriver.existPluginImplementation(pluginInterfaceName,
                                                                   pluginImplementationAddress);

        if (!pluginInterfaceName.equals(IEquivalentPluginInterfaceAdapter.class.getName())
            && !pluginInterfaceName.equals(IPluginsProvider.class.getName()))
            for (IEquivalentPluginInterfaceAdapter adapter : PluginsManager.getSingleton().getRegisteredLocalPlugins(IEquivalentPluginInterfaceAdapter.class))
                if (adapter.getPluginInterface().getName().equals(pluginInterfaceName))
                    rslt = rslt || persistenceDriver.existPluginImplementation(adapter.getPluginEquivalentInterface().getName(), pluginImplementationAddress);

        return rslt;

    }// END Méthode existPluginImplementation


    @Override
    public Iterable<String> getPluginInterfacesNames() {

        return persistenceDriver.getPluginInterfacesNames();

    }// END Méthode getPluginInterfacesNames


    @Override
    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        Collection<String> rslt = new ArrayList<String>();

        for (String implementationAddress : persistenceDriver.getPluginImplementationsAddresses(pluginInterfaceName))
            rslt.add(implementationAddress);

        if (!pluginInterfaceName.equals(IEquivalentPluginInterfaceAdapter.class.getName())
            && !pluginInterfaceName.equals(IPluginsProvider.class.getName()))
            for (IEquivalentPluginInterfaceAdapter adapter : PluginsManager.getSingleton().getRegisteredLocalPlugins(IEquivalentPluginInterfaceAdapter.class))
                if (adapter.getPluginInterface().getName().equals(pluginInterfaceName))
                    for (String implementationAddress : persistenceDriver.getPluginImplementationsAddresses(adapter.getPluginInterface().getName()))
                        rslt.add(implementationAddress);

        return rslt;

    }// END Method getPluginImplementationsAddresses


}// END Class EquivalentsPersistenceDriver
