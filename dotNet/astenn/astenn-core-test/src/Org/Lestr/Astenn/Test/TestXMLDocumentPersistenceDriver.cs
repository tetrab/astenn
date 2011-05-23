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
    public class TestXMLDocumentPersistenceDriver
    {
    

        protected IPersistenceDriver persistenceDriver;


        [SetUp]
        public void setUp() {

            persistenceDriver = new XMLDocumentPersistenceDriver();

        }// END Method setUp


        [Test]
        public void test1() {

            Assert.False(persistenceDriver.GetPluginInterfacesNames().Count() > 0);
            Assert.False(persistenceDriver.ExistPluginInterface("test"));

            persistenceDriver.AddPluginInterface("test");

            Assert.True(persistenceDriver.GetPluginInterfacesNames().Count() > 0);
            Assert.True(persistenceDriver.ExistPluginInterface("test"));
            Assert.False(persistenceDriver.ExistPluginImplementation("test", "local:test"));

            persistenceDriver.AddPluginImplementation("test", "local:test");

            Assert.True(persistenceDriver.GetPluginImplementationsAddresses("test").Count() > 0);
            Assert.True(persistenceDriver.ExistPluginInterface("test"));
            Assert.True(persistenceDriver.ExistPluginImplementation("test", "local:test"));
            Assert.False(persistenceDriver.ExistPluginImplementation("test", "local:test2"));

            persistenceDriver.RemovePluginImplementation("test", "local:test");

            Assert.True(persistenceDriver.GetPluginInterfacesNames().Count() > 0);
            Assert.False(persistenceDriver.GetPluginImplementationsAddresses("test").Count() > 0);
            Assert.True(persistenceDriver.ExistPluginInterface("test"));
            Assert.False(persistenceDriver.ExistPluginImplementation("test", "local:test"));
            
            persistenceDriver.RemovePluginInterface("test");

            Assert.False(persistenceDriver.GetPluginInterfacesNames().Count() > 0);
            Assert.False(persistenceDriver.ExistPluginInterface("test"));

        }// END Method test1


    }// END Class TestXMLDocumentPersistenceDriver


}// END Namespace Org.Lestr.Astenn.Test