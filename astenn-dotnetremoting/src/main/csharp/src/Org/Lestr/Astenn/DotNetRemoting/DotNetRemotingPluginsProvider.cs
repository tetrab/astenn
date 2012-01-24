using System;
using System.Collections.Generic;
using System.Text;

using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;

using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.DotNetRemoting
{


    public class DotNetRemotingPluginsProvider : IPluginsProvider
    {


        private static object lockObject = new object();


        private static TcpChannel channel;


        internal const string SCHEME = "dotnetremoting";


        public string Scheme
        {

            get { return SCHEME; }

        }// END Property Scheme


        public PluginInterfaceType GetPlugin<PluginInterfaceType>(string pluginImplementationAddress)
        {

            lock(lockObject) 
                if(channel == null)
                    ChannelServices.RegisterChannel(channel = new TcpChannel(), false);

            return (PluginInterfaceType)Activator.GetObject(typeof(PluginInterfaceType), pluginImplementationAddress.Substring(Scheme.Length + ":".Length));

        }// END Method GetPlugin


    }// END Class DotNetRemotingPluginsProvider


}// END Namespace Org.Lestr.Astenn.DotNetRemoting
