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
import org.lestr.astenn.configuration.IPermissionsManager;
import org.lestr.astenn.plugin.IEquivalentPluginInterfaceAdapter;
import org.lestr.astenn.plugin.IPersistenceDriver;
import org.lestr.astenn.plugin.IPluginsProvider;

/**
 *
 * @author pibonnin
 */
class PermissionsManagerImpl implements IPermissionsManager {


    private Collection<IPermissionsManagerListener> listeners = new ArrayList<IPermissionsManagerListener>();


    private Collection<Class<?>[]> exposedLocalPlugins = new ArrayList<Class<?>[]>();


    private boolean autoExposeLocalPlugins = false;


    @Override
    public Collection<IPermissionsManagerListener> getListeners() {

        return listeners;

    }// END Method getListeners


    @Override
    public void setAutoExposeLocalPlugins(boolean autoExpose) {

        if (autoExposeLocalPlugins != autoExpose) {

            autoExposeLocalPlugins = autoExpose;

            rescanAutoExposedLocalPlugins();

        }

    }// END Method setAutoExposeLocalPlugins


    @Override
    public boolean isAutoExposeLocalPlugins() {

        return autoExposeLocalPlugins;

    }// END Method isAutoExposeLocalPlugins


    @Override
    public void rescanAutoExposedLocalPlugins() {


        IPersistenceDriver persistenceDriver = PluginsManager.getSingleton().getConfiguration().getPersistenceDriver();

        if (persistenceDriver != null) {

            Collection<Class<?>[]> actuallyExposedLocalPlugins = new ArrayList<Class<?>[]>();
            for (Class<?>[] plugin : getExposedLocalPlugins())
                actuallyExposedLocalPlugins.add(plugin);

            for (String pluginInterfaceName : persistenceDriver.getPluginInterfacesNames())
                if (!pluginInterfaceName.equals(IPluginsProvider.class.getName())
                    && !pluginInterfaceName.equals(IEquivalentPluginInterfaceAdapter.class.getName()))
                    for (String pluginImplementationAddress : persistenceDriver.getPluginImplementationsAddresses(pluginInterfaceName))
                        if (pluginImplementationAddress.startsWith("local:"))
                            try {

                                Class<?> pluginInterface = this.getClass().getClassLoader().loadClass(pluginInterfaceName);
                                Class<?> pluginImplementation = this.getClass().getClassLoader().loadClass(pluginImplementationAddress.substring("local:".length()));

                                boolean founded = false;
                                for (Class<?>[] plugin : getExposedLocalPlugins())
                                    if (plugin[0] == pluginInterface && plugin[1] == pluginImplementation) {
                                        founded = true;
                                        break;
                                    }

                                if (!founded)
                                    exposeLocalPlugin(pluginInterface, pluginImplementation);

                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(PermissionsManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }

        }

    }// END Method rescanAutoExposedLocalPlugins


    @Override
    public void exposeLocalPlugin(Class<?> localPluginInterface,
                                  Class<?> localPluginImplementation) {

        for (Class<?>[] plugin : exposedLocalPlugins)
            if (plugin.length == 2
                && plugin[0] == localPluginInterface
                && plugin[1] == localPluginImplementation)
                return;

        exposedLocalPlugins.add(new Class<?>[]{localPluginInterface, localPluginImplementation});

        for (IPermissionsManagerListener listener : new ArrayList<IPermissionsManagerListener>(listeners))
            listener.localPluginExposed(localPluginInterface, localPluginImplementation);

    }// END Method exposeLocalPlugin


    @Override
    public void unexposeLocalPlugin(Class<?> localPluginInterface,
                                    Class<?> localPluginImplementation) {

        for (Class<?>[] plugin : exposedLocalPlugins)
            if (plugin.length == 2
                && plugin[0] == localPluginInterface
                && plugin[1] == localPluginImplementation) {

                for (IPermissionsManagerListener listener : new ArrayList<IPermissionsManagerListener>(listeners))
                    listener.localPluginUnexposing(localPluginInterface, localPluginImplementation);

                exposedLocalPlugins.remove(plugin);

                break;

            }

    }// END Method unexposeLocalPlugin


    @Override
    public Iterable<Class<?>[]> getExposedLocalPlugins() {

        return exposedLocalPlugins;

    }// END Method getExposedLocalPlugins


}// FIN Classe PermissionsManagerImpl


