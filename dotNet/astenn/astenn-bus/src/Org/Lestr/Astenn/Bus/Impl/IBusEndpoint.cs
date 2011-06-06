using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.Bus.Impl
{


    public interface IBusEndpoint
    {


        string GetEndpointId(string busId);


        string[] GetPluginInterfacesNames(string busId);


        string[] GetPluginImplementationsAddresses(string busId, string pluginInterfaceClassName);


        string[] GetKnowBusEndpointsAddresses(string busId);


        void DeclareBusEndpointAddress(string busId, string address);


    }// END Class IBusEndpoint


}// END Namespace Org.Lestr.Astenn.Bus.Impl
