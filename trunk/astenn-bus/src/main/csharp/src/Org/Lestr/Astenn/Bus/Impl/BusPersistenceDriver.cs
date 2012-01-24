using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.Bus.Impl
{


    public class BusPersistenceDriver : IPersistenceDriver
    {


        public void AddPluginInterface(string pluginInterfaceName) { }


        public void RemovePluginInterface(string pluginInterfaceName) { }


        public bool ExistPluginInterface(string pluginInterfaceName)
        {

            string[] pluginImplementationsAddresses;

            if (!pluginInterfaceName.Equals(typeof(IPluginsProvider).AssemblyQualifiedName)
                && !pluginInterfaceName.Equals(typeof(IBusEndpoint).AssemblyQualifiedName))
                foreach (IBusEndpoint busEndpoint in PluginsManager.Singleton.GetRegisteredRemotePlugins<IBusEndpoint>())
                    foreach (String busId in BusManager.Singleton.ConnectedBusIds)
                        try {
                            if ((pluginImplementationsAddresses = busEndpoint.GetPluginImplementationsAddresses(busId, pluginInterfaceName)) != null && pluginImplementationsAddresses.Length > 0)
                                return true;
                        } catch (Exception) {
                        }

            return false;

        }// END Method ExistPluginInterface


        public void AddPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress) { }


        public void RemovePluginImplementation(string pluginInterfaceName, string pluginImplementationAddress) { }


        public bool ExistPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            if (!pluginInterfaceName.Equals(typeof(IPluginsProvider).AssemblyQualifiedName)
                && !pluginInterfaceName.Equals(typeof(IBusEndpoint).AssemblyQualifiedName))
                foreach (IBusEndpoint busEndpoint in PluginsManager.Singleton.GetRegisteredRemotePlugins<IBusEndpoint>())
                    foreach (String busId in BusManager.Singleton.ConnectedBusIds)
                        try {
                            if (busEndpoint.GetPluginImplementationsAddresses(busId, pluginInterfaceName).Contains(pluginImplementationAddress))
                                return true;
                        } catch (Exception) {
                        }

            return false;

        }// END Method ExistPluginImplementation


        public IEnumerable<string> GetPluginInterfacesNames()
        {

            List<string> rslt = new List<string>();

            foreach (IBusEndpoint busEndpoint in PluginsManager.Singleton.GetRegisteredRemotePlugins<IBusEndpoint>())
                foreach (string busId in BusManager.Singleton.ConnectedBusIds)
                    try {
                        rslt.AddRange(busEndpoint.GetPluginInterfacesNames(busId));
                    } catch (Exception) {
                    }

            return rslt;

        }// END Method GetPluginInterfacesNames


        public IEnumerable<string> GetPluginImplementationsAddresses(string pluginInterfaceName)
        {

            List<string> rslt = new List<string>();
            
            if (!pluginInterfaceName.Equals(typeof(IPluginsProvider).AssemblyQualifiedName)
                && !pluginInterfaceName.Equals(typeof(IBusEndpoint).AssemblyQualifiedName))
                foreach (IBusEndpoint busEndpoint in PluginsManager.Singleton.GetRegisteredRemotePlugins<IBusEndpoint>())
                    foreach (String busId in BusManager.Singleton.ConnectedBusIds)
                        try {
                            rslt.AddRange(busEndpoint.GetPluginImplementationsAddresses(busId, pluginInterfaceName));
                        } catch (Exception) {
                        }

            return rslt;

        }// END Method GetPluginImplementationsAddresses


    }// END Class BusPersistenceDriver


}// END Namespace Org.Lestr.Astenn.Bus.Impl
