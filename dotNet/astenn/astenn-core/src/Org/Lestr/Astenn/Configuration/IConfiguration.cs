using System;
using System.Collections.Generic;
using System.Text;

using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.Configuration
{


    public interface IConfiguration
    {


        CompositePersistenceDriver PersistenceDriver { get; }


        IPermissionsManager PermissionsManager { get; }


    }// END Interface IConfiguration


}// END Interface Org.Lestr.Astenn.Configuration
