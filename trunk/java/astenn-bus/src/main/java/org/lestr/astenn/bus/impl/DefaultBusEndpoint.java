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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.bus.BusManager;
import org.lestr.astenn.plugin.IPluginsProvider;

/**
 *
 * @author PIBONNIN
 */
public class DefaultBusEndpoint extends UnicastRemoteObject implements IBusEndpoint {


    private static final Map<String, Collection<String>> knowBusEndpointsAddresses = new HashMap<String, Collection<String>>();


    private static final String endpointId = UUID.randomUUID().toString();


    public DefaultBusEndpoint() throws RemoteException {
    }


    @Override
    public String getEndpointId(String busId) {

        return endpointId + busId;

    }// END Method getEndpointId


    public String[] getPluginInterfacesNames(String busId) {

        Collection<String> rslt = new ArrayList<String>();

        synchronized (knowBusEndpointsAddresses) {

            if (((Set<String>) BusManager.getSingleton().getConnectedBusIds()).contains(busId))
                for (Class<?>[] plugin : PluginsManager.getSingleton().getConfiguration().getPermissionsManager().getExposedLocalPlugins())
                    rslt.add(plugin[0].getName());

        }

        return rslt.toArray(new String[0]);

    }// END Method getPluginInterfacesNames


    @Override
    public String[] getPluginImplementationsAddresses(String busId,
                                                      String pluginInterfaceClassName) {

        Collection<String> rslt = new ArrayList<String>();

        synchronized (knowBusEndpointsAddresses) {

            if (((Set<String>) BusManager.getSingleton().getConnectedBusIds()).contains(busId))
                for (Class<?>[] plugin : PluginsManager.getSingleton().getConfiguration().getPermissionsManager().getExposedLocalPlugins())
                    if (plugin[0].getName().equals(pluginInterfaceClassName))
                        rslt.add(BusManager.getSingleton().getBusEndpointServer(busId).getPluginRemoteAddress(plugin[0], plugin[1]));

        }

        return rslt.toArray(new String[0]);

    }// END Method getPluginImplementationsAddresses


    @Override
    public String[] getKnowBusEndpointsAddresses(String busId) {

        Collection<String> rslt = new ArrayList<String>();

        synchronized (knowBusEndpointsAddresses) {

            if (((Set<String>) BusManager.getSingleton().getConnectedBusIds()).contains(busId)) {

                if (!knowBusEndpointsAddresses.containsKey(busId))
                    knowBusEndpointsAddresses.put(busId, new ArrayList<String>());

                for (String knowBusEndpointAddress : knowBusEndpointsAddresses.get(busId))
                    for (IPluginsProvider pluginsProvider : PluginsManager.getSingleton().getRegisteredPlugins(IPluginsProvider.class))
                        if (knowBusEndpointAddress.startsWith(pluginsProvider.getScheme() + ":"))
                            try {
                                pluginsProvider.getPlugin(IBusEndpoint.class, knowBusEndpointAddress).getEndpointId(busId);
                                rslt.add(knowBusEndpointAddress);
                            } catch (Exception ex) {
                                break;
                            }

                knowBusEndpointsAddresses.put(busId, rslt);

            }

        }

        return rslt.toArray(new String[0]);

    }// END Method getKnowBusEndpointsAddresses


    @Override
    public void declareBusEndpointAddress(String busId,
                                          String address) {

        synchronized (knowBusEndpointsAddresses) {

            if (((Set<String>) BusManager.getSingleton().getConnectedBusIds()).contains(busId)) {

                if (!knowBusEndpointsAddresses.containsKey(busId))
                    knowBusEndpointsAddresses.put(busId, new ArrayList<String>());

                if (!knowBusEndpointsAddresses.get(busId).contains(address)) {
                    
                    knowBusEndpointsAddresses.get(busId).add(address);
                    PluginsManager.getSingleton().registerPlugin(IBusEndpoint.class, address);
                    
                }

            }

        }

    }// END Method declareBusEndpointAddress


}//FIN Classe IBusEndpoint


