using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

using NUnit.Framework;

using Org.Lestr.Astenn.Plugin;
using Org.Lestr.Astenn.Configuration;

namespace Org.Lestr.Astenn.Test
{

    [TestFixture]
    public class TestEmbbedXMLDocumentPersistenceDriver
    {


        [SetUp]
        public void setUp()
        {

            PluginsManager.Singleton.Configuration.PersistenceDriver.ReadWritePersistenceDriver = new EmbbedXMLDocumentPersistenceDriver();
            PluginsManager.Singleton.Configuration.PersistenceDriver.ReadOnlyPersistenceDrivers.Clear();

        }// END Method setUp


        [Test]
        public void test1()
        {

            IEnumerable<IPlugin> enumerator = PluginsManager.Singleton.GetRegisteredPlugins<IPlugin>();
            Assert.True(enumerator.Count() == 2);

            Assert.True(enumerator.ElementAt(0).GetType() == typeof(Plugin1));

            Assert.True(enumerator.ElementAt(1).GetType() == typeof(Plugin2));

        }// END Method test1


    }// END Class TestEmbbedXMLDocumentPersistenceDriver


}// END Namespace Org.Lestr.Astenn.Test