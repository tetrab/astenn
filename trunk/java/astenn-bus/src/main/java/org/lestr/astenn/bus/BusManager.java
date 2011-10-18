/*
 *  Copyright 2011 pibonnin.
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
package org.lestr.astenn.bus;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.astenn.bus.impl.IBusEndpoint;
import org.lestr.astenn.bus.impl.DefaultBusEndpoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.plugin.IPluginsProvider;
import org.lestr.astenn.plugin.IServer;

/**
 *
 * @author pibonnin
 */
public class BusManager {


    private static BusManager singleton;


    public static synchronized BusManager getSingleton() {

        return singleton == null ? (singleton = new BusManager()) : singleton;

    }// END Method getSingleton


    private Map<String, IServer> servers;


    private BusManager() {

        servers = new HashMap<String, IServer>();

    }// END Constructor


    public IServer getBusEndpointServer(String busId) {

        return servers.get(busId);

    }// END Method getBusEndpointServer


    public void openLocalBusEndpoint(String busId,
                                     IServer server) {

        servers.put(busId, server);

        boolean isExposed = false;
        for (Class<?>[] plugin : PluginsManager.getSingleton().getConfiguration().getPermissionsManager().getExposedLocalPlugins())
            if (plugin[0].getName().equals(IBusEndpoint.class.getName())
                && plugin[1].getName().equals(DefaultBusEndpoint.class.getName()))
                isExposed = true;

        if (!isExposed)
            PluginsManager.getSingleton().getConfiguration().getPermissionsManager().exposeLocalPlugin(IBusEndpoint.class, DefaultBusEndpoint.class);

    }// END Method openLocalBusEndpoint


    public void connectToRemoteBusEndpoint(String busId,
                                           String busEndpointPluginAddress) {

        // Init collection for remember remote bus endpoints registered
        Collection<String> busEndpointsAlreadyRegisteredIds = new ArrayList<String>();
        Collection<String> busEndpointsAlreadyRegisteredAddresses = new ArrayList<String>();

        // Détermine l'adresse distante du point d'accès local du bus
        String localBusEndpointRemoteAddress = getBusEndpointServer(busId).getPluginRemoteAddress(IBusEndpoint.class, DefaultBusEndpoint.class);

        // Register the bus endpoint given by the user
        PluginsManager.getSingleton().registerPlugin(IBusEndpoint.class, busEndpointPluginAddress);
        busEndpointsAlreadyRegisteredAddresses.add(busEndpointPluginAddress);
        busEndpointsAlreadyRegisteredAddresses.add(localBusEndpointRemoteAddress);
        boolean modified = true;

        // While remote bus endpoints are founded
        while (modified) {

            modified = false;

            // Parcours les terminaisons non-locales du bus
            for (IBusEndpoint busEndpoint : PluginsManager.getSingleton().getRegisteredRemotePlugins(IBusEndpoint.class))
                try {
                    // If a not processed remote bus endpoint is founded
                    if (!busEndpointsAlreadyRegisteredIds.contains(busEndpoint.getEndpointId(busId))) {

                        // Give to the remote bus endpoint the local bus endpoint remote address
                        busEndpoint.declareBusEndpointAddress(busId, localBusEndpointRemoteAddress);

                        // Remember that the current remote bus endpoint has been processed
                        busEndpointsAlreadyRegisteredIds.add(busEndpoint.getEndpointId(busId));

                        // Iterate remote bus endpoint know by the current remote bus endpoint
                        String[] knowBusEndpointsAddresses = busEndpoint.getKnowBusEndpointsAddresses(busId);
                        if (knowBusEndpointsAddresses != null)
                            for (String otherBusEndpointAddress : knowBusEndpointsAddresses)
                                // If the new current remote bus endpoint has not been already processed
                                if (!busEndpointsAlreadyRegisteredAddresses.contains(otherBusEndpointAddress)) {

                                    // Test it
                                    boolean ok = false;

                                    for (IPluginsProvider pluginsProvider : PluginsManager.getSingleton().getRegisteredPlugins(IPluginsProvider.class))
                                        if (otherBusEndpointAddress.startsWith(pluginsProvider.getScheme() + ":"))
                                            try {
                                                pluginsProvider.getPlugin(IBusEndpoint.class, otherBusEndpointAddress).getEndpointId(busId);
                                                ok = true;
                                                break;
                                            } catch (Exception ex) {
                                                break;
                                            }

                                    if (ok) {

                                        // Register it
                                        PluginsManager.getSingleton().registerPlugin(IBusEndpoint.class, otherBusEndpointAddress);

                                        // Remember that it has been already registered
                                        busEndpointsAlreadyRegisteredAddresses.add(otherBusEndpointAddress);
                                        modified = true;

                                    }

                                }

                    }

                } catch (RemoteException ex) {
                    Logger.getLogger(BusManager.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

    }// END Method connectToRemoteBusEndpoint


    public Iterable<String> getConnectedBusIds() {

        return servers.keySet();

    }// END Method getConnectedBusIds


}// END Interface BusManager


