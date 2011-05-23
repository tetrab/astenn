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


        private PluginsManager()
        {

            configuration = new ConfigurationImpl();

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

            if (!Configuration.PersistenceDriver.ExistPluginInterface(pluginInterfaceClass.AssemblyQualifiedName))
                Configuration.PersistenceDriver.AddPluginInterface(pluginInterfaceClass.AssemblyQualifiedName);

            if (!Configuration.PersistenceDriver.ExistPluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, pluginImplementationAddress))
            {

                Configuration.PersistenceDriver.AddPluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, pluginImplementationAddress);

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


        public void UnregisterPlugin(Type pluginInterfaceClass, Type pluginImplementationClass)
        {

            UnregisterPlugin(pluginInterfaceClass, "local:" + pluginImplementationClass.AssemblyQualifiedName);

        }// END Method UnregisterPlugin


        public void UnregisterPlugin(Type pluginInterfaceClass, string pluginImplementationAddress)
        {

            if (Configuration.PersistenceDriver.ExistPluginInterface(pluginInterfaceClass.AssemblyQualifiedName) &&
               Configuration.PersistenceDriver.ExistPluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, pluginImplementationAddress))
            {

                Configuration.PersistenceDriver.RemovePluginImplementation(pluginInterfaceClass.AssemblyQualifiedName, pluginImplementationAddress);

                if (!Configuration.PersistenceDriver.GetPluginImplementationsAddresses(pluginInterfaceClass.AssemblyQualifiedName).GetEnumerator().MoveNext())
                    Configuration.PersistenceDriver.RemovePluginInterface(pluginInterfaceClass.AssemblyQualifiedName);

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

            if (Configuration.PersistenceDriver.ExistPluginInterface(typeof(PluginInterfaceType).AssemblyQualifiedName))
                foreach (string pluginImplementationAddress in Configuration.PersistenceDriver.GetPluginImplementationsAddresses(typeof(PluginInterfaceType).AssemblyQualifiedName))
                {

                    try
                    {

                        foreach (IPluginsProvider provider in configuration.PluginsProviders)
                            if (pluginImplementationAddress.StartsWith(provider.Scheme + ":"))
                            {
                                rslt.Add(provider.GetPlugin<PluginInterfaceType>(pluginImplementationAddress));
                                break;
                            }

                    }
                    catch (System.Exception) { }

                }

            return rslt;

        }// END Method GetRegisteredPlugins


        public PluginInterfaceType GetRegisteredSingletonPlugin<PluginInterfaceType>()
        {

            IEnumerator<PluginInterfaceType> enumerator = GetRegisteredPlugins<PluginInterfaceType>().GetEnumerator();

            return enumerator.MoveNext() ? enumerator.Current : default(PluginInterfaceType);

        }// END Method GetRegisteredPlugins


        private class ConfigurationImpl : IConfiguration
        {


            private List<IPluginsProvider> pluginsProviders;
            private CompositePersistenceDriver persistenceDriver;
            IPermissionsManager permissionsManager;


            public ConfigurationImpl()
            {

                persistenceDriver = new CompositePersistenceDriver(new RAMPersistenceDriver(), new List<IPersistenceDriver>());
                permissionsManager = new PermissionsManagerImpl();
                
                pluginsProviders = new List<IPluginsProvider>();

                pluginsProviders.Add(new LocalPluginsProvider());

            }// END Constructor


            public CompositePersistenceDriver PersistenceDriver
            {

                get { return persistenceDriver; }

            }// END Property PersistenceDriver


            public IPermissionsManager PermissionsManager
            {

                get { return permissionsManager; }

            }// END Property PermissionsManager


            public List<IPluginsProvider> PluginsProviders
            {

                get { return pluginsProviders; }

            }// END Property PersistenceDriver


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
