using System;
using System.Collections.Generic;
using System.Text;

using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;

using Org.Lestr.Astenn.Plugin;
using Org.Lestr.Astenn.Configuration;

namespace Org.Lestr.Astenn.DotNetRemoting
{


    public class DotNetRemotingServer : IServer
    {


        private const string NAME = ".NET Remoting";


        private bool ensureSecurity;


        private TcpChannel channel;


        private int port;


        private string host;


        private IPermissionsManagerListener listener;


        private Dictionary<string, MarshalByRefObject> exposed;


        public DotNetRemotingServer()
        {

            ensureSecurity = false;
            port = 1069;
            listener = new PermissionManagerListenerImpl(this);
            exposed = new Dictionary<string, MarshalByRefObject>();

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


        public bool EnsureSecurity
        {

            get { return ensureSecurity; }
            set { ensureSecurity = value; }

        }// END Property EnsureSecurity


        public string Name
        {

            get { return NAME; }

        }// END Property Name


        public void Start()
        {

            channel = new TcpChannel(Port);
            ChannelServices.RegisterChannel(channel, ensureSecurity);

            foreach (Type[] plugin in PluginsManager.Singleton.Configuration.PermissionsManager.ExposedLocalPlugins)
                ExposeLocalPlugin(plugin[0], plugin[1]);

            PluginsManager.Singleton.Configuration.PermissionsManager.Listeners.Add(listener);

        }// END Method Start


        public void Stop()
        {

            ChannelServices.UnregisterChannel(channel);

            foreach (Type[] plugin in PluginsManager.Singleton.Configuration.PermissionsManager.ExposedLocalPlugins)
                UnexposeLocalPlugin(plugin[0], plugin[1]);

            PluginsManager.Singleton.Configuration.PermissionsManager.Listeners.Remove(listener);

        }// END Method Stop


        public string GetPluginRemoteAddress(Type pluginInterfaceClass, Type pluginImplementationClass)
        {

            return DotNetRemotingPluginsProvider.SCHEME + ":tcp://" + Host + ":" + Port + "/" + pluginInterfaceClass.AssemblyQualifiedName + "/" + pluginImplementationClass.AssemblyQualifiedName;

        }// END Method GetPluginRemoteAddress


        internal void ExposeLocalPlugin(Type pluginInterface, Type pluginImplementation)
        {

            MarshalByRefObject pluginInstance = (MarshalByRefObject)pluginImplementation.GetConstructor(new Type[0]).Invoke(new object[0]);
            
            string pluginId = pluginInterface.AssemblyQualifiedName + "/" + pluginImplementation.AssemblyQualifiedName;

            RemotingServices.Marshal(pluginInstance, pluginId);

            exposed.Add(pluginId, pluginInstance);

        }// END Method ExposeLocalPlugin


        internal void UnexposeLocalPlugin(Type pluginInterface, Type pluginImplementation)
        {

            string pluginId = pluginInterface.AssemblyQualifiedName + "/" + pluginImplementation.AssemblyQualifiedName;
            
            MarshalByRefObject pluginInstance = exposed[pluginId];
            
            exposed.Remove(pluginId);

            RemotingServices.Disconnect(pluginInstance);

        }// END Method ExposeLocalPlugin


        private class PermissionManagerListenerImpl : IPermissionsManagerListener
        {


            private DotNetRemotingServer server;


            public PermissionManagerListenerImpl(DotNetRemotingServer server)
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


    }// END Class DotNetRemotingServer


}// END Namespace Org.Lestr.Astenn.DotNetRemoting
