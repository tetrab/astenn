using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ServiceModel;

using Org.Lestr.Astenn.Plugin;
using Org.Lestr.Astenn.Configuration;

namespace Org.Lestr.Astenn.WCF
{


    public class SOAPServer : IServer
    {


        private const string NAME = "WCF SOAP Server";


        private int port;


        private string host;


        private IPermissionsManagerListener listener;


        private Dictionary<string, ServiceHost> exposed;


        public SOAPServer()
        {
            
            port = 1069;
            listener = new PermissionManagerListenerImpl(this);
            exposed = new Dictionary<string, ServiceHost>();

        }// END Constructor


        public string Host
        {

            get { return host; }
            set { host = value; }

        }// END Property Host


        public int Port
        {

            get { return port; }
            set { port = value; }

        }// END Property Port


        public string Name
        {

            get { return NAME; }

        }// END Property Name


        public void Start()
        {

            foreach (Type[] plugin in PluginsManager.Singleton.Configuration.PermissionsManager.ExposedLocalPlugins)
                ExposeLocalPlugin(plugin[0], plugin[1]);

            PluginsManager.Singleton.Configuration.PermissionsManager.Listeners.Add(listener);

        }// END Method Start


        public void Stop()
        {

            foreach (Type[] plugin in PluginsManager.Singleton.Configuration.PermissionsManager.ExposedLocalPlugins)
                UnexposeLocalPlugin(plugin[0], plugin[1]);

            PluginsManager.Singleton.Configuration.PermissionsManager.Listeners.Remove(listener);

        }// END Method Stop


        public string GetPluginRemoteAddress(Type pluginInterfaceClass, Type pluginImplementationClass)
        {

            return SOAPPluginsProvider.SCHEME + ":http://" + Host + ":" + Port + "/" + pluginInterfaceClass.AssemblyQualifiedName + "/" + pluginImplementationClass.AssemblyQualifiedName;

        }// END Method GetPluginRemoteAddress


        internal void ExposeLocalPlugin(Type pluginInterface, Type pluginImplementation)
        {

            string pluginId = pluginInterface.AssemblyQualifiedName + "/" + pluginImplementation.AssemblyQualifiedName;
            Uri pluginUrl = new Uri("http://" + Host + ":" + Port + "/" + pluginId);
            object pluginInstance = pluginImplementation.GetConstructor(new Type[0]).Invoke(new object[0]);

            BasicHttpBinding binding = new BasicHttpBinding();
            binding.MaxReceivedMessageSize = 25 * 1024 * 1024;
            binding.ReceiveTimeout = new TimeSpan(0, 2, 0);

            ServiceHost service = new ServiceHost(pluginInstance, pluginUrl);
            service.AddServiceEndpoint(pluginInterface, binding, pluginUrl);
            service.Open();

            exposed.Add(pluginId, service);

        }// END Method ExposeLocalPlugin


        internal void UnexposeLocalPlugin(Type pluginInterface, Type pluginImplementation)
        {

            string pluginId = pluginInterface.AssemblyQualifiedName + "/" + pluginImplementation.AssemblyQualifiedName;

            ServiceHost service = exposed[pluginId];
            service.Close();
            
            exposed.Remove(pluginId);

        }// END Method ExposeLocalPlugin


        private class PermissionManagerListenerImpl : IPermissionsManagerListener
        {


            private SOAPServer server;


            public PermissionManagerListenerImpl(SOAPServer server)
            {

                this.server = server;

            }// END Constructor


            public void LocalPluginExposed(Type localPluginInterface, Type localPluginImplementation)
            {

                server.ExposeLocalPlugin(localPluginInterface, localPluginImplementation);

            }// END Méthode LocalPluginExposed


            public void LocalPluginUnexposing(Type localPluginInterface, Type localPluginImplementation)
            {

                server.ExposeLocalPlugin(localPluginInterface, localPluginImplementation);

            }// END Méthode LocalPluginUnexposing


        }// END PermissionManagerListenerImpl


    }// FIN Classe WCFServer


}// FIN Espace de nommage Org.Lestr.Astenn.WCF
