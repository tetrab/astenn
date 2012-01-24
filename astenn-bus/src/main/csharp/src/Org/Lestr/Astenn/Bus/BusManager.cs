using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Org.Lestr.Astenn.Plugin;
using Org.Lestr.Astenn.Bus.Impl;

namespace Org.Lestr.Astenn.Bus
{


    public class BusManager
    {


        private static BusManager singleton= new BusManager();


        public static BusManager Singleton {

            get { return singleton; }

        }// END Method Singleton


        private Dictionary<string, IServer> servers;


        private BusManager() {

            servers = new Dictionary<string, IServer>();

        }// END Constructor


        public IEnumerable<string> ConnectedBusIds
        {

            get { return servers.Keys; }

        }// END Property getConnectedBusIds


        public IServer GetBusEndpointServer(string busId) {

            return servers[busId];

        }// END Method getBusEndpointServer


        public void OpenLocalBusEndpoint(string busId,
                                         IServer server) {

            servers[busId] = server;

            bool isExposed = false;
            foreach (Type[] plugin in PluginsManager.Singleton.Configuration.PermissionsManager.ExposedLocalPlugins)
                if (plugin[0].AssemblyQualifiedName.Equals(typeof(IBusEndpoint).AssemblyQualifiedName)
                    && plugin[1].AssemblyQualifiedName.Equals(typeof(DefaultBusEndpoint).AssemblyQualifiedName))
                    isExposed = true;

            if (!isExposed)
                PluginsManager.Singleton.Configuration.PermissionsManager.ExposeLocalPlugin(typeof(IBusEndpoint), typeof(DefaultBusEndpoint));

        }// END Method openLocalBusEndpoint


        public void ConnectToRemoteBusEndpoint(string busId,
                                               string busEndpointPluginAddress) {

            // Init IEnumerable for remember remote bus endpoints registered
            List<string> busEndpointsAlreadyRegisteredIds = new List<string>();
            List<string> busEndpointsAlreadyRegisteredAddresses = new List<string>();

            // Détermine l'adresse distante du point d'accès local du bus
            string localBusEndpointRemoteAddress = GetBusEndpointServer(busId).GetPluginRemoteAddress(typeof(IBusEndpoint), typeof(DefaultBusEndpoint));

            // Register the bus endpoint given by the user
            PluginsManager.Singleton.RegisterPlugin(typeof(IBusEndpoint), busEndpointPluginAddress);
            busEndpointsAlreadyRegisteredAddresses.Add(busEndpointPluginAddress);
            busEndpointsAlreadyRegisteredAddresses.Add(localBusEndpointRemoteAddress);
            bool modified = true;

            // While remote bus endpoints are founded
            while (modified) {

                modified = false;

                // Parcours les terminaisons non-locales du bus
                foreach (IBusEndpoint busEndpoint in PluginsManager.Singleton.GetRegisteredRemotePlugins<IBusEndpoint>())
                    try {
                        // If a not processed remote bus endpoint is founded
                        if (!busEndpointsAlreadyRegisteredIds.Contains(busEndpoint.GetEndpointId(busId))) {

                            // Give to the remote bus endpoint the local bus endpoint remote address
                            busEndpoint.DeclareBusEndpointAddress(busId, localBusEndpointRemoteAddress);

                            // Remember that the current remote bus endpoint has been processed
                            busEndpointsAlreadyRegisteredIds.Add(busEndpoint.GetEndpointId(busId));

                            // Iterate remote bus endpoint know by the current remote bus endpoint
                            foreach (string otherBusEndpointAddress in busEndpoint.GetKnowBusEndpointsAddresses(busId))
                                // If the new current remote bus endpoint has not been already processed
                                if (!busEndpointsAlreadyRegisteredAddresses.Contains(otherBusEndpointAddress)) {

                                    // Test it
                                    bool ok = false;

                                    foreach (IPluginsProvider pluginsProvider in PluginsManager.Singleton.GetRegisteredPlugins<IPluginsProvider>())
                                        if (otherBusEndpointAddress.StartsWith(pluginsProvider.Scheme + ":"))
                                            try {
                                                pluginsProvider.GetPlugin<IBusEndpoint>(otherBusEndpointAddress).GetEndpointId(busId);
                                                ok = true;
                                                break;
                                            } catch (Exception) {
                                                break;
                                            }

                                    if (ok) {

                                        // Register it
                                        PluginsManager.Singleton.RegisterPlugin(typeof(IBusEndpoint), otherBusEndpointAddress);

                                        // Remember that it has been already registered
                                        busEndpointsAlreadyRegisteredAddresses.Add(otherBusEndpointAddress);
                                        modified = true;

                                    }

                                }

                        }

                    } catch (Exception) {
                    }

            }

        }// END Method connectToRemoteBusEndpoint


    }// END Class BusManager


}// END Namespace Org.Lestr.Astenn.Bus
