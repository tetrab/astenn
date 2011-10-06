using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.Bus.Impl
{


    public class AstennFirstUseListener : IAstennFirstUseListener
    {


        public void AstennInstanceStarting()
        {

            PluginsManager.Singleton.Configuration.PersistenceDriver.ReadOnlyPersistenceDrivers.Add(new BusPersistenceDriver());

        }// END Method AstennInstanceStarting


    }// END Class AstennFirstUseListener


}// END Namespace Org.Lestr.Astenn.Bus.Impl
