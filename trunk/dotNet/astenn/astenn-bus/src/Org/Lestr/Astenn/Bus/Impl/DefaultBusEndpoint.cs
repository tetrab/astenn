using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.Bus.Impl
{


    public class DefaultBusEndpoint : MarshalByRefObject, IBusEndpoint
    {


        private static Dictionary<string, List<String>> knowBusEndpointsAddresses = new Dictionary<string,List<string>>();


        private static string endpointId = Guid.NewGuid().ToString();


        public string GetEndpointId(string busId)
        {

            return endpointId + busId;

        }// END Method GetEndpointId


        public string[] GetPluginInterfacesNames(string busId)
        {

            List<string> rslt = new List<string>();

            lock (knowBusEndpointsAddresses) {

                if (((List<string>) BusManager.Singleton.ConnectedBusIds).Contains(busId))
                    foreach (Type[] plugin in PluginsManager.Singleton.Configuration.PermissionsManager.ExposedLocalPlugins)
                        rslt.Add(plugin[0].AssemblyQualifiedName);

            }

            return rslt.ToArray();

        }// END Method GetPluginInterfacesNames


        public string[] GetPluginImplementationsAddresses(string busId, string pluginInterfaceClassName)
        {
            
            List<string> rslt = new List<string>();

            lock (knowBusEndpointsAddresses) {

                if (((List<string>)BusManager.Singleton.ConnectedBusIds).Contains(busId))
                    foreach (Type[] plugin in PluginsManager.Singleton.Configuration.PermissionsManager.ExposedLocalPlugins)
                        if (plugin[0].AssemblyQualifiedName.Equals(pluginInterfaceClassName))
                            rslt.Add(BusManager.Singleton.GetBusEndpointServer(busId).GetPluginRemoteAddress(plugin[0], plugin[1]));

            }

            return rslt.ToArray();

        }// END Method GetPluginImplementationsAddresses


        public string[] GetKnowBusEndpointsAddresses(string busId)
        {
            
            List<string> rslt = new List<string>();

            lock (knowBusEndpointsAddresses) {

                if (((List<string>) BusManager.Singleton.ConnectedBusIds).Contains(busId)) {

                    if (!knowBusEndpointsAddresses.ContainsKey(busId))
                        knowBusEndpointsAddresses[busId] = new List<string>();

                    foreach (string knowBusEndpointAddress in knowBusEndpointsAddresses[busId])
                        foreach (IPluginsProvider pluginsProvider in PluginsManager.Singleton.GetRegisteredPlugins<IPluginsProvider>())
                            if (knowBusEndpointAddress.StartsWith(pluginsProvider.Scheme + ":"))
                                try {
                                    pluginsProvider.GetPlugin<IBusEndpoint>(knowBusEndpointAddress).GetEndpointId(busId);
                                    rslt.Add(knowBusEndpointAddress);
                                } catch (Exception) {
                                    break;
                                }

                    knowBusEndpointsAddresses[busId] = rslt;

                }

            }

            return rslt.ToArray();

        }// END Method GetKnowBusEndpointsAddresses


        public void DeclareBusEndpointAddress(string busId, string address)
        {

            lock (knowBusEndpointsAddresses) {

                if (((List<string>) BusManager.Singleton.ConnectedBusIds).Contains(busId)) {

                    if (!knowBusEndpointsAddresses.ContainsKey(busId))
                        knowBusEndpointsAddresses[busId] = new List<string>();

                    if (!knowBusEndpointsAddresses[busId].Contains(address))
                        knowBusEndpointsAddresses[busId].Add(address);

                }

            }

        }// END Method DeclareBusEndpointAddress


    }// END Class DefaultBusEndpoint


}// END Namespace Org.Lestr.Astenn.Bus.Impl
