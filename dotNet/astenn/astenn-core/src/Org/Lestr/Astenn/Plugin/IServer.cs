
using System;

namespace Org.Lestr.Astenn.Plugin
{
	
	
	public interface IServer
	{


        string Name { get;  }


        void Start();


        void Stop();


        string GetPluginRemoteAddress(Type pluginInterfaceClass, Type pluginImplementationClass);


    }// END Interface IServer


}// END Namespace Org.Lestr.Astenn.Plugin
