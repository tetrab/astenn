using System;
using System.Collections.Generic;
using System.Text;

namespace Org.Lestr.Astenn.Configuration
{


    public interface IPermissionsManager {


        List<IPermissionsManagerListener> Listeners { get; }

        
        bool AutoExposeLocalPlugins { get; set; }


        void RescanAutoExposedLocalPlugins();


        void ExposeLocalPlugin(Type localPluginInterface,
                               Type localPluginImplementation);


        void UnexposeLocalPlugin(Type localPluginInterface,
                                 Type localPluginImplementation);


        IEnumerable<Type[]> ExposedLocalPlugins { get; }


    }// END Interface IPermissionsManager


    public interface IPermissionsManagerListener
    {


        void LocalPluginExposed(Type localPluginInterface,
                                Type localPluginImplementation);


        void LocalPluginUnexposing(Type localPluginInterface,
                                   Type localPluginImplementation);


    }// END Interface IPermissionsManagerListener


}// END Namespace Org.Lestr.Astenn.Configuration
