using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;

using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.SharePoint.Impl
{


    public class AstennFirstUseListener : IAstennFirstUseListener
    {


        public void AstennInstanceStarting()
        {

            PluginsManager.Singleton.Configuration.PersistenceDriver.ReadOnlyPersistenceDrivers.Add(SharePointPersistenceDriver.Singleton);

        }// END Namespace AstennInstanceStarting


    }// END Class SharePointPersistenceDriver


}// END Namespace Org.Lestr.Astenn.SharePoint.Impl
