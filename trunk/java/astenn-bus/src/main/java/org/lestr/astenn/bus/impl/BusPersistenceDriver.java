/*
 *  Copyright 2011 PIBONNIN.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.lestr.astenn.bus.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.bus.BusManager;
import org.lestr.astenn.plugin.IPersistenceDriver;
import org.lestr.astenn.plugin.IPluginsProvider;

/**
 *
 * @author PIBONNIN
 */
class BusPersistenceDriver implements IPersistenceDriver {


    public void addPluginInterface(String pluginInterfaceName) {
    }// END Method addPluginInterface


    public void removePluginInterface(String pluginInterfaceName) {
    }// END Method removePluginInterface


    public boolean existPluginInterface(String pluginInterfaceName) {

        if (pluginInterfaceName.equals(IPluginsProvider.class.getName())
            || pluginInterfaceName.equals(IBusEndpoint.class.getName()))
            return false;

        Iterable<String> busEndpointsAddresses = PluginsManager.getSingleton().getConfiguration().getPersistenceDriver().getPluginImplementationsAddresses(IBusEndpoint.class.getName());

        for (String busEndpointAddress : busEndpointsAddresses)
            for (String busId : BusManager.getSingleton().getConnectedBusIds())
                try {

                    IBusEndpoint busEndpoint = getBusEndpoint(busEndpointAddress);

                    String[] pluginImplementationsAddresses;
                    if ((pluginImplementationsAddresses = busEndpoint.getPluginImplementationsAddresses(busId, pluginInterfaceName)) != null
                        && pluginImplementationsAddresses.length > 0)
                        return true;

                } catch (Exception ex) {

                    Logger.getLogger(BusPersistenceDriver.class.getName()).log(Level.FINEST, null, ex);

                    PluginsManager.getSingleton().unregisterPlugin(IBusEndpoint.class, busEndpointAddress);
                    break;

                }

        return false;

    }// END Method existPluginInterface


    public void addPluginImplementation(String pluginInterfaceName,
                                        String pluginImplementationAddress) {
    }// END Method addPluginImplementation


    public void removePluginImplementation(String pluginInterfaceName,
                                           String pluginImplementationAddress) {
    }// END Method removePluginImplementation


    public boolean existPluginImplementation(String pluginInterfaceName,
                                             String pluginImplementationAddress) {

        if (pluginInterfaceName.equals(IPluginsProvider.class.getName())
            || pluginInterfaceName.equals(IBusEndpoint.class.getName()))
            return false;

        Iterable<String> busEndpointsAddresses = PluginsManager.getSingleton().getConfiguration().getPersistenceDriver().getPluginImplementationsAddresses(IBusEndpoint.class.getName());

        for (String busEndpointAddress : busEndpointsAddresses)
            for (String busId : BusManager.getSingleton().getConnectedBusIds())
                try {

                    IBusEndpoint busEndpoint = getBusEndpoint(busEndpointAddress);

                    if (Arrays.asList(busEndpoint.getPluginImplementationsAddresses(busId, pluginInterfaceName)).contains(pluginImplementationAddress))
                        return true;

                } catch (Exception ex) {

                    Logger.getLogger(BusPersistenceDriver.class.getName()).log(Level.FINEST, null, ex);

                    PluginsManager.getSingleton().unregisterPlugin(IBusEndpoint.class, busEndpointAddress);
                    break;

                }

        return false;

    }// END Method existPluginImplementation


    public Iterable<String> getPluginInterfacesNames() {

        Collection<String> rslt = new ArrayList<String>();

        Iterable<String> busEndpointsAddresses = PluginsManager.getSingleton().getConfiguration().getPersistenceDriver().getPluginImplementationsAddresses(IBusEndpoint.class.getName());

        for (String busEndpointAddress : busEndpointsAddresses)
            for (String busId : BusManager.getSingleton().getConnectedBusIds())
                try {

                    IBusEndpoint busEndpoint = getBusEndpoint(busEndpointAddress);

                    rslt.addAll(Arrays.asList(busEndpoint.getPluginInterfacesNames(busId)));

                } catch (Exception ex) {

                    Logger.getLogger(BusPersistenceDriver.class.getName()).log(Level.FINEST, null, ex);

                    PluginsManager.getSingleton().unregisterPlugin(IBusEndpoint.class, busEndpointAddress);
                    break;

                }

        return rslt;

    }// END Method getPluginInterfacesNames


    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        Collection<String> rslt = new ArrayList<String>();

        if (pluginInterfaceName.equals(IPluginsProvider.class.getName())
            || pluginInterfaceName.equals(IBusEndpoint.class.getName()))
            return rslt;

        Iterable<String> busEndpointsAddresses = PluginsManager.getSingleton().getConfiguration().getPersistenceDriver().getPluginImplementationsAddresses(IBusEndpoint.class.getName());

        for (String busEndpointAddress : busEndpointsAddresses)
            for (String busId : BusManager.getSingleton().getConnectedBusIds())
                try {

                    IBusEndpoint busEndpoint = getBusEndpoint(busEndpointAddress);

                    rslt.addAll(Arrays.asList(busEndpoint.getPluginImplementationsAddresses(busId, pluginInterfaceName)));

                } catch (Exception ex) {

                    Logger.getLogger(BusPersistenceDriver.class.getName()).log(Level.FINEST, null, ex);

                    PluginsManager.getSingleton().unregisterPlugin(IBusEndpoint.class, busEndpointAddress);
                    break;

                }

        return rslt;

    }// END Method getPluginImplementationsAddresses


    private IBusEndpoint getBusEndpoint(String busEndpointAddress) {

        for (IPluginsProvider pluginsProvider : PluginsManager.getSingleton().getRegisteredLocalPlugins(IPluginsProvider.class))
            if (!(pluginsProvider instanceof IPluginsProvider.LocalPluginsProvider)
                && busEndpointAddress.startsWith(pluginsProvider.getScheme() + ":"))
                return pluginsProvider.getPlugin(IBusEndpoint.class, busEndpointAddress);

        return null;
        
    }// END Method getBusEndpoint


}// END Class BusPersistenceDriver


