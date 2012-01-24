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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.astenn.plugin.IPersistenceDriver;

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

        try {
            for (Class<?> equivalentInterfaceType : PluginsManager.getSingleton().getAdvanced().getEquivalentsPluginsInterfaces(Class.forName(pluginInterfaceName)))
                rslt = rslt || existPluginInterface(equivalentInterfaceType.getName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EquivalentsPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        try {
            for (Class<?> equivalentInterfaceType : PluginsManager.getSingleton().getAdvanced().getEquivalentsPluginsInterfaces(Class.forName(pluginInterfaceName)))
                rslt = rslt || existPluginImplementation(equivalentInterfaceType.getName(), pluginImplementationAddress);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EquivalentsPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        try {
            for (Class<?> equivalentInterfaceType : PluginsManager.getSingleton().getAdvanced().getEquivalentsPluginsInterfaces(Class.forName(pluginInterfaceName)))
                for (String implementationAddress : persistenceDriver.getPluginImplementationsAddresses(equivalentInterfaceType.getName()))
                    rslt.add(implementationAddress);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EquivalentsPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Méthode getPluginImplementationsAddresses


}// END Class EquivalentsPersistenceDriver


