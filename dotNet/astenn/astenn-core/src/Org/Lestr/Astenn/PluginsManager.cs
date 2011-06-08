using System;
using System.Collections.Generic;
using System.Reflection;
using System.Text;
using Org.Lestr.Astenn.Configuration;
using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn
{


    public class PluginsManager
    {


        private static object lockObject = new object();


        private static PluginsManager singleton;


        public static PluginsManager Singleton
        {

            get 
            {

                lock (lockObject)
                {

                    if (singleton == null)
                    {

                        singleton = new PluginsManager();

                        foreach (IAstennFirstUseListener listener in PluginsManager.Singleton.GetRegisteredPlugins<IAstennFirstUseListener>())
                            listener.AstennInstanceStarting();

                    }

                    return singleton; 

                }
            }

        }// END Property Singleton


        private IConfiguration configuration;


        private IPluginsProvider localPluginsProvider;


        private PluginsManager()
        {

            configuration = new ConfigurationImpl();
            localPluginsProvider = new LocalPluginsProvider();

        }// END Constructor


        public IConfiguration Configuration
        {

            get { return configuration; }

        }// END Property Configuration


        public void RegisterPlugin(Type pluginInterfaceClass, Type pluginImplementationClass)
        {

            RegisterPlugin(pluginInterfaceClass, "local:" + pluginImplementationClass.AssemblyQualifiedName);

        }// END Method RegisterPlugin


        public void RegisterPlugin(Type pluginInterfaceClass, string pluginImplementationAddress)
        {

            RegisterPlugin(pluginInterfaceClass, pluginImplementationAddress, Configuration.PersistenceDriver);

        }// END Method RegisterPlugin


        public void RegisterPlugin(Type pluginInterfaceClass, string pluginImplementationAddress, IPersistenceDriver persistenceDriver)
        {

            if (!persistenceDriver.ExistPluginInterface(pluginInterfaceClass.AssemblyQualifiedName))
                persistenceDriver.AddPluginInterface(pluginInterfaceClass.AssemblyQualifiedName);

            if (!persistenceDriver.ExistPluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, pluginImplementationAddress))
            {

                persistenceDriver.AddPluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, pluginImplementationAddress);

                if (Configuration.PermissionsManager.AutoExposeLocalPlugins)
                    Configuration.PermissionsManager.RescanAutoExposedLocalPlugins();

            }

        }// END Method RegisterPlugin


        public void RegisterSingletonPlugin(Type pluginInterfaceClass, Type pluginImplementationClass)
        {

            RegisterSingletonPlugin(pluginInterfaceClass, "local:" + pluginImplementationClass.AssemblyQualifiedName);

        }// END Method RegisterPlugin


        public void RegisterSingletonPlugin(Type pluginInterfaceClass, string pluginImplementationAddress)
        {

            UnregisterSingletonPlugin(pluginInterfaceClass);

            Configuration.PersistenceDriver.AddPluginInterface(pluginInterfaceClass.AssemblyQualifiedName);
            Configuration.PersistenceDriver.AddPluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, pluginImplementationAddress);

        }// END Method RegisterPlugin


        public void RegisterSingletonPlugin(Type pluginInterfaceClass, string pluginImplementationAddress, IPersistenceDriver persistenceDriver)
        {

            UnregisterSingletonPlugin(pluginInterfaceClass);

            RegisterPlugin(pluginInterfaceClass, pluginImplementationAddress, persistenceDriver);
            
        }// END Method RegistreSingletonPlugin


        public void UnregisterPlugin(Type pluginInterfaceClass, Type pluginImplementationClass)
        {

            UnregisterPlugin(pluginInterfaceClass, "local:" + pluginImplementationClass.AssemblyQualifiedName);

        }// END Method UnregisterPlugin


        public void UnregisterPlugin(Type pluginInterfaceClass, string pluginImplementationAddress)
        {

            UnregisterPlugin(pluginInterfaceClass, pluginImplementationAddress, Configuration.PersistenceDriver);
            
        }// END Method UnregisterPlugin


        public void UnregisterPlugin(Type pluginInterfaceClass, string pluginImplementationAddress, IPersistenceDriver persistenceDriver)
        {

            if (persistenceDriver.ExistPluginInterface(pluginInterfaceClass.AssemblyQualifiedName) &&
               persistenceDriver.ExistPluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, pluginImplementationAddress))
            {

                persistenceDriver.RemovePluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, pluginImplementationAddress);

                if (!persistenceDriver.GetPluginImplementationsAddresses(pluginInterfaceClass.AssemblyQualifiedName).GetEnumerator().MoveNext())
                    persistenceDriver.RemovePluginInterface(pluginInterfaceClass.AssemblyQualifiedName);

            }

        }// END Method UnregisterPlugin


        public void UnregisterSingletonPlugin(Type pluginInterfaceClass)
        {

            if (Configuration.PersistenceDriver.ExistPluginInterface(pluginInterfaceClass.AssemblyQualifiedName))
            {

                foreach (string implementationAddress in new List<string>(Configuration.PersistenceDriver.GetPluginImplementationsAddresses(pluginInterfaceClass.AssemblyQualifiedName)))
                    Configuration.PersistenceDriver.RemovePluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, implementationAddress);

                Configuration.PersistenceDriver.RemovePluginInterface(pluginInterfaceClass.AssemblyQualifiedName);

            }

        }// END Method UnregisterPlugin


        public IEnumerable<PluginInterfaceType> GetRegisteredPlugins<PluginInterfaceType>()
        {

            List<PluginInterfaceType> rslt = new List<PluginInterfaceType>();

            lock (this) {

                if (typeof(PluginInterfaceType) != typeof(IPluginsProvider))
                    foreach (IPluginsProvider pluginsProvider in GetRegisteredLocalPlugins<IPluginsProvider>())
                        foreach (PluginInterfaceType plugin in GetRegisteredPlugins<PluginInterfaceType>(pluginsProvider))
                            rslt.Add(plugin);

                foreach (PluginInterfaceType plugin in GetRegisteredPlugins<PluginInterfaceType>(localPluginsProvider))
                    rslt.Add(plugin);

            }

            return rslt;

        }// END Method GetRegisteredPlugins


        public IEnumerable<PluginInterfaceType> GetRegisteredPlugins<PluginInterfaceType>(IPluginsProvider pluginsProvider)
        {

            List<PluginInterfaceType> rslt = new List<PluginInterfaceType>();

            lock (this) {

                List<string> pluginImplementationAddresses = new List<string>();

                if (Configuration.PersistenceDriver.ExistPluginInterface(typeof(PluginInterfaceType).AssemblyQualifiedName))
                    foreach (String pluginImplementationAddress in Configuration.PersistenceDriver.GetPluginImplementationsAddresses(typeof(PluginInterfaceType).AssemblyQualifiedName))
                        pluginImplementationAddresses.Add(pluginImplementationAddress);

                foreach (string pluginImplementationAddress in pluginImplementationAddresses)
                    if (pluginImplementationAddress.StartsWith(pluginsProvider.Scheme + ":"))
                        rslt.Add(pluginsProvider.GetPlugin<PluginInterfaceType>(pluginImplementationAddress));

            }

            return rslt;

        }// END Method GetRegisteredPlugins


        public IEnumerable<PluginInterfaceType> GetRegisteredLocalPlugins<PluginInterfaceType>()
        {

            return GetRegisteredPlugins<PluginInterfaceType>(localPluginsProvider);

        }// END Method GetRegisteredLocalPlugins

        
        public IEnumerable<PluginInterfaceType> GetRegisteredRemotePlugins<PluginInterfaceType>() {

            List<PluginInterfaceType> rslt = new List<PluginInterfaceType>();

            if (typeof(PluginInterfaceType) != typeof(IPluginsProvider))
                foreach (IPluginsProvider pluginsProvider in GetRegisteredPlugins<IPluginsProvider>(localPluginsProvider))
                    if (!(pluginsProvider is LocalPluginsProvider))
                        foreach (PluginInterfaceType plugin in GetRegisteredPlugins<PluginInterfaceType>(pluginsProvider))
                            rslt.Add(plugin);

            return rslt;

        }// END Method GetRegisteredRemotePlugins


        public PluginInterfaceType GetRegisteredSingletonPlugin<PluginInterfaceType>()
        {

            IEnumerator<PluginInterfaceType> enumerator = GetRegisteredPlugins<PluginInterfaceType>().GetEnumerator();

            return enumerator.MoveNext() ? enumerator.Current : default(PluginInterfaceType);

        }// END Method GetRegisteredPlugins


        private class ConfigurationImpl : IConfiguration
        {


            private CompositePersistenceDriver persistenceDriver;
            IPermissionsManager permissionsManager;


            public ConfigurationImpl()
            {

                persistenceDriver = new CompositePersistenceDriver(new RAMPersistenceDriver(), new List<IPersistenceDriver>());
                permissionsManager = new PermissionsManagerImpl();

                persistenceDriver.ReadOnlyPersistenceDrivers.Add(new EmbbedXMLDocumentPersistenceDriver());

            }// END Constructor


            public CompositePersistenceDriver PersistenceDriver
            {

                get { return persistenceDriver; }

            }// END Property PersistenceDriver


            public IPermissionsManager PermissionsManager
            {

                get { return permissionsManager; }

            }// END Property PermissionsManager


        }// END Class ConfigurationImpl 


        private class PermissionsManagerImpl : IPermissionsManager 
        {


            private List<IPermissionsManagerListener> listeners;


            private List<Type[]> exposedLocalPlugins;


            private bool autoExposeLocalPlugins;


            public PermissionsManagerImpl()
            {

                listeners = new List<IPermissionsManagerListener>();
                exposedLocalPlugins = new List<Type[]>();
                autoExposeLocalPlugins = false;

            }// END Constructuor


            public bool AutoExposeLocalPlugins
            {

                get { return autoExposeLocalPlugins; }

                set
                {

                    if (autoExposeLocalPlugins != value)
                    {

                        autoExposeLocalPlugins = value;

                        RescanAutoExposedLocalPlugins();

                    }

                }

            }// END Property AutoExposeLocalPlugins


            public List<IPermissionsManagerListener> Listeners
            {

                get { return listeners; }

            }// END Property Listeners


            public IEnumerable<Type[]> ExposedLocalPlugins
            {

                get { return exposedLocalPlugins; }

            }// END Property ExposedLocalPlugins


            public void RescanAutoExposedLocalPlugins()
            {

                List<Type[]> actuallyExposedLocalPlugins = new List<Type[]>();
                foreach (Type[] plugin in ExposedLocalPlugins)
                    actuallyExposedLocalPlugins.Add(plugin);

                foreach (String pluginInterfaceName in PluginsManager.Singleton.Configuration.PersistenceDriver.GetPluginInterfacesNames())
                    if (!pluginInterfaceName.Equals(typeof(IPluginsProvider).AssemblyQualifiedName))
                        foreach (String pluginImplementationAddress in PluginsManager.Singleton.Configuration.PersistenceDriver.GetPluginImplementationsAddresses(pluginInterfaceName))
                            if (pluginImplementationAddress.StartsWith("local:"))
                                try {

                                    Type pluginInterface = Type.GetType(pluginInterfaceName);
                                    Type pluginImplementation = Type.GetType(pluginImplementationAddress.Substring("local:".Length));

                                    bool founded = false;
                                    foreach (Type[] plugin in ExposedLocalPlugins)
                                        if (plugin[0] == pluginInterface && plugin[1] == pluginImplementation) {
                                            founded = true;
                                            break;
                                        }

                                    if (!founded)
                                        ExposeLocalPlugin(pluginInterface, pluginImplementation);

                                } catch (Exception ex) {
                                    Console.Error.WriteLine(ex.StackTrace);
                                }


            }// END Method RescanAutoExposedLocalPlugins


            public void ExposeLocalPlugin(Type localPluginInterface, Type localPluginImplementation)
            {

                foreach (Type[] plugin in exposedLocalPlugins)
                    if (plugin.Length == 2
                        && plugin[0] == localPluginInterface
                        && plugin[1] == localPluginImplementation)
                        return;

                exposedLocalPlugins.Add(new Type[]{localPluginInterface, localPluginImplementation});

                foreach (IPermissionsManagerListener listener in new List<IPermissionsManagerListener>(listeners))
                    listener.LocalPluginExposed(localPluginInterface, localPluginImplementation);

            }// END Method ExposeLocalPlugin


            public void UnexposeLocalPlugin(Type localPluginInterface, Type localPluginImplementation)
            {

                foreach (Type[] plugin in exposedLocalPlugins)
                    if (plugin.Length == 2
                        && plugin[0] == localPluginInterface
                        && plugin[1] == localPluginImplementation) {

                        foreach (IPermissionsManagerListener listener in new List<IPermissionsManagerListener>(listeners))
                            listener.LocalPluginUnexposing(localPluginInterface, localPluginImplementation);

                        exposedLocalPlugins.Remove(plugin);

                        break;

                    }

            }// END Method UnexposeLocalPlugin


        }// END Class PermissionsManagerImpl 


    }// END Classe PluginsManager


}// END Namespace Org.Lestr.Astenn
