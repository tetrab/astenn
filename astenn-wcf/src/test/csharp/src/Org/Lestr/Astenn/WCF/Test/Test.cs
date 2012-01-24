using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ServiceModel;

using NUnit.Framework;

using Org.Lestr.Astenn;
using Org.Lestr.Astenn.WCF;

namespace Org.Lestr.Astenn.WCF.Test
{


    [TestFixture]
    public class Test
    {


        [Test]
        public void Test1()
        {

            SOAPServer server = new SOAPServer();
            server.Start();

            PluginsManager.Singleton.RegisterPlugin(typeof(IPlugin), typeof(Plugin));
            PluginsManager.Singleton.Configuration.PermissionsManager.ExposeLocalPlugin(typeof(IPlugin), typeof(Plugin));

            PluginsManager.Singleton.RegisterPlugin(typeof(IPlugin), server.GetPluginRemoteAddress(typeof(IPlugin), typeof(Plugin)));

            foreach (IPlugin plugin in PluginsManager.Singleton.GetRegisteredRemotePlugins<IPlugin>())
                Assert.AreEqual("Bip", plugin.bip());

        }// FIN Méthode Test1

        
        [ServiceContract]
        public interface IPlugin
        {

            [OperationContract]
            string bip();

        }// FIN Interface IPlugin


        [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
        public class Plugin : IPlugin
        {


            public string bip()
            {

                return "Bip";

            }// FIN Méthode bip


        }// FIN Classe IPlugin


    }// FIN Classe Test


}// FIN Espace de nommage Org.Lestr.Astenn.WCF.Test
