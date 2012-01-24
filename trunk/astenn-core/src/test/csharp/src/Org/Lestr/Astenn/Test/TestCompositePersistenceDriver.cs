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
    public class TestCompositePersistenceDriver
    {

        protected IPersistenceDriver persistenceDriver;
        protected IPersistenceDriver writeReadPersistenceDriver;
        protected IPersistenceDriver readPersistenceDriver;


        [SetUp]
        public void setUp() {

            writeReadPersistenceDriver = new RAMPersistenceDriver();
            readPersistenceDriver = new RAMPersistenceDriver();
            persistenceDriver = new CompositePersistenceDriver(writeReadPersistenceDriver, new IPersistenceDriver[]{ readPersistenceDriver });

        }// END Method setUp


        [Test]
        public void test1()
        {

            persistenceDriver.AddPluginInterface("test");

            Assert.True(persistenceDriver.GetPluginInterfacesNames().Count() > 0);
            Assert.True(persistenceDriver.ExistPluginInterface("test"));
            Assert.True(writeReadPersistenceDriver.GetPluginInterfacesNames().Count() > 0);
            Assert.True(writeReadPersistenceDriver.ExistPluginInterface("test"));
            Assert.False(readPersistenceDriver.GetPluginInterfacesNames().Count() > 0);
            Assert.False(readPersistenceDriver.ExistPluginInterface("test"));

            persistenceDriver.AddPluginImplementation("test", "local:test");

            Assert.True(persistenceDriver.GetPluginImplementationsAddresses("test").Count() > 0);
            Assert.True(persistenceDriver.ExistPluginImplementation("test", "local:test"));
            Assert.True(writeReadPersistenceDriver.ExistPluginImplementation("test", "local:test"));
            Assert.False(readPersistenceDriver.ExistPluginImplementation("test", "local:test"));

            persistenceDriver.RemovePluginImplementation("test", "local:test");

            Assert.True(persistenceDriver.ExistPluginInterface("test"));
            Assert.False(persistenceDriver.ExistPluginImplementation("test", "local:test"));
            Assert.True(writeReadPersistenceDriver.ExistPluginInterface("test"));
            Assert.False(writeReadPersistenceDriver.ExistPluginImplementation("test", "local:test"));
            Assert.False(readPersistenceDriver.ExistPluginInterface("test"));
            Assert.False(readPersistenceDriver.ExistPluginImplementation("test", "local:test"));

            persistenceDriver.RemovePluginInterface("test");

            Assert.False(persistenceDriver.ExistPluginInterface("test"));
            Assert.False(writeReadPersistenceDriver.ExistPluginInterface("test"));
            Assert.False(readPersistenceDriver.ExistPluginInterface("test"));

            readPersistenceDriver.AddPluginInterface("test");
            readPersistenceDriver.AddPluginImplementation("test", "local:test");

            Assert.True(persistenceDriver.ExistPluginInterface("test"));
            Assert.False(writeReadPersistenceDriver.ExistPluginInterface("test"));
            Assert.True(readPersistenceDriver.ExistPluginInterface("test"));
            Assert.True(persistenceDriver.ExistPluginImplementation("test", "local:test"));
            Assert.False(writeReadPersistenceDriver.ExistPluginImplementation("test", "local:test"));
            Assert.True(readPersistenceDriver.ExistPluginImplementation("test", "local:test"));

            readPersistenceDriver.RemovePluginImplementation("test", "local:test");

            Assert.False(persistenceDriver.ExistPluginImplementation("test", "local:test"));
            Assert.False(writeReadPersistenceDriver.ExistPluginImplementation("test", "local:test"));
            Assert.False(readPersistenceDriver.ExistPluginImplementation("test", "local:test"));

            readPersistenceDriver.RemovePluginInterface("test");

            Assert.False(persistenceDriver.ExistPluginInterface("test"));
            Assert.False(writeReadPersistenceDriver.ExistPluginInterface("test"));
            Assert.False(readPersistenceDriver.ExistPluginInterface("test"));
            
        }// END Method test1


    }// END Class TestCompositePersistenceDriver


}// END Namespace Org.Lestr.Astenn.Test