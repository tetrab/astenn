using System;
using System.Collections.Generic;
using System.Text;
using System.ServiceModel;

using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.WCF
{


    public class SOAPPluginsProvider : IPluginsProvider
    {


        internal const string SCHEME = "soap";


        public string Scheme
        {

            get { return SCHEME; }

        }// END Property Scheme


        public PluginInterfaceType GetPlugin<PluginInterfaceType>(string pluginImplementationAddress)
        {

            EndpointAddress endpointAddress = new EndpointAddress(pluginImplementationAddress.Substring(SCHEME.Length + +":".Length));

            BasicHttpBinding binding = new BasicHttpBinding();
            binding.MaxReceivedMessageSize = 25 * 1024 * 1024;
            binding.ReceiveTimeout = new TimeSpan(0, 2, 0);

            ChannelFactory<PluginInterfaceType> channelFactory = new ChannelFactory<PluginInterfaceType>(binding, endpointAddress);

            return channelFactory.CreateChannel(endpointAddress);

        }// END Method GetPlugin


    }// END Class SOAPPluginsProvider


}// END Namespace Org.Lestr.Astenn.WCF
